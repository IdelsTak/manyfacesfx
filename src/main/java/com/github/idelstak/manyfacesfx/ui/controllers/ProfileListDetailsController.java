/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.api.ProfilesRepository;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.ProfileNode;
import com.github.idelstak.manyfacesfx.ui.SelectProfiles;
import com.github.idelstak.manyfacesfx.ui.util.TitledPaneInputEventBypass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleNode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.HBox;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileListDetailsController {

    private static final Logger LOG = Logger.getLogger(ProfileListDetailsController.class.getName());
    private static final GlobalContext CONTEXT = GlobalContext.getDefault();
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
    private final Lookup.Result<ProfileNode> lookupResult;
    private final SimpleBooleanProperty selectionAvailable;

    {
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

        ObservableSet<Profile> profiles = ProfilesRepository.getDefault().findAll();

        profiles.stream()
                .map(ProfileNode::new)
                .map(node -> node.getLookup().lookup(TitledPane.class))
                .forEach(profileListAccordion.getPanes()::add);

        nameToggle.selectedProperty()
                .addListener((ob, ov, selected) -> {
                    profileListAccordion.getPanes()
                            .sort(selected
                                  ? Comparator.comparing(
                                            Node::getId,
                                            this::compareStrings).reversed()
                                  : Comparator.comparing(
                                            Node::getId,
                                            this::compareStrings));
                });
        lastEditedToggle.selectedProperty()
                .addListener((ob, ov, selected) -> {
                    profileListAccordion.getPanes()
                            .sort(selected
                                  ? Comparator.comparing(
                                            Node::getId,
                                            this::compareDates).reversed()
                                  : Comparator.comparing(
                                            Node::getId,
                                            this::compareDates));
                });

        SelectProfiles selectProfiles = new SelectProfiles();

        selectProfiles.selectProperty().bind(checkBoxSelected);
        selectProfiles.visibleProperty().bind(showActionsToggle.selectedProperty());

        CONTEXT.add(selectProfiles);

        deleteButton.disableProperty().bind(selectionAvailable.not());
        moveToGroupButton.disableProperty().bind(selectionAvailable.not());
        removeFromGroupButton.disableProperty().bind(selectionAvailable.not());

        lookupResult.addLookupListener(e -> listenToSelectedProfiles());
    }

    private int compareDates(String id1, String id2) {
        LocalDate ldt1 = LocalDate.parse(id1.split(":")[1]);
        LocalDate ldt2 = LocalDate.parse(id2.split(":")[1]);

        return ldt1.compareTo(ldt2);
    }

    private int compareStrings(String id1, String id2) {
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
