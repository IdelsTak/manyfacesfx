/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.ui.NewProfileNodeContext;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class NoProfileAvailableController {

    @FXML
    private Hyperlink createProfileHyperlink;

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        createProfileHyperlink.setOnAction(e -> {
            new NewProfileNodeContext().select();
        });
    }

}
