/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui;

import com.github.idelstak.manyfacesfx.api.ProfilesRepository;
import com.github.idelstak.manyfacesfx.api.Stackable;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.controllers.DeleteProfileDialogController;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class DeleteProfileDialog {

    private static final Logger LOG;
    private static final URL URL;
    private static final ProfilesRepository PROFILES_REPO;
    private static final StackPane DIALOG_CONTAINER;
    private final FXMLLoader fxmlLoader;
    private Button closeButton, yesButton, noButton;
    private Text messageText;
    private final JFXDialog dialog;
    private Region content;
    private boolean deleted;

    static {
        LOG = Logger.getLogger(DeleteProfileDialog.class.getName());
        URL = DeleteProfileDialog.class.getResource("/fxml/DeleteProfileDialog.fxml");
        PROFILES_REPO = ProfilesRepository.getDefault();
        DIALOG_CONTAINER = Stackable.getDefault().getStackPane();
    }

    public DeleteProfileDialog() {
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

            DeleteProfileDialogController controller = fxmlLoader.getController();
            closeButton = controller.getCloseButton();
            yesButton = controller.getYesButton();
            noButton = controller.getNoButton();
            messageText = controller.getMessageText();

            closeButton.setOnAction(e -> closeDialogOnFinish());
            noButton.setOnAction(e -> closeDialogOnFinish());
        }
    }

    public void setProfile(Profile profile) {
        checkForNullArgument("Profile", profile);

        messageText.setText(messageFor(profile));

        yesButton.setOnAction(e -> {
            deleted = PROFILES_REPO.delete(profile);

            closeDialogOnFinish();
        });
    }

    public void setProfiles(Profile... profiles) {
        checkForNullArgument("Profiles", profiles);

        if (profiles.length == 1) {
            setProfile(profiles[0]);
        } else {
            checkForNullsInArgument("Profile", Arrays.asList(profiles));

            messageText.setText(messageFor(profiles));

            yesButton.setOnAction(e -> {
                int deletedProfiles = Long.valueOf(Arrays.stream(profiles)
                        .map(PROFILES_REPO::delete)
                        .filter(Boolean.TRUE::equals)
                        .count())
                        .intValue();
                //Operation is considered a success if 
                //all the received profiles were deleted
                deleted = (deletedProfiles == profiles.length);

                closeDialogOnFinish();
            });
        }
    }

    public boolean profileDeleted() {
        return deleted;
    }

    public void show() {
        dialog.show();
    }

    public void setOnDialogClosed(EventHandler<? super JFXDialogEvent> handler) {
        dialog.setOnDialogClosed(handler);
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

    private void closeDialogOnFinish() {
        Platform.runLater(dialog::close);
    }

    private String messageFor(Profile profile) {
        return String.format(
                "Do you really want to delete \"%s\" browser profile?",
                profile.getName());
    }

    private String messageFor(Profile... profiles) {
        return String.format(
                "Do you really want to delete %d browser profiles?",
                profiles.length);
    }
}
