/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.Stackable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXToggleButton;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileAdvancedExtensionsController {

    private static final Logger LOG;
    private static final Lookup LOOKUP = Lookup.getDefault();
    @FXML
    private JFXToggleButton preloadExtensionsToggle;
    @FXML
    private VBox preloadExtensionsBox;
    @FXML
    private JFXButton selectButton;

    static {
        LOG = Logger.getLogger(ExtensionsDialogController.class.getName());
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        preloadExtensionsBox.visibleProperty().bind(preloadExtensionsToggle.selectedProperty());

        selectButton.setOnAction(e -> showExtensionsDialog());
    }

    private void showExtensionsDialog() {
        URL location = getClass().getResource("/fxml/ExtensionsDialog.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Pane pane = null;
        ExtensionsDialogController controller = null;

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
