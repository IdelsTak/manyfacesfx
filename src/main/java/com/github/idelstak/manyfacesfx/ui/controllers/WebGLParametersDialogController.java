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
public class WebGLParametersDialogController {

    @FXML
    private JFXButton closeButton;

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

        closeButton.setOnAction(e -> dialog.close());
    }

}
