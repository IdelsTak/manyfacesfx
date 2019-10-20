/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.api.GroupsRepository;
import com.github.idelstak.manyfacesfx.api.ProfilesRepository;
import com.github.idelstak.manyfacesfx.model.Group;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.ProfileNode;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import java.util.Collection;
import java.util.Objects;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class RemoveProfileFromGroupDialogController {

    private static final GlobalContext CONTEXT = GlobalContext.getDefault();
    private static final ProfilesRepository PROFILES_REPO = ProfilesRepository.getDefault();
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
        JFXDialog nonNullDialog = Objects.requireNonNull(dialog, message);

        closeButton.setOnAction(e -> nonNullDialog.close());
        noButton.setOnAction(e -> nonNullDialog.close());
        yesButton.setOnAction(e -> {
            profileNodeResult.allInstances()
                    .stream()
                    .forEach(this::removeProfileFromGroup);

            Platform.runLater(nonNullDialog::close);
        });
    }

    private void removeProfileFromGroup(ProfileNode node) {
        Profile profile = node.getLookup().lookup(Profile.class);
        if (profile != null) {
            Group group = findDefaultGroup();
            profile.setGroup(group);
            PROFILES_REPO.update(profile);

            CONTEXT.remove(node);
        }
    }

    private Group findDefaultGroup() {
        return GROUPS_REPO.findByName(Group.DEFAULT_NAME)
                .orElseGet(() -> GROUPS_REPO.add(Group.DEFAULT_NAME));
    }

    private static final GroupsRepository GROUPS_REPO = GroupsRepository.getDefault();

    private String resolveMessageFrom(Collection<? extends ProfileNode> nodes) {
        String message;

        if (nodes.size() == 1) {
            ProfileNode node = nodes.iterator().next();
            Profile profile = node.getLookup().lookup(Profile.class);
            message = String.format("Do you really want to remove \"%s\" from a group?",
                    profile.getName());
        } else {
            message = String.format(
                    "Do you really want to remove %d browser profiles from a group?",
                    nodes.size());
        }

        return message;
    }

}
