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
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
    private final Map<CheckBox, Group> groupsMap = new HashMap<>();

    {
        selectGroupCheckBoxes = FXCollections.observableSet(new HashSet<>());
        profileNodeResult = CONTEXT.lookupResult(ProfileNode.class);
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

        GROUPS_REPO.addListener((ListChangeListener.Change<? extends Group> change) -> {
            refreshGroupsList();
        });

        profileNodeResult.addLookupListener(e -> {
            updateSelectedProfileLabels();
        });
    }

    void setDialog(JFXDialog dialog) {
        if (dialog == null) {
            throw new IllegalArgumentException("Dialog should not be null");
        }

        dialog.setOnDialogClosed(e -> profileNodeResult.allInstances()
                .stream()
                .forEach(CONTEXT::remove));

        closeButton.setOnAction(e -> dialog.close());
        cancelButton.setOnAction(e -> dialog.close());
        moveButton.setOnAction(e -> {
            moveProfilesToSomeSelectedGroup();
            Platform.runLater(dialog::close);
        });

        updateSelectedProfileLabels();
        refreshGroupsList();
    }

    private void refreshGroupsList() {
        List<Optional<Pane>> panesOptionals = GROUPS_REPO.findAll()
                .stream()
                .map(this::getGroupCell)
                .collect(Collectors.toList());

        groupsBox.getChildren().clear();

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
            JFXCheckBox selectGroupCheckBox = controller.getSelectGroupCheckBox();

            selectGroupCheckBoxes.add(selectGroupCheckBox);
            selectGroupCheckBox.selectedProperty()
                    .addListener((ob, deselected, selected) -> {
                        if (selected) {
                            selectGroupCheckBoxes.stream()
                                    .filter(checkBox -> !Objects.equals(checkBox, selectGroupCheckBox))
                                    .forEach(checkBox -> checkBox.setSelected(deselected));
                        }
                    });

            groupsMap.put(selectGroupCheckBox, group);
        });

        return paneOptional;
    }

    private void updateSelectedProfileLabels() {
        Profile[] profiles = profileNodeResult.allInstances()
                .stream()
                .map(ProfileNode::getLookup)
                .map(this::getProfile)
                .filter(Objects::nonNull)
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

    private void moveProfilesToSomeSelectedGroup() {
        groupsMap.keySet()
                .stream()
                .filter(CheckBox::isSelected)
                .findFirst()
                .map(groupsMap::get)
                .ifPresent(this::moveProfilesToGroup);
    }

    private void moveProfilesToGroup(Group group) {
        profileNodeResult.allInstances()
                .stream()
                .map(ProfileNode::getLookup)
                .map(this::getProfile)
                .filter(Objects::nonNull)
                .forEach(profile -> {
                    profile.setGroup(group);
                    ProfilesRepository.getDefault().update(profile);
                });

//        profileNodeResult.allInstances()
//                .stream()
//                .forEach(node -> CONTEXT.remove(node));
    }
}