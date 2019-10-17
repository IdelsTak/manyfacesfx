/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.AbstractProfileListDetails;
import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.model.Group;
import com.github.idelstak.manyfacesfx.ui.MenuNode;
import com.jfoenix.controls.JFXTabPane;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class HomeDetailsController {

    private static final Logger LOG = Logger.getLogger(HomeDetailsController.class.getName());
    private static final GlobalContext CONTEXT = GlobalContext.getDefault();
    @FXML
    private JFXTabPane detailsTabPane;
    @FXML
    private Tab profileListTab;
    @FXML
    private Tab groupsTab;
    private Node groupsContent;
    private MenuNode homeNode;
    private final Lookup.Result<MenuNode> menuNodeResult;
    private final Lookup.Result<Group> groupResult;

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

        groupResult.addLookupListener(e -> showProfilesByGroup());

        profileListTab.setContent(AbstractProfileListDetails.getDefault().getPane());
        groupsTab.setContent(getGroupsTab());
    }

    private String from(Tab tab) {
        return tab.equals(profileListTab)
               ? "Browser profile list"
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
            Platform.runLater(() -> {
                detailsTabPane.getSelectionModel().select(groupsTab);
                getProfilesListByGroupContent(it.next()).ifPresent(groupsTab::setContent);
            });
        }
    }
}
