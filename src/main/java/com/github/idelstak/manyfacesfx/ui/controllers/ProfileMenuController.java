/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.ui.GeolocationNodeContext;
import com.github.idelstak.manyfacesfx.ui.HomeNodeContext;
import com.github.idelstak.manyfacesfx.ui.OverViewNodeContext;
import com.github.idelstak.manyfacesfx.ui.ProfileMenuNode;
import com.github.idelstak.manyfacesfx.ui.ProxyNodeContext;
import com.github.idelstak.manyfacesfx.ui.TimezoneNodeContext;
import com.github.idelstak.manyfacesfx.ui.WebRtcNodeContext;
import com.github.idelstak.manyfacesfx.ui.util.TitledPaneInputEventBypass;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.InputEvent;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileMenuController {

    private static final Logger LOG = Logger.getLogger(ProfileMenuController.class.getName());
    private static final GlobalContext CONTEXT = GlobalContext.getDefault();
    @FXML
    private JFXButton goHomeButton;
    @FXML
    private ToggleGroup profileMenuGroup;
    @FXML
    private TitledPane advancedMenuTitledPane;
    @FXML
    private RadioButton advancedMenuToggle;
    @FXML
    private RadioButton overviewToggle;
    @FXML
    private RadioButton proxyToggle;
    @FXML
    private RadioButton timezoneToggle;
    @FXML
    private RadioButton webRtcToggle;
    @FXML
    private RadioButton geolocationToggle;
    @FXML
    private RadioButton navigatorToggle;
    @FXML
    private RadioButton fontsToggle;
    @FXML
    private RadioButton mediaDevicesToggle;
    @FXML
    private RadioButton hardwareToggle;
    @FXML
    private RadioButton extensionsToggle;
    @FXML
    private RadioButton storageOptionsToggle;
    @FXML
    private RadioButton browserPluginsToggle;
    @FXML
    private RadioButton otherToggle;
    private final Lookup.Result<ProfileMenuNode> lookupResult;

    {
        lookupResult = CONTEXT.lookupResult(ProfileMenuNode.class);
    }

    /**
     Initializes the controller class.

     @throws java.io.IOException
     */
    @FXML
    public void initialize() throws IOException {
        ProfileMenuNode overviewNode = new OverViewNodeContext().getNode();
        ProfileMenuNode proxyNode = new ProxyNodeContext().getNode();
        ProfileMenuNode timezoneNode = new TimezoneNodeContext().getNode();
        ProfileMenuNode webRtcNode = new WebRtcNodeContext().getNode();
        ProfileMenuNode geolocationNode = new GeolocationNodeContext().getNode();

        ProfileMenuNode mediaDevicesNode = new ProfileMenuNode(
                "media devices",
                mediaDevicesToggle.getText(),
                FXMLLoader.load(getClass().getResource("/fxml/ProfileAdvancedDevices.fxml")));
        ProfileMenuNode extensionsNode = new ProfileMenuNode(
                "extensions",
                extensionsToggle.getText(),
                FXMLLoader.load(getClass().getResource("/fxml/ProfileAdvancedExtensions.fxml")));
        ProfileMenuNode fontsNode = new ProfileMenuNode(
                "fonts",
                fontsToggle.getText(),
                FXMLLoader.load(getClass().getResource("/fxml/ProfileAdvancedFonts.fxml")));
        ProfileMenuNode hardwareNode = new ProfileMenuNode(
                "hardware",
                hardwareToggle.getText(),
                FXMLLoader.load(getClass().getResource("/fxml/ProfileAdvancedHardware.fxml")));
        ProfileMenuNode navigatorNode = new ProfileMenuNode(
                "navigator",
                navigatorToggle.getText(),
                FXMLLoader.load(getClass().getResource("/fxml/ProfileAdvancedNavigator.fxml")));
        ProfileMenuNode otherNode = new ProfileMenuNode(
                "other",
                otherToggle.getText(),
                FXMLLoader.load(getClass().getResource("/fxml/ProfileAdvancedOther.fxml")));
        ProfileMenuNode browserPluginsNode = new ProfileMenuNode(
                "browser plugins",
                browserPluginsToggle.getText(),
                FXMLLoader.load(getClass().getResource("/fxml/ProfileAdvancedPlugins.fxml")));
        ProfileMenuNode storageOptionsNode = new ProfileMenuNode(
                "storage options",
                storageOptionsToggle.getText(),
                FXMLLoader.load(getClass().getResource("/fxml/ProfileAdvancedStorage.fxml")));

        overviewToggle.setOnAction(e -> CONTEXT.replace(ProfileMenuNode.class, overviewNode));
        proxyToggle.setOnAction(e -> CONTEXT.replace(ProfileMenuNode.class, proxyNode));
        timezoneToggle.setOnAction(e -> CONTEXT.replace(ProfileMenuNode.class, timezoneNode));
        webRtcToggle.setOnAction(e -> CONTEXT.replace(ProfileMenuNode.class, webRtcNode));
        geolocationToggle.setOnAction(e -> CONTEXT.replace(ProfileMenuNode.class, geolocationNode));

        mediaDevicesToggle.setOnAction(e -> CONTEXT.replace(ProfileMenuNode.class, mediaDevicesNode));
        extensionsToggle.setOnAction(e -> CONTEXT.replace(ProfileMenuNode.class, extensionsNode));
        fontsToggle.setOnAction(e -> CONTEXT.replace(ProfileMenuNode.class, fontsNode));
        hardwareToggle.setOnAction(e -> CONTEXT.replace(ProfileMenuNode.class, hardwareNode));
        navigatorToggle.setOnAction(e -> CONTEXT.replace(ProfileMenuNode.class, navigatorNode));
        otherToggle.setOnAction(e -> CONTEXT.replace(ProfileMenuNode.class, otherNode));
        browserPluginsToggle.setOnAction(e -> CONTEXT.replace(ProfileMenuNode.class, browserPluginsNode));
        storageOptionsToggle.setOnAction(e -> CONTEXT.replace(ProfileMenuNode.class, storageOptionsNode));

        //Ensure overview is selected
        Platform.runLater(() -> {
            overviewToggle.fireEvent(new ActionEvent());
            overviewToggle.setSelected(true);
        });

        advancedMenuTitledPane.addEventFilter(InputEvent.ANY, new TitledPaneInputEventBypass());
        advancedMenuTitledPane.expandedProperty().bind(advancedMenuToggle.selectedProperty());

        goHomeButton.setOnAction(e -> {
            new HomeNodeContext().select();
            //Collapse the advanced menu when
            //the edit profile page is exited
            Platform.runLater(() -> advancedMenuToggle.setSelected(false));
        });

        lookupResult.addLookupListener(e -> manageToggleSelection());
    }

    private void manageToggleSelection() {
        Iterator<? extends ProfileMenuNode> it = lookupResult.allInstances().iterator();

        if (it.hasNext()) {
            ProfileMenuNode node = it.next();

            profileMenuGroup.getToggles()
                    .stream()
                    .map(RadioButton.class::cast)
                    .filter(button -> button.getText().equals(node.getDisplayName()))
                    .findFirst()
                    .ifPresent(this::selectIfNotSelected);
        }
    }

    private void selectIfNotSelected(RadioButton button) {
        Platform.runLater(() -> button.setSelected(!button.isSelected()));
    }

}
