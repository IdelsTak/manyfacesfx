/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.api.ProfilesRepository;
import com.github.idelstak.manyfacesfx.model.Group;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.BulkProfilesSelect;
import com.github.idelstak.manyfacesfx.ui.DeleteProfileDialog;
import com.github.idelstak.manyfacesfx.ui.MoveProfileToGroupDialog;
import com.github.idelstak.manyfacesfx.ui.ProfileNode;
import com.github.idelstak.manyfacesfx.ui.UngroupProfileDialog;
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
    private TitledPane bulkActionsPane;
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
    private JFXTextField searchField;
    @FXML
    private JFXToggleNode bulkProfileActionsToggle;
    @FXML
    private JFXButton refreshButton;
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
    private final Lookup.Result<ProfileNode> profileNodeResult;
    private final SimpleBooleanProperty selectionAvailable;
    private final BulkProfilesSelect bulkProfilesSelect = new BulkProfilesSelect();
    private Group group;

    {
        titledPanes = new ArrayList<>();
        profileNodeResult = CONTEXT.lookupResult(ProfileNode.class);
        selectionAvailable = new SimpleBooleanProperty(false);
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        bulkActionsPane.addEventFilter(InputEvent.ANY, new TitledPaneInputEventBypass());
        bulkActionsPane.expandedProperty().bind(bulkProfileActionsToggle.selectedProperty());

        selectButton.textProperty().bind(Bindings.createStringBinding(
                () -> selectCheckBox.isSelected()
                      ? "Deselect all"
                      : "Select all",
                selectCheckBox.selectedProperty()));

        selectButton.setOnAction(e -> selectCheckBox.setSelected(!selectCheckBox.isSelected()));

        bulkProfilesSelect.selectProperty().bind(selectCheckBox.selectedProperty());
        bulkProfilesSelect.visibleProperty().bind(bulkProfileActionsToggle.selectedProperty());

        searchField.textProperty()
                .addListener((ob, ov, name) -> filterBy(name));
        nameToggle.selectedProperty()
                .addListener((ob, ov, descending) -> sortByName(descending));
        lastEditedToggle.selectedProperty()
                .addListener((ob, ov, descending) -> sortByLastEdited(descending));

        deleteButton.disableProperty().bind(selectionAvailable.not());
        moveToGroupButton.disableProperty().bind(selectionAvailable.not());
        removeFromGroupButton.disableProperty().bind(selectionAvailable.not());

        profileNodeResult.addLookupListener(e -> listenToSelectedProfiles());

        deleteButton.setOnAction(e -> deleteSelectedProfiles());
        moveToGroupButton.setOnAction(e -> moveSelectedProfiles());
        removeFromGroupButton.setOnAction(e -> ungroupSelectedProfiles());

        Platform.runLater(() -> rootBox.getChildren().remove(groupNameBox));

        PROFILES_REPO.addListener((Change<? extends Profile> change) -> refreshProfilesList());
    }

    void setGroup(Group inst) {
        String message = "Group should not be null";
        group = Objects.requireNonNull(inst, message);

        groupNameLabel.textProperty().bind(group.nameProperty());

        Platform.runLater(() -> rootBox.getChildren().add(2, groupNameBox));

        refreshProfilesList();
    }

    private void deleteSelectedProfiles() {
        Profile[] profiles = getNonNullProfiles();
        DeleteProfileDialog dialog = new DeleteProfileDialog();
        dialog.setProfiles(profiles);
        dialog.setOnDialogClosed(handler -> {
            if (dialog.profileDeleted()) {
                removeSelectedNodesFromContext();
            }
        });
        dialog.show();
    }

    private void moveSelectedProfiles() {
        Profile[] profiles = getNonNullProfiles();
        MoveProfileToGroupDialog dialog = new MoveProfileToGroupDialog();
        dialog.setProfiles(profiles);
        dialog.setOnDialogClosed(handler -> {
            if (dialog.profileMoved()) {
                removeSelectedNodesFromContext();
            }
        });
        dialog.show();
    }

    private void ungroupSelectedProfiles() {
        Profile[] profiles = getNonNullProfiles();
        UngroupProfileDialog dialog = new UngroupProfileDialog();
        dialog.setProfiles(profiles);
        dialog.setOnDialogClosed(handler -> {
            if (dialog.profileUngrouped()) {
                removeSelectedNodesFromContext();
            }
        });
        dialog.show();
    }

    private Profile[] getNonNullProfiles() {
        return profileNodeResult.allInstances()
                .stream()
                .map(ProfileNode::getLookup)
                .map(lookup -> lookup.lookup(Profile.class))
                .filter(Objects::nonNull)
                .toArray(Profile[]::new);
    }

    private void removeSelectedNodesFromContext() {
        profileNodeResult.allInstances().forEach(CONTEXT::remove);
    }

    private void sortByLastEdited(Boolean descending) {
        profileListAccordion.getPanes()
                .sort(descending
                      ? Comparator.comparing(
                                Node::getId,
                                this::compareDates).reversed()
                      : Comparator.comparing(
                                Node::getId,
                                this::compareDates));
    }

    private void sortByName(boolean descending) {
        profileListAccordion.getPanes()
                .sort(descending
                      ? Comparator.comparing(
                                Node::getId,
                                this::compareNames).reversed()
                      : Comparator.comparing(
                                Node::getId,
                                this::compareNames));
    }

    private void filterBy(String filter) {
        Collection<TitledPane> filteredCollection = new ArrayList<>();

        if (filter == null || filter.trim().isEmpty()) {
            filteredCollection.addAll(titledPanes);
        } else {
            filteredCollection.addAll(profileListAccordion.getPanes()
                    .stream()
                    .filter(tp -> {
                        String name = tp.getId().split(":")[0];
                        return name.toLowerCase().contains(filter.toLowerCase());
                    })
                    .collect(Collectors.toList()));
        }

        Platform.runLater(() -> {
            profileListAccordion.getPanes().setAll(filteredCollection);
        });
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
                .map(profile -> new ProfileNode(profile, bulkProfilesSelect))
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
        List<Profile> selectedNodes = profileNodeResult.allInstances()
                .stream()
                .map(node -> node.getLookup().lookup(Profile.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        selectionAvailable.set(!selectedNodes.isEmpty());
    }
}
