/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.model.Group;
import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class MoveProfileCellController {

    @FXML
    private Label groupNameLabel;
    @FXML
    private JFXCheckBox selectGroupCheckBox;

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        // TODO
    }

    void setGroup(Group group) {
        if (group == null) {
            throw new IllegalArgumentException("Group should not be null");
        }

        Platform.runLater(() -> groupNameLabel.setText(group.getName()));
    }

    JFXCheckBox getSelectGroupCheckBox() {
        return selectGroupCheckBox;
    }

}
