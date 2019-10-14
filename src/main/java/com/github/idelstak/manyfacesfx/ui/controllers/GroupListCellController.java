/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GroupsRepository;
import com.github.idelstak.manyfacesfx.model.Group;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class GroupListCellController {

    private static final Logger LOG;
    private static final GroupsRepository GROUPS_REPO = GroupsRepository.getDefault();
    @FXML
    private Label groupNameLabel;
    @FXML
    private JFXButton editNameButton;
    @FXML
    private StackPane editGroupNamePane;
    @FXML
    private JFXTextField editGroupNameField;
    @FXML
    private JFXButton noButton;
    @FXML
    private JFXButton yesButton;
    @FXML
    private HBox editGroupNameActionsBox;
    @FXML
    private JFXButton deleteGroupButton;
    @FXML
    private VBox confirmDeleteBox;
    @FXML
    private JFXButton doDeleteButton;
    @FXML
    private JFXButton dontDeleteButton;

    private final SimpleObjectProperty<Group> groupProperty;

    static {
        LOG = Logger.getLogger(GroupListCellController.class.getName());
    }

    {
        groupProperty = new SimpleObjectProperty<>();
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        editNameButton.visibleProperty().bind(editGroupNamePane.visibleProperty().not());
        groupNameLabel.visibleProperty().bind(editGroupNamePane.visibleProperty().not());

        editNameButton.setOnAction(e -> editGroupNamePane.setVisible(true));
        noButton.setOnAction(e -> editGroupNamePane.setVisible(false));
        yesButton.setOnAction(e -> {
            Group group = groupProperty.getValue();
            group.setName(editGroupNameField.getText());
            updateDisplayedNames(editGroupNameField.getText());
            editGroupNamePane.setVisible(false);

            GROUPS_REPO.update(groupProperty.get());
        });

        editGroupNamePane.visibleProperty().addListener((o, ov, nv) -> {
            if (nv) {
                Group group = groupProperty.getValue();
                editGroupNameField.setText(group.getName());
                Platform.runLater(editGroupNameField::requestFocus);
            }
        });

        deleteGroupButton.visibleProperty().bind(confirmDeleteBox.visibleProperty().not());

        deleteGroupButton.setOnAction(e -> confirmDeleteBox.setVisible(true));
        doDeleteButton.setOnAction(e -> {
            GROUPS_REPO.delete(groupProperty.get());
        });
        dontDeleteButton.setOnAction(e -> confirmDeleteBox.setVisible(false));

        groupProperty.addListener((o, ov, nv) -> {
            updateDisplayedNames(nv.getName());
        });
    }

    void setGroup(Group group) {
        if (group == null) {
            throw new IllegalArgumentException("Group should not be null");
        }
        groupProperty.set(group);
    }

    private void updateDisplayedNames(String groupName) {
        groupNameLabel.setText(groupName);
        editGroupNameField.setText(groupName);
    }

}
