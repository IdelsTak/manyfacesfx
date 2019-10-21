/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

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
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
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
    private Label titleLabel;
    @FXML
    private JFXToggleNode notificationsToggle;
    @FXML
    private StackPane detailPane;
    private final Lookup.Result<MenuNode> menuNodeResult;

    {
        menuNodeResult = CONTEXT.lookupResult(MenuNode.class);
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        Pane homeMenuPane = AppMenu.HOME.get();
        masterBox.getChildren().setAll(homeMenuPane);
        VBox.setVgrow(homeMenuPane, Priority.ALWAYS);

        menuNodeResult.addLookupListener(e -> {
            Iterator<? extends MenuNode> it = menuNodeResult.allInstances().iterator();

            //Use while loop because a ProfileMenuNode
            //extends the MenuNode. The while loop thus 
            //enables operations to be carried on the last
            //context in the global lookup
            while (it.hasNext()) {
                //Variable declared outside the Platform.runLater
                //runnable to avoid introducing a race condition
                MenuNode nextMenuNode = it.next();
                Platform.runLater(() -> initComponents(nextMenuNode));
            }
        });
    }

    /**
     For testing notifications display.

     @param event
     */
    @FXML
    void showNotification(ActionEvent event) {
//        AbstractNotificationPane.getDefault().notify(event.toString());
    }

    private void initComponents(MenuNode node) {
        titleLabel.textProperty().bind(node.displayNameProperty());
        notificationsToggle.setVisible(node.showsNotifications());

        //Don't allow profile menu nodes to set the 
        //detailsPane content. That's handled elsewhere
        if (!node.getClass().equals(ProfileMenuNode.class)) {
            detailPane.getChildren().setAll(node.getDetailsPane());
        }

        ObservableList<Node> nodes = masterBox.getChildren();

        if (!nodes.isEmpty()
                && !Objects.equals(nodes.get(0), node.getMenuPane())) {
            nodes.set(0, node.getMenuPane());
            VBox.setVgrow(node.getMenuPane(), Priority.ALWAYS);
        }
    }
}
