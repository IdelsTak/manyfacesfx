/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.api.Stackable;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.BulkProfilesSelect;
import com.github.idelstak.manyfacesfx.ui.DeleteProfileDialog;
import com.github.idelstak.manyfacesfx.ui.ProfileNode;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
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
    private BulkProfilesSelect bulkProfilesSelect;
    private Profile profile;
    private final Lookup.Result<ProfileNode> profileNodeResult;

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
                    .map(ProfileNode::getLookup)
                    .map(lookup -> lookup.lookup(Profile.class))
                    .filter(Objects::nonNull)
                    .filter(profile::equals)
                    .findFirst();

            Platform.runLater(() -> {
                selectCheckBox.setSelected(optionalProfile.isPresent());
//                selectCheckBox.fireEvent(new ActionEvent());
            });
        });
    }

    public void setProfileNode(ProfileNode profileNode) {
        ProfileNode node = Objects.requireNonNull(
                profileNode,
                "Profile node should not be null");
        Lookup nodeLookup = node.getLookup();

        profile = Objects.requireNonNull(
                nodeLookup.lookup(Profile.class),
                "Profile should not be null");
        bulkProfilesSelect = Objects.requireNonNull(
                nodeLookup.lookup(BulkProfilesSelect.class),
                "Bulk profiles select should not be null");

        initTitledPaneIdBinding();
        initSelectCheckBoxVisibileBinding();
        initProfileAttributesBinding();

        bulkProfilesSelect.selectProperty()
                .addListener((ob, ov, nv) -> {
                    if (nodeLookup.lookup(Profile.class) != null) {
                        Platform.runLater(() -> {
                            selectCheckBox.setSelected(nv);
                            selectCheckBox.fireEvent(new ActionEvent());
                        });
                    }
                });

        selectCheckBox.setOnAction(e -> {
            if (selectCheckBox.isSelected()) {
                CONTEXT.add(node);
            } else {
                CONTEXT.remove(node);
            }
        });

        menuButton.setOnAction(e -> showProfileContextMenu());
        moveProfileToAGroupItem.setOnAction(e -> showMoveProfileDialog(node));
        deleteProfileItem.setOnAction(e -> {
            boolean deleteSuccessful = new DeleteProfileDialog().delete(profile);
            if (deleteSuccessful) {
                CONTEXT.remove(node);
            }
        });
    }

    private void initTitledPaneIdBinding() {
        titlePane.idProperty()
                .bind(Bindings.createStringBinding(
                        () -> profile.getName() + ":" + profile.getLastEdited(),
                        new Observable[]{
                            profile.nameProperty(),
                            profile.lastEditedProperty()}));
    }

    private void initProfileAttributesBinding() {
        nameLabel.textProperty().bind(profile.nameProperty());
        idLabel.textProperty().bind(profile.idProperty());
        notesTextArea.textProperty().bindBidirectional(profile.notesProperty());

        StringBinding lastEditedDatePropertyAsFormattedDate = Bindings.createStringBinding(
                () -> profile.getLastEdited()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                profile.lastEditedProperty());
        lastEditedLabel.textProperty().bind(lastEditedDatePropertyAsFormattedDate);
    }

    private void initSelectCheckBoxVisibileBinding() {
        selectCheckBoxPane.prefWidthProperty().bind(Bindings.createDoubleBinding(
                () -> bulkProfilesSelect.isVisible()
                      ? 25.0
                      : 0.0,
                bulkProfilesSelect.visibleProperty()));
    }

    private void showProfileContextMenu() {
        double xOffset = menuButton.getWidth();
        double yOffset = 0;

        profileMenu.show(menuButton, Side.LEFT, xOffset, yOffset);
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
}
