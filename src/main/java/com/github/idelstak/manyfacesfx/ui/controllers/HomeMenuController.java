/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.api.GroupsRepository;
import com.github.idelstak.manyfacesfx.api.ProfilesRepository;
import com.github.idelstak.manyfacesfx.api.Stackable;
import com.github.idelstak.manyfacesfx.model.Group;
import com.github.idelstak.manyfacesfx.ui.AppMenu;
import com.github.idelstak.manyfacesfx.ui.HomeNodeContext;
import com.github.idelstak.manyfacesfx.ui.MenuNode;
import com.github.idelstak.manyfacesfx.ui.OverViewNodeContext;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class HomeMenuController {

    private static final GlobalContext CONTEXT = GlobalContext.getDefault();
    private static final Logger LOG = Logger.getLogger(HomeMenuController.class.getName());
    private static final GroupsRepository GROUPS_REPO = GroupsRepository.getDefault();
    private static final ProfilesRepository PROFILES_REPO = ProfilesRepository.getDefault();
    @FXML
    private RadioButton homeToggle;
    @FXML
    private ToggleGroup homeMenuGroup;
    @FXML
    private RadioButton newProfileToggle;
    @FXML
    private RadioButton myAccountToggle;
    @FXML
    private RadioButton pluginsToggle;
    @FXML
    private RadioButton helpToggle;
    @FXML
    private JFXButton groupSettingsButton;
    @FXML
    private JFXListView<Group> groupsList;
    @FXML
    private Label numberInUseLabel;
    private ObservableList<Group> groups;
    private final Lookup.Result<MenuNode> lookupResult;

    {
        lookupResult = CONTEXT.lookupResult(MenuNode.class);
    }

    /**
     Initializes the controller class.

     @throws java.io.IOException
     */
    @FXML
    public void initialize() throws IOException {
        MenuNode homeNode = new HomeNodeContext().getNode();
        MenuNode pluginsNode = new MenuNode(
                "plugins",
                pluginsToggle.getText(),
                true,
                FXMLLoader.load(getClass().getResource("/fxml/Plugins.fxml")),
                AppMenu.HOME);
        MenuNode myAccountNode = new MenuNode(
                "my account",
                myAccountToggle.getText(),
                true,
                FXMLLoader.load(getClass().getResource("/fxml/AccountPreferencesTab.fxml")),
                AppMenu.HOME);
        MenuNode helpNode = new MenuNode(
                "help & support",
                helpToggle.getText(),
                true,
                FXMLLoader.load(getClass().getResource("/fxml/HelpAndSupport.fxml")),
                AppMenu.HOME);
        MenuNode newProfileNode = new MenuNode(
                "new profile",
                newProfileToggle.getText(),
                false,
                FXMLLoader.load(getClass().getResource("/fxml/ProfileAttributes.fxml")),
                AppMenu.PROFILE);

        homeToggle.setOnAction(e -> CONTEXT.replace(MenuNode.class, homeNode));
        newProfileToggle.setOnAction(e -> {
            CONTEXT.replace(MenuNode.class, newProfileNode);
            //Ensure the overview node context is added to lookup
            Platform.runLater(() -> new OverViewNodeContext().select());
        });
        myAccountToggle.setOnAction(e -> CONTEXT.replace(MenuNode.class, myAccountNode));
        pluginsToggle.setOnAction(e -> CONTEXT.replace(MenuNode.class, pluginsNode));
        helpToggle.setOnAction(e -> CONTEXT.replace(MenuNode.class, helpNode));

        ensureHomeSelected();

        lookupResult.addLookupListener(e -> {
            Iterator<? extends MenuNode> it = lookupResult.allInstances().iterator();

            if (it.hasNext()
                    && it.next()
                            .getName()
                            .equals(newProfileNode.getName())) {
                Platform.runLater(() -> homeToggle.setSelected(true));
            }
        });

        numberInUseLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            int numberOfProfiles = PROFILES_REPO.findAll().size();
            return numberOfProfiles < 1
                   ? "-/"
                   : numberOfProfiles + "/";
        }, PROFILES_REPO.findAll()));

        groups = GROUPS_REPO.findAll();

        GROUPS_REPO.addListener((ListChangeListener.Change<? extends Group> change) -> {
            Platform.runLater(() -> groupsList.getItems().setAll(groups));
        });
        //Ensure that the number of profiles is
        //updated in the display name of the group
        groups.forEach(group -> {
            group.numberOfProfilesProperty()
                    .addListener((ob, ov, nv) -> updateDisplayName(group));
            group.nameProperty()
                    .addListener((ob, ov, nv) -> updateDisplayName(group));
        });

        groupsList.getItems().setAll(groups);

        groupSettingsButton.setOnAction(e -> {

            URL location = getClass().getResource("/fxml/EditGroupsDialog.fxml");
            FXMLLoader loader = new FXMLLoader(location);
            Pane pane = null;
            EditGroupsDialogController controller = null;

            try {
                pane = loader.load();
                controller = loader.getController();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            if (pane != null && controller != null) {
                JFXDialog dialog = new JFXDialog();
                dialog.setContent(pane);
                dialog.setTransitionType(JFXDialog.DialogTransition.BOTTOM);
                controller.setDialog(dialog);
                dialog.show(Stackable.getDefault().getStackPane());
            }

        });
    }

    public void updateDisplayName(Group group) {
        Platform.runLater(() -> {
            int idx = groupsList.getItems().indexOf(group);
            groupsList.getItems().set(idx, group);
        });
    }

    private void ensureHomeSelected() {
        Platform.runLater(() -> {
            homeToggle.fireEvent(new ActionEvent());
            homeToggle.setSelected(true);
        });
    }

}
