/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.ui.AppMenu;
import com.github.idelstak.manyfacesfx.ui.HomeNodeContext;
import com.github.idelstak.manyfacesfx.ui.MenuNode;
import com.github.idelstak.manyfacesfx.ui.util.TitledPaneInputEventBypass;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.input.InputEvent;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileMenuController {

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
    private final Lookup.Result<MenuNode> lookupResult;

    {
        lookupResult = CONTEXT.lookupResult(MenuNode.class);
    }

    /**
     Initializes the controller class.

     @throws java.io.IOException
     */
    @FXML
    public void initialize() throws IOException {
        MenuNode overviewNode = new MenuNode(
                "overview",
                overviewToggle.getText(),
                false,
                FXMLLoader.load(getClass().getResource("/fxml/ProfileOverview.fxml")),
                AppMenu.PROFILE);
        
        Set<MenuNode> nodes = new HashSet<>();

        nodes.add(overviewNode);
        
        overviewToggle.setOnAction(e -> nodes.stream().forEach(n -> switchContext(n, overviewNode)));
        
        ensureOverviewSelected();

        advancedMenuTitledPane.addEventFilter(InputEvent.ANY, new TitledPaneInputEventBypass());
        advancedMenuTitledPane.expandedProperty().bind(advancedMenuToggle.selectedProperty());

        goHomeButton.setOnAction(e -> switchToHome());

    }

    private void switchToHome() {
        new HomeNodeContext().refreshContext();
        //Collapse the advanced menu when
        //the edit profile page is exited
        Platform.runLater(() -> advancedMenuToggle.setSelected(false));
    }

    private void ensureOverviewSelected() {
        Platform.runLater(() -> {
//            overviewToggle.fireEvent(new ActionEvent());
//            overviewToggle.setSelected(true);
        });
    }

    private void switchContext(MenuNode n1, MenuNode n2) {
        if (Objects.equals(n1, n2)) {
            CONTEXT.add(n1);
        } else {
            CONTEXT.remove(n1);
        }
    }
}
