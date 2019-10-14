/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.Stackable;
import com.github.idelstak.manyfacesfx.ui.ProxyNodeContext;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
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
public class ProfileOverviewController {

    private static final Logger LOG;
    private static final Lookup LOOKUP = Lookup.getDefault();
    @FXML
    private JFXButton editProxyButton;
    @FXML
    private Hyperlink timezoneHyperlink;
    @FXML
    private Hyperlink webRtcHyperlink;
    @FXML
    private Hyperlink geolocationHyperlink;
    @FXML
    private JFXTextField profileNameField;
    @FXML
    private Hyperlink editOsHyperlink;
    @FXML
    private Label osLabel;

    static {
        LOG = Logger.getLogger(ProfileOverviewController.class.getName());
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        editOsHyperlink.setOnAction(e -> showChooseOSDialog());
        editProxyButton.setOnAction(e -> new ProxyNodeContext().refreshContext());
    }

    private void showChooseOSDialog() {
        URL location = getClass().getResource("/fxml/ChooseOSDialog.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Pane pane = null;
        ChooseOSDialogController controller = null;

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
            controller.setOSLabel(osLabel);
            dialog.show(Stackable.getDefault().getStackPane());
        }
    }

}
