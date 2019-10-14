/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.jfoenix.controls.JFXButton;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileAdvancedDevicesController {

    private static final Logger LOG;
    @FXML
    private RadioButton onToggle;
    @FXML
    private VBox devicesBox;
    @FXML
    private JFXButton minusVideoInputsButton;
    @FXML
    private Label numberOfVideoInputsLabel;
    @FXML
    private JFXButton plusVideoInputsButton;
    @FXML
    private JFXButton minusAudioInputsButton;
    @FXML
    private Label numberOfAudioInputsLabel;
    @FXML
    private JFXButton plusAudioInputsButton;
    @FXML
    private JFXButton minusAudioOutputsButton;
    @FXML
    private Label numberOfAudioOutputsLabel;
    @FXML
    private JFXButton plusAudioOutputsButton;
    private final NumberOfDevices numberOfVideoInputs;
    private final NumberOfDevices numberOfAudioInputs;
    private final NumberOfDevices numberOfAudioOutputs;

    static {
        LOG = Logger.getLogger(ProfileAdvancedDevicesController.class.getName());
    }

    {
        numberOfVideoInputs = new NumberOfDevices(0);
        numberOfAudioInputs = new NumberOfDevices(0);
        numberOfAudioOutputs = new NumberOfDevices(0);
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        devicesBox.visibleProperty().bind(onToggle.selectedProperty());

        numberOfVideoInputsLabel.textProperty().bind(numberOfVideoInputs.asString());
        numberOfAudioInputsLabel.textProperty().bind(numberOfAudioInputs.asString());
        numberOfAudioOutputsLabel.textProperty().bind(numberOfAudioOutputs.asString());

        minusVideoInputsButton.setOnAction(e -> numberOfVideoInputs.minusOne());
        plusVideoInputsButton.setOnAction(e -> numberOfVideoInputs.plusOne());
        minusAudioInputsButton.setOnAction(e -> numberOfAudioInputs.minusOne());
        plusAudioInputsButton.setOnAction(e -> numberOfAudioInputs.plusOne());
        minusAudioOutputsButton.setOnAction(e -> numberOfAudioOutputs.minusOne());
        plusAudioOutputsButton.setOnAction(e -> numberOfAudioOutputs.plusOne());
    }

    private static class NumberOfDevices extends SimpleIntegerProperty {

        private NumberOfDevices(int initialValue) {
            super(initialValue);
        }

        private void plusOne() {
            int numberOfDevices = super.get();

            if (numberOfDevices < 10) {
                numberOfDevices += 1;
                super.set(numberOfDevices);
            }
        }

        private void minusOne() {
            int numberOfDevices = super.get();

            if (numberOfDevices > 0) {
                numberOfDevices -= 1;
                super.set(numberOfDevices);
            }
        }

    }

}
