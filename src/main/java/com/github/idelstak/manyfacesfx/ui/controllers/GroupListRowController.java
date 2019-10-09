/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.model.Group;
import com.github.idelstak.manyfacesfx.ui.util.TitledPaneInputEventBypass;
import java.util.Objects;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.HBox;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class GroupListRowController {

    @FXML
    private TitledPane titledPane;
    @FXML
    private HBox titleHbox;
    @FXML
    private Label groupNameLabel;
    @FXML
    private Label noOfProfilesLabel;

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        titledPane.addEventFilter(InputEvent.ANY, new TitledPaneInputEventBypass());
    }

    void setGroup(Group inst) {
        String message = "Group should not be null";
        Group group = Objects.requireNonNull(inst, message);

        Platform.runLater(() -> {
            groupNameLabel.textProperty().bind(group.nameProperty());
            noOfProfilesLabel.textProperty().bind(Bindings.createStringBinding(
                    () -> Integer.toString(group.getNumberOfProfiles()),
                    group.numberOfProfilesProperty()));
        });
    }

}
