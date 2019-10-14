/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import java.util.Objects;
import javafx.fxml.FXML;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ExtensionsDialogController {

    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        // TODO
    }

    void setDialog(JFXDialog fXDialog) {
        String message = "Dialog should not be null";
        JFXDialog dialog = Objects.requireNonNull(fXDialog, message);
        
        cancelButton.setOnAction(e -> dialog.close());
        closeButton.setOnAction(e -> dialog.close());
    }

}
