/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.javafaker.Faker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileWebRtcAlteredController {

    private static final Logger LOG;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXToggleButton fillIPToggle;
    @FXML
    private JFXTextField fillIPField;
    @FXML
    private JFXTextField ip1Field;
    @FXML
    private VBox extraIpsBox;
    @FXML
    private Hyperlink addIpHyperlink;
    private final SimpleIntegerProperty ipPanesCountProperty;
    private int ipPanesCount;

    static {
        LOG = Logger.getLogger(ProfileWebRtcAlteredController.class.getName());
    }

    {
        ipPanesCount = 1;
        ipPanesCountProperty = new SimpleIntegerProperty(ipPanesCount);
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        fillIPField.disableProperty().bind(fillIPToggle.selectedProperty());

        fillIPField.setText(new Faker().internet().publicIpV4Address());
        ip1Field.setText(new Faker().internet().ipV4Address());

        addIpHyperlink.setOnAction(e -> {
            Pane extraIpPane = getExtraIpPane();

            addHeightToRoot(extraIpPane.getPrefHeight());
            extraIpsBox.getChildren().add(extraIpPane);

            ipPanesCount += 1;
            ipPanesCountProperty.set(ipPanesCount);
        });

        ipPanesCountProperty.addListener((o, ov, nv) -> {
            ipPanesCount = nv.intValue();
        });
    }

    private void addHeightToRoot(double height) {
        rootPane.setMinHeight(rootPane.getHeight() + height);
    }

    private Pane getExtraIpPane() {
        URL location = getClass().getResource("/fxml/LocalIPs.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Pane pane = null;

        try {
            pane = loader.load();

            LocalIPsController controller = loader.getController();
            controller.setParentAndCountProperty(extraIpsBox, ipPanesCountProperty);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return pane;
    }
}
