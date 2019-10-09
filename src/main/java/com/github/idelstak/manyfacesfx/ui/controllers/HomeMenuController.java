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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class HomeMenuController {

    private static final GlobalContext CONTEXT = GlobalContext.getDefault();

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
                false,
                new VBox());
        MenuNode helpNode = new MenuNode(
                "help & support",
                helpToggle.getText(),
                true,
                FXMLLoader.load(getClass().getResource("/fxml/HelpAndSupport.fxml")));

        Set<MenuNode> nodes = new HashSet<>();

        nodes.add(homeNode);
        nodes.add(pluginsNode);
        nodes.add(helpNode);

        homeToggle.setOnAction(e -> nodes.stream().forEach(n -> context(n, homeNode)));
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
