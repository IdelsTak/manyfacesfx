/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.ui.HomeNodeContext;
import com.github.idelstak.manyfacesfx.ui.OverViewNodeContext;
import com.github.idelstak.manyfacesfx.ui.ProfileMenuNode;
import com.github.idelstak.manyfacesfx.ui.util.TitledPaneInputEventBypass;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
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

        overviewToggle.setOnAction(e -> CONTEXT.replace(ProfileMenuNode.class, overviewNode));

        //Ensure overview is selected
        Platform.runLater(() -> {
            overviewToggle.fireEvent(new ActionEvent());
            overviewToggle.setSelected(true);
        });

        advancedMenuTitledPane.addEventFilter(InputEvent.ANY, new TitledPaneInputEventBypass());
        advancedMenuTitledPane.expandedProperty().bind(advancedMenuToggle.selectedProperty());

        goHomeButton.setOnAction(e -> {
            new HomeNodeContext().refreshContext();
            //Collapse the advanced menu when
            //the edit profile page is exited
            Platform.runLater(() -> advancedMenuToggle.setSelected(false));
        });
    }


}
