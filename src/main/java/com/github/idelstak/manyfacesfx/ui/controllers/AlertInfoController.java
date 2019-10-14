/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class AlertInfoController {

    @FXML
    private HBox alertBox;
    @FXML
    private Label alertLabel;

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        // TODO
    }

    void setMessage(String message, Style style) {
        alertLabel.setText(message);
        
        switch (style) {
            case INFO:
                alertBox.getStyleClass().remove("warning-box");
                alertBox.getStyleClass().add("info-box");
                break;
            case WARNING:
                alertBox.getStyleClass().remove("info-box");
                alertBox.getStyleClass().add("warning-box");
                break;
            default:
                throw new IllegalArgumentException("Style not known");
        }
    }

    enum Style {
        INFO, WARNING
    }

}
