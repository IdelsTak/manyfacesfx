/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.Stackable;
import com.github.javafaker.Faker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class MyAccountController {

    private static final Logger LOG;
    private static final Lookup LOOKUP = Lookup.getDefault();
    @FXML
    private Label emailLabel;
    @FXML
    private Label planLabel;
    @FXML
    private Hyperlink logsHyperlink;
    @FXML
    private JFXButton changePasswordButton;
    @FXML
    private JFXButton logoutButton;

    static {
        LOG = Logger.getLogger(MyAccountController.class.getName());
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        String email = new Faker().internet().emailAddress();
        emailLabel.setText(email);

        changePasswordButton.setOnAction(e -> {
            URL location = getClass().getResource("/fxml/PasswordDialog.fxml");
            FXMLLoader loader = new FXMLLoader(location);
            Pane pane = null;
            PasswordDialogController controller = null;

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
        });

        logoutButton.setOnAction(e -> {
            URL location = getClass().getResource("/fxml/LogoutDialog.fxml");
            FXMLLoader loader = new FXMLLoader(location);
            Pane pane = null;
            LogoutDialogController controller = null;

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
        });
    }

}
