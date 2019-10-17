/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.api.GroupsRepository;
import com.github.idelstak.manyfacesfx.model.Group;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.ProfileNode;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class MoveProfilesDialogController {

    private static final Logger LOG = Logger.getLogger(MoveProfilesDialogController.class.getName());
    private static final GlobalContext CONTEXT = GlobalContext.getDefault();
    private static final GroupsRepository GROUPS_REPO = GroupsRepository.getDefault();
    @FXML
    private JFXButton closeButton;
    @FXML
    private Label profile1Label;
    @FXML
    private Label profile2Label;
    @FXML
    private Label extraProfilesLabel;
    @FXML
    private VBox groupsBox;
    @FXML
    private StackPane listActionsPane;
    @FXML
    private JFXButton addNewgroupButton;
    @FXML
    private JFXTextField groupNameField;
    @FXML
    private JFXButton applyGroupNameButton;
    @FXML
    private JFXButton moveButton;
    @FXML
    private JFXButton cancelButton;
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private final ObservableSet<CheckBox> selectGroupCheckBoxes;
    private final Lookup.Result<ProfileNode> profileNodeResult;

    {
        selectGroupCheckBoxes = FXCollections.observableSet(new HashSet<>());
        profileNodeResult = CONTEXT.lookupResult(ProfileNode.class);
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        profileNodeResult.addLookupListener(e -> updateSelectedProfileLabels());
    }

    void setDialog(JFXDialog dialog) {
        if (dialog == null) {
            throw new IllegalArgumentException("Dialog should not be null");
        }

        closeButton.setOnAction(e -> dialog.close());
        cancelButton.setOnAction(e -> dialog.close());

        updateSelectedProfileLabels();
        refreshGroupsList();
    }

    private void refreshGroupsList() {
        List<Optional<Pane>> panesOptionals = GROUPS_REPO.findAll()
                .stream()
                .map(this::getGroupCell)
                .collect(Collectors.toList());

        Platform.runLater(() -> panesOptionals.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(groupsBox.getChildren()::add));
    }

    private Optional<Pane> getGroupCell(Group group) {
        URL location = getClass().getResource("/fxml/MoveProfileCell.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Optional<Pane> paneOptional = Optional.empty();

        try {
            paneOptional = Optional.ofNullable(loader.load());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        paneOptional.ifPresent(pane -> {
            MoveProfileCellController controller = loader.getController();

            controller.setGroup(group);
            selectGroupCheckBoxes.add(controller.getSelectGroupCheckBox());
        });

        return paneOptional;
    }

    private void updateSelectedProfileLabels() {
        Profile[] profiles = profileNodeResult.allInstances()
                .stream()
                .map(ProfileNode::getLookup)
                .map(this::getProfile)
                .toArray(Profile[]::new);

        Platform.runLater(() -> {
            int numberOfExtraProfiles = 0;

            for (int i = 0; i < profiles.length; i++) {
                Profile profile = profiles[i];

                switch (i) {
                    case 0:
                        profile1Label.setText(profile.getName());
                        break;
                    case 1:
                        profile2Label.setVisible(true);
                        profile2Label.setText(profile.getName());
                        break;
                    default:
                        numberOfExtraProfiles += 1;
                        break;
                }
            }

            if (numberOfExtraProfiles > 0) {
                extraProfilesLabel.setVisible(true);
                extraProfilesLabel.setText(String.format("...and %d more", numberOfExtraProfiles));
            }
        });

    }

    private Profile getProfile(Lookup lookup) {
        return lookup.lookup(Profile.class);
    }
}
