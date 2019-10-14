/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.model.Group;
import java.util.Objects;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class GroupListRowController {

    private static final Logger LOG = Logger.getLogger(GroupListRowController.class.getName());
    @FXML
    private TitledPane titledPane;
    @FXML
    private HBox titleHbox;
    @FXML
    private Label groupNameLabel;
    @FXML
    private Label noOfProfilesLabel;
    private Group group;

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        titleHbox.minWidthProperty().bind(titledPane.widthProperty());
        titledPane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            GlobalContext.getDefault().set(Group.class, group);
        });
    }

    void setGroup(Group inst) {
        String message = "Group should not be null";
        group = Objects.requireNonNull(inst, message);

        groupNameLabel.textProperty().bind(group.nameProperty());
        noOfProfilesLabel.textProperty().bind(Bindings.createStringBinding(
                () -> Integer.toString(group.getNumberOfProfiles()),
                group.numberOfProfilesProperty()));
    }

}
