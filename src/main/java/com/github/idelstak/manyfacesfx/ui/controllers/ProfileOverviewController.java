/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.api.Stackable;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.GeolocationNodeContext;
import com.github.idelstak.manyfacesfx.ui.ProxyNodeContext;
import com.github.idelstak.manyfacesfx.ui.TimezoneNodeContext;
import com.github.idelstak.manyfacesfx.ui.WebRtcNodeContext;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Optional;
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
    private final Lookup.Result<Profile> profileResult;
    private Profile currentProfile;

    static {
        LOG = Logger.getLogger(ProfileOverviewController.class.getName());
    }

    {
        profileResult = GlobalContext.getDefault().lookupResult(Profile.class);
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        editOsHyperlink.setOnAction(e -> showChooseOSDialog());
        editProxyButton.setOnAction(e -> new ProxyNodeContext().select());
        timezoneHyperlink.setOnAction(e -> new TimezoneNodeContext().select());
        webRtcHyperlink.setOnAction(e -> new WebRtcNodeContext().select());
        geolocationHyperlink.setOnAction(e -> new GeolocationNodeContext().select());

        initProfileNameBinding();

        profileResult.addLookupListener(e -> initProfileNameBinding());
    }

    private void initProfileNameBinding() {
        getProfileFromContext().ifPresent(profile -> {
            LOG.log(Level.FINE, "Current profile: {0}", currentProfile);
            LOG.log(Level.FINE, "Incoming profile: {0}", profile);

            if (currentProfile != null) {
                profileNameField.textProperty()
                        .unbindBidirectional(currentProfile.nameProperty());
            }

            profileNameField.textProperty()
                    .bindBidirectional(profile.nameProperty());

            currentProfile = profile;
        });
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

    private Optional<Profile> getProfileFromContext() {
        Optional<Profile> optionalProfile = Optional.empty();
        Iterator<? extends Profile> it = profileResult.allInstances().iterator();

        while (it.hasNext()) {
            Profile nextProfile = it.next();
            optionalProfile = Optional.of(nextProfile);
        }

        return optionalProfile;
    }

}
