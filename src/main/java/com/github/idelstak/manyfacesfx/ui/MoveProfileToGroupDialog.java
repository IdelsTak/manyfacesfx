/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui;

import com.github.idelstak.manyfacesfx.api.ProfilesRepository;
import com.github.idelstak.manyfacesfx.api.Stackable;
import com.github.idelstak.manyfacesfx.model.Group;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.controllers.MoveProfilesDialogController;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class MoveProfileToGroupDialog {

    private static final Logger LOG;
    private static final URL URL;
    private static final ProfilesRepository PROFILES_REPO;
    private static final StackPane DIALOG_CONTAINER;
    private final FXMLLoader fxmlLoader;
    private final JFXDialog dialog;
    private Region content;
    private MoveProfilesDialogController controller;
    private Label firstProfileToMoveLabel;
    private Label secondProfileToMoveLabel;
    private Label otherProfilesToMoveLabel;
    private Button moveButton;
    private Button cancelButton;
    private Button closeButton;
    private boolean moved;

    static {
        LOG = Logger.getLogger(DeleteProfileDialog.class.getName());
        URL = MoveProfileToGroupDialog.class.getResource("/fxml/MoveProfilesDialog.fxml");
        PROFILES_REPO = ProfilesRepository.getDefault();
        DIALOG_CONTAINER = Stackable.getDefault().getStackPane();
    }

    public MoveProfileToGroupDialog() {
        dialog = new JFXDialog();
        fxmlLoader = new FXMLLoader(URL);

        try {
            content = fxmlLoader.load();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        if (content != null) {
            dialog.setContent(content);
            dialog.setDialogContainer(DIALOG_CONTAINER);

            controller = fxmlLoader.getController();
            moveButton = controller.getMoveButton();
            cancelButton = controller.getCancelButton();
            closeButton = controller.getCloseButton();
            firstProfileToMoveLabel = controller.getFirstProfileToMoveLabel();
            secondProfileToMoveLabel = controller.getSecondProfileToMoveLabel();
            otherProfilesToMoveLabel = controller.getOtherProfilesToMoveLabel();

            cancelButton.setOnAction(e -> closeDialogOnFinish());
            closeButton.setOnAction(e -> closeDialogOnFinish());
        }
    }

    public void setOnDialogClosed(EventHandler<? super JFXDialogEvent> handler) {
        dialog.setOnDialogClosed(handler);
    }

    public void show() {
        dialog.show();
    }

    public boolean profileMoved() {
        return moved;
    }

    public void setProfile(Profile profile) {
        checkForNullArgument("Profile", profile);

        updateLabelsWithNamesOf(new Profile[]{profile});

        moveButton.setOnAction(e -> {
            Optional<Group> selectedGroup = controller.getSelectedGroup();

            selectedGroup.ifPresent(group -> {
                profile.setGroup(group);
                moved = PROFILES_REPO.update(profile);
            });

            closeDialogOnFinish();
        });
    }

    public void setProfiles(Profile... profiles) {
        checkForNullArgument("Profiles", profiles);

        if (profiles.length == 1) {
            setProfile(profiles[0]);
        } else {
            checkForNullsInArgument("Profile", Arrays.asList(profiles));

            updateLabelsWithNamesOf(profiles);

            moveButton.setOnAction(e -> {
                Optional<Group> selectedGroup = controller.getSelectedGroup();
                int numberOfMovedProfiles = 0;

                if (selectedGroup.isPresent()) {
                    Group newParentGroup = selectedGroup.get();
                    numberOfMovedProfiles = Long.valueOf(Arrays.stream(profiles)
                            .map(profile -> {
                                profile.setGroup(newParentGroup);
                                return PROFILES_REPO.update(profile);
                            })
                            .filter(Boolean.TRUE::equals)
                            .count())
                            .intValue();
                }
                //Operation is considered a success if 
                //all the received profiles were deleted
                moved = (numberOfMovedProfiles == profiles.length);

                closeDialogOnFinish();
            });
        }
    }

    private void updateLabelsWithNamesOf(Profile... profiles) {
        Platform.runLater(() -> {
            int numberOfExtraProfiles = 0;

            for (int i = 0; i < profiles.length; i++) {
                Profile profile = profiles[i];

                switch (i) {
                    case 0:
                        firstProfileToMoveLabel.setText(profile.getName());
                        break;
                    case 1:
                        secondProfileToMoveLabel.setVisible(true);
                        secondProfileToMoveLabel.setText(profile.getName());
                        break;
                    default:
                        numberOfExtraProfiles += 1;
                        break;
                }
            }

            if (numberOfExtraProfiles > 0) {
                otherProfilesToMoveLabel.setVisible(true);
                otherProfilesToMoveLabel.setText(String.format("...and %d more", numberOfExtraProfiles));
            }
        });
    }

    private void closeDialogOnFinish() {
        Platform.runLater(dialog::close);
    }

    private <T> void checkForNullsInArgument(String argumentName, Collection<T> arguments) throws IllegalArgumentException {
        boolean containsIllegalArgument = false;

        for (T argument : arguments) {
            if (argument == null) {
                containsIllegalArgument = true;
                break;
            }
        }

        if (containsIllegalArgument) {
            throw new IllegalArgumentException("No " + argumentName + " should be null");
        }
    }

    private <T> void checkForNullArgument(String argumentName, T argument) throws IllegalArgumentException {
        if (argument == null) {
            throw new IllegalArgumentException(argumentName + " should not be null");
        }
    }
}
