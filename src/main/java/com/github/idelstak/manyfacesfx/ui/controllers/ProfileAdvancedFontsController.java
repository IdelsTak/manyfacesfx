/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.Stackable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXToggleButton;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileAdvancedFontsController {

    private static final Logger LOG;
    @FXML
    private JFXToggleButton fontsMaskingToggle;
    @FXML
    private VBox fontsMaskingBox;
    @FXML
    private VBox fontsBox;
    @FXML
    private Hyperlink moreFontsHyperlink;
    @FXML
    private JFXButton editButton;

    static {
        LOG = Logger.getLogger(ProfileAdvancedFontsController.class.getName());
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        fontsMaskingBox.visibleProperty().bind(fontsMaskingToggle.selectedProperty());
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] families = ge.getAvailableFontFamilyNames(Locale.getDefault());

        for (int i = 0; i < 10; i++) {
            String family = families[i];
            fontsBox.getChildren().add(new Label(family));
        }

        moreFontsHyperlink.setText(String.format("...and %d more", (families.length - 11)));

        moreFontsHyperlink.setOnAction(e -> {
            for (int i = 10; i < families.length; i++) {
                String family = families[i];
                fontsBox.getChildren().add(new Label(family));
            }

            moreFontsHyperlink.setVisible(false);
        });

        editButton.setOnAction(e -> showFontsDialog(families));
    }

    private void showFontsDialog(String[] fontFamilies) {
        URL location = getClass().getResource("/fxml/FontsDialog.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Pane pane = null;
        FontsDialogController controller = null;

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
            controller.setFontFamilies(fontFamilies);
            dialog.show(Stackable.getDefault().getStackPane());
        }
    }

}
