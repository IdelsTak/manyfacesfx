/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.ui.MenuNode;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.openide.util.lookup.Lookups;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class HomeMenuController {

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
        GlobalContext context = GlobalContext.getDefault();

        homeToggle.setOnAction(e -> context.setLookup(Lookups.singleton(homeNode)));
        pluginsToggle.setOnAction(e -> context.setLookup(Lookups.singleton(pluginsNode)));

        Platform.runLater(() -> {
            homeToggle.fireEvent(new ActionEvent());
            homeToggle.setSelected(true);
        });
    }

}
