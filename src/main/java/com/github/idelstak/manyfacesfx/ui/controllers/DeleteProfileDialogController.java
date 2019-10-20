/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class DeleteProfileDialogController {

    @FXML
    private JFXButton closeButton;
    @FXML
    private Text messageText;
    @FXML
    private JFXButton yesButton;
    @FXML
    private JFXButton noButton;

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
    }

    public JFXButton getCloseButton() {
        return closeButton;
    }

    public Text getMessageText() {
        return messageText;
    }

    public JFXButton getYesButton() {
        return yesButton;
    }

    public JFXButton getNoButton() {
        return noButton;
    }
}
