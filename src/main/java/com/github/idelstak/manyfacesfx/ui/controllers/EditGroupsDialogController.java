/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GroupsRepository;
import com.github.idelstak.manyfacesfx.model.Group;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class EditGroupsDialogController {

    private static final Logger LOG;
    private static final GroupsRepository GROUPS_REPO = GroupsRepository.getDefault();
    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private StackPane listActionsPane;
    @FXML
    private JFXButton addNewgroupButton;
    @FXML
    private JFXTextField groupNameField;
    @FXML
    private JFXButton applyGroupNameButton;
    @FXML
    private JFXListView<Group> groupsListView;
    @FXML
    private Text noGroupsFoundText;
    private ObservableList<Group> groups;

    static {
        LOG = Logger.getLogger(EditGroupsDialogController.class.getName());
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        BooleanProperty nameFieldprop = groupNameField.visibleProperty();
        BooleanBinding nameFieldNotProp = nameFieldprop.not();

        addNewgroupButton.visibleProperty().bind(nameFieldNotProp);
        applyGroupNameButton.visibleProperty().bind(nameFieldprop);

        addNewgroupButton.setOnAction(e -> groupNameField.setVisible(true));
        applyGroupNameButton.setOnAction(e -> {
            GROUPS_REPO.add(groupNameField.getText());
            groupNameField.setText(null);
            groupNameField.setVisible(false);
        });

        groups = GROUPS_REPO.findAll();

        GROUPS_REPO.addListener((Change<? extends Group> change) -> {
            noGroupsFoundText.setVisible(change.getList().size() <= 1);
            groupsListView.setVisible(change.getList().size() > 1);

            Platform.runLater(() -> updateListView());
        });

        noGroupsFoundText.setVisible(groups.size() <= 1);
        groupsListView.setVisible(groups.size() > 1);

        updateListView();
    }

    void setDialog(JFXDialog dialog) {
        if (dialog == null) {
            throw new IllegalArgumentException("Dialog should not be null");
        }
        closeButton.setOnAction(e -> dialog.close());
        cancelButton.setOnAction(e -> dialog.close());
    }

    private void updateListView() {
        groupsListView.setCellFactory(l -> new GroupCell());
        groupsListView.getItems().setAll(groups);
    }

    private class GroupCell extends ListCell<Group> {

        private GroupCell() {
            //DnD implementations
            setOnDragDetected(this::initDragContent);
            setOnDragOver(this::acceptMoveTransferMode);
            setOnDragEntered(this::setDragEnteredOpacity);
            setOnDragExited(this::setDragExitedOpacity);
            setOnDragDropped(this::consumeDragDrop);
        }

        @Override
        protected void updateItem(Group group, boolean empty) {
            super.updateItem(group, empty);

            if (group != null && !empty) {
                setGraphic(getHBox(group));
            }
        }

        private void setDragExitedOpacity(DragEvent event) {
            if (event.getGestureSource() != this
                    && event.getDragboard().hasString()) {
                setOpacity(1);
            }
        }

        private void setDragEnteredOpacity(DragEvent event) {
            if (event.getGestureSource() != this
                    && event.getDragboard().hasString()) {
                setOpacity(0.3);
            }
        }

        private void acceptMoveTransferMode(DragEvent event) {
            if (getItem() == null || getItem().getName().equals(Group.DEFAULT_NAME)) {
                return;
            }

            if (event.getGestureSource() != this
                    && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();
        }

        private void initDragContent(MouseEvent event) {
            if (getItem() == null || getItem().getName().equals(Group.DEFAULT_NAME)) {
                return;
            }

            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            SnapshotParameters parameters = new SnapshotParameters();
            Image snapshot = getGraphic().snapshot(parameters, null);

            content.putString(String.valueOf(getItem().getId()));
            dragboard.setDragView(snapshot);
            dragboard.setContent(content);

            event.consume();
        }

        private void consumeDragDrop(DragEvent event) {
            if (getItem() == null || getItem().getName().equals(Group.DEFAULT_NAME)) {
                return;
            }

            Dragboard dragboard = event.getDragboard();
            boolean success = false;

            if (dragboard.hasString()) {
                int newIdx = groups.indexOf(getItem());

                groups.stream()
                        .filter(group -> {
                            String otherIdString = dragboard.getString();
                            int otherId = Integer.parseInt(otherIdString);
                            return group.getId() == otherId;
                        })
                        .findFirst()
                        .ifPresent(group -> {
                            GROUPS_REPO.updateWithPosition(newIdx, group);
                        });
            }

            event.setDropCompleted(success);
            event.consume();
        }

        private HBox getHBox(Group group) {
            URL location = getClass().getResource("/fxml/GroupListCell.fxml");
            FXMLLoader loader = new FXMLLoader(location);
            HBox hBox = null;

            try {
                if (!group.getName().equals(Group.DEFAULT_NAME)) {
                    hBox = loader.load();
                    GroupListCellController controller = loader.getController();
                    controller.setGroup(group);
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            if (hBox == null) {
                hBox = new HBox();
                hBox.setPrefSize(485, 20);
                hBox.getStyleClass().add("-fx-background-color: transparent");
            }

            return hBox;
        }

    }
}
