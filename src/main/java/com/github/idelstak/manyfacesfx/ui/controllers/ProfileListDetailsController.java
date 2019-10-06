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
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.HBox;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileListDetailsController {

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
    private Accordion profileListAccordion;

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

        deleteButton.disableProperty().bind(checkBoxSelected.not());
        moveToGroupButton.disableProperty().bind(checkBoxSelected.not());
        removeFromGroupButton.disableProperty().bind(checkBoxSelected.not());

        ObservableSet<Profile> profiles = ProfilesRepository.getDefault().findAll();

        profiles.stream()
                .map(ProfileNode::new)
                .map(node -> node.getLookup().lookup(TitledPane.class))
                .forEach(profileListAccordion.getPanes()::add);

        SelectProfiles selectProfiles = new SelectProfiles();

        selectProfiles.selectProperty().bind(checkBoxSelected);
        selectProfiles.visibleProperty().bind(showActionsToggle.selectedProperty());

        GlobalContext.getDefault().add(selectProfiles);
    }

}
