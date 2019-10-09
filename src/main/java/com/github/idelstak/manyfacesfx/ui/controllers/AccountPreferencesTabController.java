/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.ui.MenuNode;
import com.jfoenix.controls.JFXTabPane;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class AccountPreferencesTabController {

    private static final Logger LOG = Logger.getLogger(AccountPreferencesTabController.class.getName());
    @FXML
    private JFXTabPane tabPane;
    @FXML
    private Tab accountTab;
    @FXML
    private Tab preferencesTab;
    private MenuNode myAccountNode;
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
        URL location = getClass().getResource("/fxml/MyAccount.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Node accountPane = loader.load();

        LOG.log(Level.FINE, "Loaded my acc tab: {0}", accountPane);

        accountTab.setContent(accountPane);

        location = getClass().getResource("/fxml/Preferences.fxml");
        loader = new FXMLLoader(location);
        Node preferencesPane = loader.load();

        LOG.log(Level.FINE, "Loaded preferences tab: {0}", preferencesPane);

        preferencesTab.setContent(preferencesPane);

        tabPane.getSelectionModel()
                .selectedItemProperty()
                .addListener((ob, ov, nv) -> {
                    if (myAccountNode != null) {
                        myAccountNode.setDisplayName(from(nv));
                    }
                });
        
        lookupResult.addLookupListener(e -> {
            Iterator<? extends MenuNode> it = lookupResult.allInstances().iterator();

            if (it.hasNext()) {
                MenuNode node = it.next();
                if (node.getName().equals("my account")) {
                    myAccountNode = node;
                    Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
                    myAccountNode.setDisplayName(from(selectedTab));
                }
            }
        });
    }

    private String from(Tab tab) {
        return tab.equals(accountTab)
               ? "My account"
               : "Preferences";
    }
}
