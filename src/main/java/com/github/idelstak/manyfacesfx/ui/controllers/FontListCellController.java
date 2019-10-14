/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.jfoenix.controls.JFXCheckBox;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class FontListCellController {

    @FXML
    private Label fontLabel;
    @FXML
    private JFXCheckBox fontCheckBox;

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        // TODO
    }

    void setFontFamilyName(String fontFamily) {
        String message = "Font family name should not be null";
        String font = Objects.requireNonNull(fontFamily, message);
        
        fontLabel.setText(font);
    }

}
