/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.ui.HomeNodeContext;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileMenuController {

    @FXML
    private JFXButton goHomeButton;
    @FXML
    private ToggleGroup profileMenuGroup;

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        goHomeButton.setOnAction(e -> {
            new HomeNodeContext().refreshContext();
        });
    }

}
