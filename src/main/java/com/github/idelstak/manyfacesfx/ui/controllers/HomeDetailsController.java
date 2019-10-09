/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.AbstractProfileListDetails;
import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.ui.MenuNode;
import com.jfoenix.controls.JFXTabPane;
import java.io.IOException;
import java.util.Iterator;
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

    @FXML
    private JFXTabPane detailsTabPane;
    @FXML
    private Tab profileListTab;
    @FXML
    private Tab groupsTab;
    private Node groupsContent;
    private MenuNode homeNode;
    private final Lookup.Result<MenuNode> lookupResult;

    {
        lookupResult = GlobalContext.getDefault().lookupResult(MenuNode.class);
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

        lookupResult.addLookupListener(e -> {
            Iterator<? extends MenuNode> it = lookupResult.allInstances().iterator();

            if (it.hasNext()) {
                MenuNode node = it.next();
                if (node.getName().equals("home")) {
                    homeNode = node;
                    Tab selectedTab = detailsTabPane.getSelectionModel().getSelectedItem();
                    homeNode.setDisplayName(from(selectedTab));
                }
            }
        });

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
}
