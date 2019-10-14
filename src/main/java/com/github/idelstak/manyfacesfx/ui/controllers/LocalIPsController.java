/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.javafaker.Faker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.util.Objects;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class LocalIPsController {

    private static final Logger LOG;
    @FXML
    private AnchorPane ipRootPane;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private Label ipPaneCountLabel;
    @FXML
    private JFXTextField ipField;

    static {
        LOG = Logger.getLogger(LocalIPsController.class.getName());
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        ipField.setText(new Faker().internet().privateIpV4Address());
    }

    void setParentAndCountProperty(Pane pane, SimpleIntegerProperty countProperty) {
        String message = "Extra IPs box should not be null";
        Pane extraIpsBox = Objects.requireNonNull(pane, message);

        deleteButton.setOnAction(e -> Platform.runLater(() -> {
            extraIpsBox.getChildren().remove(ipRootPane);
            countProperty.set(countProperty.get() - 1);
        }));

        Platform.runLater(() -> setCountText(countProperty.get()));

        countProperty.addListener((o, ov, nv) -> {
            int idx = extraIpsBox.getChildren().indexOf(ipRootPane);
            setCountText(idx + 2);
        });
    }

    private void setCountText(int count) {
        String text = String.format("Local IP %d", count);
        ipPaneCountLabel.setText(text);
    }

}
