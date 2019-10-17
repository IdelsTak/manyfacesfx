/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.api.ProfilesRepository;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.ProfileNode;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import java.util.Collection;
import java.util.Objects;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class DeleteProfileDialogController {

    private static final GlobalContext CONTEXT = GlobalContext.getDefault();
    private static final ProfilesRepository PROFILES_REPO = ProfilesRepository.getDefault();
    @FXML
    private Label headerLabel;
    @FXML
    private JFXButton closeButton;
    @FXML
    private Text contentText;
    @FXML
    private JFXButton yesButton;
    @FXML
    private JFXButton noButton;
    private final Lookup.Result<ProfileNode> profileNodeResult;

    {
        profileNodeResult = CONTEXT.lookupResult(ProfileNode.class);
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        contentText.setText(resolveMessageFrom(profileNodeResult.allInstances()));

        profileNodeResult.addLookupListener(e -> {
            Collection<? extends ProfileNode> profiles = profileNodeResult.allInstances();

            contentText.setText(resolveMessageFrom(profiles));
        });
    }

    void setDialog(JFXDialog dialog) {
        String message = "Dialog should not be null";
        JFXDialog dlg = Objects.requireNonNull(dialog, message);

        closeButton.setOnAction(e -> dlg.close());
        noButton.setOnAction(e -> dlg.close());
        yesButton.setOnAction(e -> {
            profileNodeResult.allInstances()
                    .stream()
                    .forEach(this::deleteProfile);
            
            Platform.runLater(dlg::close);
        });
    }

    private void deleteProfile(ProfileNode node) {
        Profile profile = node.getLookup().lookup(Profile.class);
        if (profile != null) {
            PROFILES_REPO.delete(profile);
            CONTEXT.remove(node);
        }
    }

    private String resolveMessageFrom(Collection<? extends ProfileNode> nodes) {
        String message;

        if (nodes.size() == 1) {
            ProfileNode node = nodes.iterator().next();
            Profile profile = node.getLookup().lookup(Profile.class);
            message = String.format("Do you really want to delete \"%s\" browser profile?",
                    profile.getName());
        } else {
            message = String.format(
                    "Do you really want to delete %d browser profiles?",
                    nodes.size());
        }

        return message;
    }
}
