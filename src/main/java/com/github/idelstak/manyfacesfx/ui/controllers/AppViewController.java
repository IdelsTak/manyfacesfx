/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.AbstractNotificationPane;
import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.ui.AppMenu;
import com.github.idelstak.manyfacesfx.ui.MenuNode;
import com.github.idelstak.manyfacesfx.ui.ProfileMenuNode;
import com.jfoenix.controls.JFXToggleNode;
import java.util.Iterator;
import java.util.Objects;
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
    private final Lookup.Result<MenuNode> menuNodeResult;
    private final Lookup.Result<ProfileMenuNode> profileMenuNodeResult;

    {
        menuNodeResult = CONTEXT.lookupResult(MenuNode.class);
        profileMenuNodeResult = CONTEXT.lookupResult(ProfileMenuNode.class);
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        masterBox.getChildren().setAll(AppMenu.HOME.get());

        menuNodeResult.addLookupListener(e -> {
            Iterator<? extends MenuNode> it = menuNodeResult.allInstances().iterator();

            if (it.hasNext()) {
                Platform.runLater(() -> initComponents(it.next()));
            }
        });

        profileMenuNodeResult.addLookupListener(e -> {
            Iterator<? extends ProfileMenuNode> it = profileMenuNodeResult.allInstances().iterator();

            if (it.hasNext()) {
                Platform.runLater(() -> {
                    titleLabel.textProperty().unbind();
                    titleLabel.textProperty().bind(it.next().displayNameProperty());
                });
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
        titleLabel.textProperty().unbind();
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
