/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.AbstractNavigationBar;
import com.github.idelstak.manyfacesfx.api.AbstractNotificationPane;
import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.ui.MenuNode;
import com.jfoenix.controls.JFXToggleNode;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class AppViewController {

    private static final Logger LOG = Logger.getLogger(AppViewController.class.getName());
    @FXML
    private VBox masterBox;
    @FXML
    private VBox viewBox;
    @FXML
    private HBox titleBox;
    @FXML
    private Label titleLabel;
    @FXML
    private JFXToggleNode notificationsToggle;
    @FXML
    private StackPane detailPane;
    private final Lookup.Result<MenuNode> lookupResult;

    {
        lookupResult = GlobalContext.getDefault().lookupResult(MenuNode.class);
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        Pane navigationPane = AbstractNavigationBar.getDefault()
                .getLookup()
                .lookup(Pane.class);

        masterBox.getChildren().setAll(navigationPane);

        lookupResult.addLookupListener(e -> {
            Collection<? extends MenuNode> result = lookupResult.allInstances();
            Iterator<? extends MenuNode> it = result.iterator();

            if (it.hasNext()) {
                MenuNode menuNode = it.next();

                titleLabel.textProperty().bind(menuNode.displayNameProperty());
                notificationsToggle.setVisible(menuNode.showsNotifications());
                detailPane.getChildren().setAll(menuNode.getDetailsPane());
            }
        });
    }

    /**
     For testing notifications display.

     @param event
     */
    @FXML
    void showNotification(ActionEvent event) {
        AbstractNotificationPane.getDefault().notify(event.toString());
    }
}
