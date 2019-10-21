/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.AbstractProfileListDetails;
import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.api.ProfilesRepository;
import com.github.idelstak.manyfacesfx.model.Group;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.MenuNode;
import com.jfoenix.controls.JFXTabPane;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class HomeDetailsController {

    private static final Logger LOG = Logger.getLogger(HomeDetailsController.class.getName());
    private static final GlobalContext CONTEXT = GlobalContext.getDefault();
    private static final ProfilesRepository PROFILES_REPO = ProfilesRepository.getDefault();
    @FXML
    private JFXTabPane detailsTabPane;
    @FXML
    private Tab profileListTab;
    @FXML
    private Tab groupsTab;
    private MenuNode homeNode;
    private final Lookup.Result<MenuNode> menuNodeResult;
    private final Lookup.Result<Group> groupResult;
    private ChangeListener<Tab> tabChangeListener;

    {
        menuNodeResult = CONTEXT.lookupResult(MenuNode.class);
        groupResult = CONTEXT.lookupResult(Group.class);
    }

    /**
     Initializes the controller class.

     @throws java.io.IOException
     */
    @FXML
    public void initialize() throws IOException {
        detailsTabPane.getSelectionModel()
                .selectedItemProperty()
                .addListener((ob, ov, nv) -> {
                    if (homeNode != null) {
                        homeNode.setDisplayName(from(nv));
                    }
                });

        menuNodeResult.addLookupListener(e -> {
            Iterator<? extends MenuNode> it = menuNodeResult.allInstances().iterator();

            if (it.hasNext()) {
                MenuNode node = it.next();
                if (node.getName().equals("home")) {
                    homeNode = node;
                    Tab selectedTab = detailsTabPane.getSelectionModel().getSelectedItem();
                    homeNode.setDisplayName(from(selectedTab));
                }
            }
        });

        groupResult.addLookupListener(e -> {
            showProfilesByGroup();

            String profilesByGroupTitle = "Profile list by group";

            Platform.runLater(() -> {
                homeNode.setDisplayName(profilesByGroupTitle);
            });
        });

        profileListTab.setContent(AbstractProfileListDetails.getDefault().getPane());
        groupsTab.setContent(getGroupsTab());

        
        tabChangeListener = (ob, ov, nv) -> {
            if (nv.equals(groupsTab)) {
                detailsTabPane.getSelectionModel().selectFirst();
            }
        };

        PROFILES_REPO.addListener((SetChangeListener.Change<? extends Profile> change) -> {
            updateProfileListContent();
        });
        
        updateProfileListContent();
    }

    private void updateProfileListContent() {
        if (PROFILES_REPO.findAll().isEmpty()) {
            profileListTab.setContent(getNoProfilePane());
            detailsTabPane.getSelectionModel()
                    .selectedItemProperty()
                    .addListener(tabChangeListener);
        } else {
            profileListTab.setContent(AbstractProfileListDetails.getDefault().getPane());
            detailsTabPane.getSelectionModel()
                    .selectedItemProperty()
                    .removeListener(tabChangeListener);
        }
    }

    private Pane getNoProfilePane() {
        URL location = getClass().getResource("/fxml/NoProfileAvailable.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Pane pane = null;

        try {
            pane = fxmlLoader.load();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        return pane;
    }

    private String from(Tab tab) {
        return tab.equals(profileListTab)
               ? "Browser profile list"
               : groupResult.allInstances().size() > 0
                 ? "Profile list by group"
                 : tab.getText();
    }

    private Node getGroupsTab() throws IOException {
        return FXMLLoader.load(getClass().getResource("/fxml/GroupListDetails.fxml"));
    }

    private Optional<Node> getProfilesListByGroupContent(Group group) {
        URL location = getClass().getResource("/fxml/ProfileListDetails.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Optional<Node> content = Optional.empty();

        try {
            content = Optional.ofNullable(loader.load());
            ProfileListDetailsController controller = loader.getController();
            controller.setGroup(group);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        return content;
    }

    private void showProfilesByGroup() {
        Iterator<? extends Group> it = groupResult.allInstances().iterator();

        if (it.hasNext()) {
            Group nextGroup = it.next();

            Platform.runLater(() -> {
                detailsTabPane.getSelectionModel().select(groupsTab);
                getProfilesListByGroupContent(nextGroup).ifPresent(groupsTab::setContent);
            });
        }
    }
}
