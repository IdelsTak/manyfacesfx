/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.ui.EditType;
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
import javafx.scene.control.Label;
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
    private Label menuTitleLabel;
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
    private final Lookup.Result<ProfileMenuNode> profileMenuNodeResult;
    private final Lookup.Result<EditType> editTypeResult;

    {
        profileMenuNodeResult = CONTEXT.lookupResult(ProfileMenuNode.class);
        editTypeResult = CONTEXT.lookupResult(EditType.class);
    }

    /**
     Initializes the controller class.

     @throws java.io.IOException
     */
    @FXML
    public void initialize() throws IOException {
        OverViewNodeContext overViewNodeContext = new OverViewNodeContext();
        ProxyNodeContext proxyNodeContext = new ProxyNodeContext();
        TimezoneNodeContext timezoneNodeContext = new TimezoneNodeContext();
        WebRtcNodeContext webRtcNodeContext = new WebRtcNodeContext();
        GeolocationNodeContext geolocationNodeContext = new GeolocationNodeContext();

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

        overviewToggle.setOnAction(e -> overViewNodeContext.select());
        proxyToggle.setOnAction(e -> proxyNodeContext.select());
        timezoneToggle.setOnAction(e -> timezoneNodeContext.select());
        webRtcToggle.setOnAction(e -> webRtcNodeContext.select());
        geolocationToggle.setOnAction(e -> geolocationNodeContext.select());

        mediaDevicesToggle.setOnAction(e -> CONTEXT.set(ProfileMenuNode.class, mediaDevicesNode));
        extensionsToggle.setOnAction(e -> CONTEXT.set(ProfileMenuNode.class, extensionsNode));
        fontsToggle.setOnAction(e -> CONTEXT.set(ProfileMenuNode.class, fontsNode));
        hardwareToggle.setOnAction(e -> CONTEXT.set(ProfileMenuNode.class, hardwareNode));
        navigatorToggle.setOnAction(e -> CONTEXT.set(ProfileMenuNode.class, navigatorNode));
        otherToggle.setOnAction(e -> CONTEXT.set(ProfileMenuNode.class, otherNode));
        browserPluginsToggle.setOnAction(e -> CONTEXT.set(ProfileMenuNode.class, browserPluginsNode));
        storageOptionsToggle.setOnAction(e -> CONTEXT.set(ProfileMenuNode.class, storageOptionsNode));

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

        profileMenuNodeResult.addLookupListener(e -> manageToggleSelection());
        
        updateMenuTitleText();
        
        editTypeResult.addLookupListener(e -> updateMenuTitleText());
    }

    private void manageToggleSelection() {
        Iterator<? extends ProfileMenuNode> it = profileMenuNodeResult.allInstances().iterator();

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
        Platform.runLater(() -> {
            if (!button.isSelected()) {
                button.setSelected(true);
            }
        });
    }

    private void updateMenuTitleText() {
        Iterator<? extends EditType> it = editTypeResult.allInstances().iterator();
        
        if (it.hasNext()) {
            EditType editType = it.next();

            switch (editType) {
                case CREATE:
                    setMenuTitleText("New");
                    break;
                case UPDATE:
                    setMenuTitleText("Edit");
                    break;
                default:
                    throw new IllegalArgumentException("Edit type not known");
            }
        }
    }

    private void setMenuTitleText(String text) {
        Platform.runLater(() -> menuTitleLabel.setText(text + " browser profile"));
    }

}
