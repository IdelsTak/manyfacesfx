/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.ui.AppMenu;
import com.github.idelstak.manyfacesfx.ui.HomeNodeContext;
import com.github.idelstak.manyfacesfx.ui.MenuNode;
import com.github.idelstak.manyfacesfx.ui.OverViewNodeContext;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class HomeMenuController {

    private static final GlobalContext CONTEXT = GlobalContext.getDefault();
    private static final Logger LOG = Logger.getLogger(HomeMenuController.class.getName());
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
    }

    private void ensureHomeSelected() {
        Platform.runLater(() -> {
            homeToggle.fireEvent(new ActionEvent());
            homeToggle.setSelected(true);
        });
    }

}
