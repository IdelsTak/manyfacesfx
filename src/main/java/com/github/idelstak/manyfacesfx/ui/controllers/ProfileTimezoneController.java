/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.ui.Timezone;
import com.github.idelstak.manyfacesfx.ui.Timezones;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import java.util.Collection;
import java.util.logging.Logger;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileTimezoneController {

    private static final Logger LOG;
    @FXML
    private JFXToggleButton fillTimezoneToggle;
    @FXML
    private VBox timezonePickerBox;
    @FXML
    private JFXComboBox<Timezone> timezonesComboBox;
    @FXML
    private Label utcOffsetLabel;

    static {
        LOG = Logger.getLogger(ProfileTimezoneController.class.getName());
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        BooleanProperty displayTimezoneBox = timezonePickerBox.visibleProperty();
        BooleanBinding timezoneToggleNotSelected = fillTimezoneToggle.selectedProperty().not();
        displayTimezoneBox.bind(timezoneToggleNotSelected);

        Collection<Timezone> timezones = new Timezones().getTimezones(true);
        timezonesComboBox.getItems().setAll(timezones);

        timezonesComboBox.getSelectionModel()
                .selectedItemProperty()
                .addListener((o, ov, nv) -> {
                    utcOffsetLabel.setText(nv != null ? nv.getOffset() : null);
                });
    }

}
