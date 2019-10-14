/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.Stackable;
import com.github.idelstak.manyfacesfx.ui.controllers.AlertInfoController.Style;
import com.github.javafaker.Faker;
import com.jfoenix.controls.JFXDialog;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileAdvancedHardwareController {

    private static final Logger LOG;
    @FXML
    private RadioButton canvasNoiseToggle;
    @FXML
    private RadioButton canvasOffToggle;
    @FXML
    private RadioButton canvasBlockToggle;
    @FXML
    private BorderPane canvasAlertPane;
    @FXML
    private Label canvasHashLabel;
    @FXML
    private RadioButton audioContextNoiseToggle;
    @FXML
    private RadioButton audioContextOffToggle;
    @FXML
    private BorderPane audioContextAlertPane;
    @FXML
    private Label audioContextHashLabel;
    @FXML
    private RadioButton webGLImageNoiseToggle;
    @FXML
    private RadioButton webGLImageOffToggle;
    @FXML
    private BorderPane webGLImageAlertPane;
    @FXML
    private RadioButton webGLMetadataMaskToggle;
    @FXML
    private RadioButton webGLMetadataOffToggle;
    @FXML
    private BorderPane webGLMetadataAlertPane;
    @FXML
    private Label webGLMetadataHashLabel;
    @FXML
    private VBox otherParametersBox;
    @FXML
    private Hyperlink otherParametersHyperlink;

    static {
        LOG = Logger.getLogger(ProfileAdvancedHardwareController.class.getName());
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        canvasNoiseToggle.setOnAction(e -> {
            if (canvasNoiseToggle.isSelected()) {
                Pane alertPane = getAlertPane("Canvas fingerprint will be masked "
                        + "by applying a "
                        + "unique and persistent noise. If you launch the same "
                        + "browser profile on different hardware the resulting "
                        + "canvas fingerprint may differ. This option is most "
                        + "popular for web scraping.", Style.INFO);
                canvasAlertPane.setCenter(alertPane);
            }
        });
        canvasOffToggle.setOnAction(e -> {
            if (canvasOffToggle.isSelected()) {
                Pane alertPane = getAlertPane("Don't mask Canvas. "
                        + "Suitable for most users.", Style.INFO);
                canvasAlertPane.setCenter(alertPane);
            }
        });
        canvasBlockToggle.setOnAction(e -> {
            if (canvasBlockToggle.isSelected()) {
                Pane alertPane = getAlertPane("Block - Block websites from "
                        + "reading Canvas "
                        + "fingerprint. This is a legacy mode and it's not "
                        + "advised to use.", Style.WARNING);
                canvasAlertPane.setCenter(alertPane);
            }
        });

        Platform.runLater(() -> canvasNoiseToggle.fireEvent(new ActionEvent()));

        canvasHashLabel.setText(new Faker().internet().uuid().replaceAll("-", ""));

        audioContextNoiseToggle.setOnAction(e -> {
            if (audioContextNoiseToggle.isSelected()) {
                Pane alertPane = getAlertPane("AudioContext fingerprint will be "
                        + "masked by applying a unique and persistent noise. If "
                        + "you launch the same browser profile on different "
                        + "hardware the resulting canvas fingerprint may differ.",
                        Style.INFO);
                audioContextAlertPane.setCenter(alertPane);
            }
        });

        audioContextOffToggle.setOnAction(e -> {
            if (audioContextOffToggle.isSelected()) {
                Pane alertPane = getAlertPane("Don't mask AudioContext. "
                        + "Suitable for most users.", Style.INFO);
                audioContextAlertPane.setCenter(alertPane);
            }
        });

        Platform.runLater(() -> audioContextNoiseToggle.fireEvent(new ActionEvent()));

        audioContextHashLabel.setText(new Faker().internet().uuid().replaceAll("-", ""));

        webGLImageNoiseToggle.setOnAction(e -> {
            Pane alertPane = getAlertPane("WebGL Image will be masked by "
                    + "applying a unique and persistent noise. There's no "
                    + "confirmed information about web sites actually utilizing "
                    + "this parameter for browser fingerprinting.", Style.INFO);
            webGLImageAlertPane.setCenter(alertPane);
        });

        webGLImageOffToggle.setOnAction(e -> {
            Pane alertPane = getAlertPane("Don't mask WebGL Image. There's no "
                    + "confirmed information about web sites actually utilizing "
                    + "this parameter for browser fingerprinting.", Style.INFO);
            webGLImageAlertPane.setCenter(alertPane);
        });

        webGLMetadataMaskToggle.setOnAction(e -> {
            Pane alertPane = getAlertPane("Replace WebGL metadata with "
                    + "alternative suitable values. Recommended for most use "
                    + "cases.", Style.INFO);
            webGLMetadataAlertPane.setCenter(alertPane);
        });
        
        otherParametersBox.visibleProperty().bind(webGLMetadataMaskToggle.selectedProperty());

        webGLMetadataOffToggle.setOnAction(e -> {
            Pane alertPane = getAlertPane("Don't mask WebGL Metadata. Websites "
                    + "will see real WebGL parameters of your computer.",
                    Style.WARNING);
            webGLMetadataAlertPane.setCenter(alertPane);
        });

        Platform.runLater(() -> webGLImageNoiseToggle.fireEvent(new ActionEvent()));
        Platform.runLater(() -> webGLMetadataMaskToggle.fireEvent(new ActionEvent()));

        webGLMetadataHashLabel.setText(new Faker().internet().uuid().replaceAll("-", ""));
        
        otherParametersHyperlink.setOnAction(e -> showOtherParametersDialog());
    }

    private Pane getAlertPane(String message, Style style) {
        URL location = getClass().getResource("/fxml/AlertInfo.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Pane pane = null;

        try {
            pane = loader.load();

            AlertInfoController controller = loader.getController();
            controller.setMessage(message, style);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        return pane;
    }

    private void showOtherParametersDialog() {
        URL location = getClass().getResource("/fxml/WebGLParametersDialog.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Pane pane = null;
        WebGLParametersDialogController controller = null;

        try {
            pane = loader.load();
            controller = loader.getController();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        if (pane != null && controller != null) {
            JFXDialog dialog = new JFXDialog();
            dialog.setContent(pane);
            controller.setDialog(dialog);
            dialog.show(Stackable.getDefault().getStackPane());
        }
    }

}
