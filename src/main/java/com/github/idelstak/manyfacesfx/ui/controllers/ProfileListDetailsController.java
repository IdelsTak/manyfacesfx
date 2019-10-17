/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.api.ProfilesRepository;
import com.github.idelstak.manyfacesfx.model.Group;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.ProfileNode;
import com.github.idelstak.manyfacesfx.ui.SelectProfiles;
import com.github.idelstak.manyfacesfx.ui.util.TitledPaneInputEventBypass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleNode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener.Change;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileListDetailsController {

    private static final Logger LOG = Logger.getLogger(ProfileListDetailsController.class.getName());
    private static final GlobalContext CONTEXT = GlobalContext.getDefault();
    private static final ProfilesRepository PROFILES_REPO = ProfilesRepository.getDefault();
    @FXML
    private VBox rootBox;
    @FXML
    private TitledPane actionsPane;
    @FXML
    private JFXCheckBox selectCheckBox;
    @FXML
    private JFXButton selectButton;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private JFXButton moveToGroupButton;
    @FXML
    private JFXButton removeFromGroupButton;
    @FXML
    private HBox searchPane;
    @FXML
    private JFXTextField searchField;
    @FXML
    private HBox spacerBox;
    @FXML
    private JFXToggleNode showActionsToggle;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private HBox headersBox;
    @FXML
    private JFXToggleNode nameToggle;
    @FXML
    private JFXToggleNode lastEditedToggle;
    @FXML
    private Accordion profileListAccordion;
    @FXML
    private Label groupNameLabel;
    @FXML
    private HBox groupNameBox;
    private final Collection<TitledPane> titledPanes;
    private final Lookup.Result<ProfileNode> lookupResult;
    private final SimpleBooleanProperty selectionAvailable;
    private final SelectProfiles selectProfiles = new SelectProfiles();
    private Group group;

    {
        titledPanes = new ArrayList<>();
        lookupResult = CONTEXT.lookupResult(ProfileNode.class);
        selectionAvailable = new SimpleBooleanProperty(false);
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        actionsPane.addEventFilter(InputEvent.ANY, new TitledPaneInputEventBypass());
        actionsPane.expandedProperty().bind(showActionsToggle.selectedProperty());
        BooleanProperty checkBoxSelected = selectCheckBox.selectedProperty();

        selectButton.textProperty().bind(
                Bindings.createStringBinding(() -> selectCheckBox.isSelected()
                                                   ? "Deselect all"
                                                   : "Select all", checkBoxSelected));

        selectButton.setOnAction(e -> selectCheckBox.setSelected(!selectCheckBox.isSelected()));

        selectProfiles.selectProperty().bind(checkBoxSelected);
        selectProfiles.visibleProperty().bind(showActionsToggle.selectedProperty());

        CONTEXT.set(SelectProfiles.class, selectProfiles);

        PROFILES_REPO.addListener((Change<? extends Profile> change) -> refreshProfilesList());

        searchField.textProperty().addListener((ob, ov, name) -> filterBy(name));
        nameToggle.selectedProperty().addListener((ob, ov, descending) -> {
            sortByName(descending);
        });
        lastEditedToggle.selectedProperty().addListener((ob, ov, descending) -> {
            sortByLastEdited(descending);
        });

        deleteButton.disableProperty().bind(selectionAvailable.not());
        moveToGroupButton.disableProperty().bind(selectionAvailable.not());
        removeFromGroupButton.disableProperty().bind(selectionAvailable.not());

        lookupResult.addLookupListener(e -> listenToSelectedProfiles());

//        groupNameBox.setVisible(false);
        Platform.runLater(() -> rootBox.getChildren().remove(groupNameBox));
    }

    void setGroup(Group inst) {
        String message = "Group should not be null";
        group = Objects.requireNonNull(inst, message);

        groupNameLabel.textProperty().bind(group.nameProperty());

//        group.nameProperty().addListener((ob, ov, nv) -> {
//            LOG.log(Level.INFO, "Group name change occured: nv = {0}", nv);
//        });
//        Platform.runLater(() -> groupNameBox.setVisible(true));
        Platform.runLater(() -> rootBox.getChildren().add(2, groupNameBox));

        refreshProfilesList();
    }

    private void sortByLastEdited(Boolean descending) {
        profileListAccordion.getPanes().sort(
                descending
                ? Comparator.comparing(
                                Node::getId,
                                this::compareDates).reversed()
                : Comparator.comparing(
                                Node::getId,
                                this::compareDates));
    }

    private void sortByName(boolean descending) {
        profileListAccordion.getPanes().sort(
                descending
                ? Comparator.comparing(
                                Node::getId,
                                this::compareNames).reversed()
                : Comparator.comparing(
                                Node::getId,
                                this::compareNames));
    }

    private void filterBy(String profileName) {
        if (profileName == null || profileName.trim().isEmpty()) {
            Platform.runLater(() -> profileListAccordion.getPanes().setAll(titledPanes));
        } else {
            List<TitledPane> result = profileListAccordion.getPanes()
                    .stream()
                    .filter(tp -> {
                        String name = tp.getId().split(":")[0];
                        return name.toLowerCase().contains(profileName.toLowerCase());
                    })
                    .collect(Collectors.toList());
            Platform.runLater(() -> profileListAccordion.getPanes().setAll(result));
        }
    }

    private void refreshProfilesList() {
        ObservableSet<Profile> allProfiles = PROFILES_REPO.findAll();
        Collection<Profile> profiles = new ArrayList<>(
                group != null
                ? allProfiles.stream()
                                .filter(profile -> profile.getGroup().equals(group))
                                .collect(Collectors.toList())
                : allProfiles);

        List<TitledPane> panes = profiles.stream()
                .map(profile -> new ProfileNode(profile))
                .map(node -> node.getLookup().lookup(TitledPane.class))
                .collect(Collectors.toList());

        titledPanes.clear();
        titledPanes.addAll(panes);

        Platform.runLater(() -> profileListAccordion.getPanes().setAll(panes));
    }

    private int compareDates(String id1, String id2) {
        LocalDate ldt1 = LocalDate.parse(id1.split(":")[1]);
        LocalDate ldt2 = LocalDate.parse(id2.split(":")[1]);

        return ldt1.compareTo(ldt2);
    }

    private int compareNames(String id1, String id2) {
        String name1 = id1.split(":")[0];
        String name2 = id2.split(":")[0];

        return name1.compareTo(name2);
    }

    private void listenToSelectedProfiles() {
        List<Profile> selectedProfiles = lookupResult.allInstances()
                .stream()
                .map(node -> node.getLookup().lookup(Profile.class))
                .collect(Collectors.toList());

        selectionAvailable.set(!selectedProfiles.isEmpty());
    }

}
