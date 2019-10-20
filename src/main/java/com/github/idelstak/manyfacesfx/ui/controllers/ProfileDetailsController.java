/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.api.Stackable;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.ProfileNode;
import com.github.idelstak.manyfacesfx.ui.BulkProfilesSelect;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileDetailsController {

    private static final Logger LOG = Logger.getLogger(ProfileDetailsController.class.getName());
    private static final GlobalContext CONTEXT = GlobalContext.getDefault();
    @FXML
    private TitledPane titlePane;
    @FXML
    private HBox titleBox;
    @FXML
    private HBox selectCheckBoxPane;
    @FXML
    private JFXCheckBox selectCheckBox;
    @FXML
    private Label nameLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label membersLabel;
    @FXML
    private Label lastEditedLabel;
    @FXML
    private HBox actionBox;
    @FXML
    private JFXButton actionButton;
    @FXML
    private HBox menuBox;
    @FXML
    private JFXButton menuButton;
    @FXML
    private Label idLabel;
    @FXML
    private JFXTextArea notesTextArea;
    private final MenuItem editProfileItem;
    private final MenuItem moveProfileToAGroupItem;
    private final MenuItem deleteProfileItem;
    private final ContextMenu profileMenu;
    private BulkProfilesSelect selectProfiles;
    private Profile profile;
    private final Lookup.Result<ProfileNode> profileNodeResult;
    private final ChangeListener<Boolean> selectionListener = new SelectionListener();

    {
        profileNodeResult = CONTEXT.lookupResult(ProfileNode.class);

        deleteProfileItem = new MenuItem("Delete");
        moveProfileToAGroupItem = new MenuItem("Move to a group");
        editProfileItem = new MenuItem("Edit");

        profileMenu = new ContextMenu(editProfileItem, moveProfileToAGroupItem, deleteProfileItem);
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        titleBox.minWidthProperty().bind(titlePane.widthProperty());

        profileNodeResult.addLookupListener(e -> {
            Optional<Profile> optionalProfile = profileNodeResult.allInstances()
                    .stream()
                    .map(node -> node.getLookup())
                    .map(lookup -> lookup.lookup(Profile.class))
                    .filter(aProfile -> Objects.equals(aProfile, profile))
                    .findFirst();

            Platform.runLater(() -> {
                selectCheckBox.setSelected(optionalProfile.isPresent());
            });
        });
    }

    public void setProfileNode(ProfileNode profileNode) {
        String message = "Profile node should not be null";
        ProfileNode node = Objects.requireNonNull(profileNode, message);

        message = "Profile should not be null";
        profile = Objects.requireNonNull(node.getLookup().lookup(Profile.class), message);

        titlePane.idProperty()
                .bind(Bindings.createStringBinding(
                        () -> profile.getName() + ":" + profile.getLastEdited(),
                        new Observable[]{
                            profile.nameProperty(),
                            profile.lastEditedProperty()}));

        nameLabel.textProperty().bind(profile.nameProperty());
        idLabel.textProperty().bind(profile.idProperty());
        notesTextArea.textProperty().bindBidirectional(profile.notesProperty());

        StringBinding lastEditedDatePropertyAsFormattedDate = Bindings.createStringBinding(
                () -> profile.getLastEdited()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                profile.lastEditedProperty());
        lastEditedLabel.textProperty().bind(lastEditedDatePropertyAsFormattedDate);

        message = "Select profiles should not be null";
        selectProfiles = Objects.requireNonNull(node.getLookup().lookup(BulkProfilesSelect.class), message);

        updateSelection();
        selectProfiles.selectProperty().addListener(selectionListener);

        selectCheckBox.setOnAction(e -> {
            if (selectCheckBox.isSelected()) {
                CONTEXT.add(node);
            } else {
                CONTEXT.remove(node);
            }
        });

        menuButton.setOnAction(e -> showProfileContextMenu());
        moveProfileToAGroupItem.setOnAction(e -> showMoveProfileDialog(node));
        deleteProfileItem.setOnAction(e -> showDeleteProfileDialog(node));
    }

    private void updateSelection() {
        Callable<Double> widthCalculator = () -> selectProfiles.isVisible()
                                                 ? 25.0
                                                 : 0.0;
        DoubleBinding widthBinding = Bindings.createDoubleBinding(
                widthCalculator,
                selectProfiles.visibleProperty());

        selectCheckBoxPane.prefWidthProperty().bind(widthBinding);
//        selectProfiles.selectProperty().addListener((ob, ov, nv) -> {
//            Platform.runLater(() -> selectCheckBox.setSelected(false));
//
//            if (profile != null) {
//                ProfilesRepository.getDefault()
//                        .findById(profile.getId())
//                        .ifPresent(p -> Platform.runLater(() -> selectCheckBox.setSelected(nv)));
//            }
//        });
//        selectProfiles.selectProperty().addListener(selectionListener);
    }

    private void showProfileContextMenu() {
        double xOffset = menuButton.getWidth();
        double yOffset = 0;

        profileMenu.show(menuButton, Side.LEFT, xOffset, yOffset);
    }

    private void showDeleteProfileDialog(ProfileNode node) {
        URL location = getClass().getResource("/fxml/DeleteProfileDialog.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Pane pane = null;
        DeleteProfileDialogController controller = null;

        try {
            pane = loader.load();
            controller = loader.getController();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        if (pane != null && controller != null) {
            JFXDialog dialog = new JFXDialog();
            dialog.setContent(pane);
            controller.setDialog(dialog);

            CONTEXT.set(ProfileNode.class, node);
            Platform.runLater(() -> dialog.show(Stackable.getDefault().getStackPane()));
        }
    }

    private void showMoveProfileDialog(ProfileNode node) {
        URL location = getClass().getResource("/fxml/MoveProfilesDialog.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Pane pane = null;
        MoveProfilesDialogController controller = null;

        try {
            pane = loader.load();
            controller = loader.getController();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        if (pane != null && controller != null) {
            JFXDialog dialog = new JFXDialog();
            dialog.setContent(pane);
            controller.setDialog(dialog);

            CONTEXT.set(ProfileNode.class, node);
            Platform.runLater(() -> dialog.show(Stackable.getDefault().getStackPane()));
        }
    }

    private class SelectionListener implements ChangeListener<Boolean> {

        private SelectionListener() {
        }

        @Override
        public void changed(
                ObservableValue<? extends Boolean> observableValue,
                Boolean oldValue,
                Boolean newValue) {
            Platform.runLater(() -> {
                selectCheckBox.setSelected(newValue);
                selectCheckBox.fireEvent(new ActionEvent());
            });
        }
    }
}
