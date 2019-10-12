/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileAttributesController {

    @FXML
    private HBox attributesBox;
    @FXML
    private VBox detailsBox;

    /**
     Initializes the controller class.

     @throws java.io.IOException
     */
    @FXML
    public void initialize() throws IOException {
        Pane pane = FXMLLoader.load(getClass().getResource("/fxml/ProfileSummary.fxml"));

        Platform.runLater(() -> {
            attributesBox.getChildren().add(1, pane);
            HBox.setHgrow(pane, Priority.NEVER);
        });
    }

}
