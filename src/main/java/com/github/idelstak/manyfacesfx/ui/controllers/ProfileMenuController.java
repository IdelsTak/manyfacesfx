/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.ui.HomeNodeContext;
import com.github.idelstak.manyfacesfx.ui.util.TitledPaneInputEventBypass;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.input.InputEvent;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileMenuController {

    @FXML
    private JFXButton goHomeButton;
    @FXML
    private TitledPane advancedMenuTitledPane;
    @FXML
    private RadioButton advancedMenuToggle;

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        goHomeButton.setOnAction(e -> {
            new HomeNodeContext().refreshContext();
            //Collapse the advanced menu when
            //the edit profile page is exited
            Platform.runLater(() -> advancedMenuToggle.setSelected(false));
        });

        advancedMenuTitledPane.addEventFilter(InputEvent.ANY, new TitledPaneInputEventBypass());
        
        advancedMenuTitledPane.expandedProperty().bind(advancedMenuToggle.selectedProperty());
    }

}
