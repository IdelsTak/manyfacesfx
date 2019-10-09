/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.ui.MenuNode;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

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

    /**
     Initializes the controller class.

     @throws java.io.IOException
     */
    @FXML
    public void initialize() throws IOException {
        MenuNode homeNode = new MenuNode(
                "home",
                homeToggle.getText(),
                true,
                FXMLLoader.load(getClass().getResource("/fxml/HomeDetails.fxml")));
        MenuNode pluginsNode = new MenuNode(
                "plugins",
                pluginsToggle.getText(),
                true,
                FXMLLoader.load(getClass().getResource("/fxml/Plugins.fxml")));
        MenuNode myAccountNode = new MenuNode(
                "my account",
                myAccountToggle.getText(),
                true,
                FXMLLoader.load(getClass().getResource("/fxml/AccountPreferencesTab.fxml")));
        MenuNode helpNode = new MenuNode(
                "help & support",
                helpToggle.getText(),
                true,
                FXMLLoader.load(getClass().getResource("/fxml/HelpAndSupport.fxml")));

        Set<MenuNode> nodes = new HashSet<>();

        nodes.add(homeNode);
        nodes.add(myAccountNode);
        nodes.add(pluginsNode);
        nodes.add(helpNode);

        homeToggle.setOnAction(e -> nodes.stream().forEach(n -> context(n, homeNode)));
        myAccountToggle.setOnAction(e -> nodes.stream().forEach(n -> context(n, myAccountNode)));
        pluginsToggle.setOnAction(e -> nodes.stream().forEach(n -> context(n, pluginsNode)));
        helpToggle.setOnAction(e -> nodes.stream().forEach(n -> context(n, helpNode)));

        Platform.runLater(() -> {
            homeToggle.fireEvent(new ActionEvent());
            homeToggle.setSelected(true);
        });
    }

    private void context(MenuNode n1, MenuNode n2) {
        if (Objects.equals(n1, n2)) {
            CONTEXT.add(n1);
        } else {
            CONTEXT.remove(n1);
        }
    }

}
