/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.AbstractNotificationPane;
import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.ui.AppMenu;
import com.github.idelstak.manyfacesfx.ui.MenuNode;
import com.jfoenix.controls.JFXToggleNode;
import java.util.Iterator;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class AppViewController {

    private static final Logger LOG = Logger.getLogger(AppViewController.class.getName());
    private static final GlobalContext CONTEXT = GlobalContext.getDefault();
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
        lookupResult = CONTEXT.lookupResult(MenuNode.class);
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        masterBox.getChildren().setAll(AppMenu.HOME.get());

        lookupResult.addLookupListener(e -> {
            Iterator<? extends MenuNode> it = lookupResult.allInstances().iterator();

            if (it.hasNext()) {
                Platform.runLater(() -> initComponents(it.next()));
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

    private void initComponents(MenuNode node) {
        LOG.log(Level.FINE, "Initting components with node: {0}", node);

        titleLabel.textProperty().bind(node.displayNameProperty());
        notificationsToggle.setVisible(node.showsNotifications());
        detailPane.getChildren().setAll(node.getDetailsPane());

        ObservableList<Node> nodes = masterBox.getChildren();

        if (!nodes.isEmpty()
                && !Objects.equals(nodes.get(0), node.getMenuPane())) {
            nodes.set(0, node.getMenuPane());
        }
    }
}
