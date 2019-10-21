/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.jfoenix.controls.JFXComboBox;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class PreferencesController {

    private static final Logger LOG;
    @FXML
    private AnchorPane preferencesRootPane;
    @FXML
    private JFXComboBox<Resolution> nativeResolutionComboBox;
    @FXML
    private JFXComboBox<Resolution> minResolutionComboBox;
    @FXML
    private JFXComboBox<Resolution> maxResolutionComboBox;
    @FXML
    private ToggleGroup defaultBrowser;
    @FXML
    private RadioButton foxBrowserToggle;
    @FXML
    private RadioButton chromeBrowserToggle;

    static {
        LOG = Logger.getLogger(PreferencesController.class.getName());

    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        nativeResolutionComboBox.getItems().setAll(Resolution.values());
        minResolutionComboBox.getItems().setAll(Resolution.values());
        maxResolutionComboBox.getItems().setAll(Resolution.values());

        Platform.runLater(() -> {
            nativeResolutionComboBox.getSelectionModel().select(13);
            minResolutionComboBox.getSelectionModel().select(8);
            maxResolutionComboBox.getSelectionModel().selectLast();
        });
    }

    private enum Resolution implements Size {

        _800x600("800x600"),
        _1024x600("1024x600"),
        _1024x768("1024x768"),
        _1024x640("1024x640"),
        _1152x864("1152x864"),
        _1280x720("1280x720"),
        _1280x768("1280x768"),
        _1280x800("1280x800"),
        _1280x960("1280x960"),
        _1280x1024("1280x1024"),
        _1360x768("1360x768"),
        _1366x768("1366x768"),
        _1400x900("1400x900"),
        _1536x864("1536x864"),
        _1600x900("1600x900"),
        _1600x1200("1600x1200"),
        _1680x1050("1680x1050"),
        _1920x1080("1920x1080"),
        _1920x1200("1920x1200"),
        _2048x1152("2048x1152"),
        _2304x1440("2304x1440"),
        _2560x1440("2560x1440"),
        _2560x1600("2560x1600"),
        _2880x1800("2880x1800"),
        _4096x4230("4096x4230"),
        _5120x2880("5120x2880");

        private final String value;

        private Resolution(String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }

    }

    private interface Size {

        String value();
    }
}
