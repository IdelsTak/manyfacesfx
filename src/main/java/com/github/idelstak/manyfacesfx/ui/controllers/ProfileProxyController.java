/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleNode;
import javafx.fxml.FXML;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileProxyController {

    @FXML
    private JFXComboBox<Proxy> connectionTypeComboBox;
    @FXML
    private JFXToggleNode showPasswordToggle;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXTextField passwordTextField;

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        connectionTypeComboBox.getItems().setAll(Proxy.values());
        passwordField.visibleProperty()
                .bind(showPasswordToggle.selectedProperty().not());
        passwordTextField.visibleProperty()
                .bind(showPasswordToggle.selectedProperty());
        passwordTextField.promptTextProperty()
                .bind(passwordField.promptTextProperty());
        passwordTextField.textProperty()
                .bindBidirectional(passwordField.textProperty());
    }

    private enum Proxy implements Type {

        NONE("Without proxy"),
        HTTP("HTTP proxy"),
        SOCKS4("Socks 4 proxy"),
        SOCKS5("Socks 5 proxy");

        private final String description;

        private Proxy(String description) {
            this.description = description;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return description;
        }

    }

    private interface Type {

        String getDescription();
    }

}
