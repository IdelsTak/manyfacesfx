/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import java.util.Objects;
import javafx.fxml.FXML;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class PasswordDialogController {

    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXButton changePasswordButton;
    @FXML
    private JFXButton cancelButton;

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        // TODO
    }
    
    void setDialog(JFXDialog dialog){
        String message = "Dialog should not be null";
        JFXDialog dlg = Objects.requireNonNull(dialog, message);
        
        closeButton.setOnAction(e -> dlg.close());
        cancelButton.setOnAction(e -> dlg.close());
    }

}
