/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.ui.AppMenu;
import com.github.idelstak.manyfacesfx.ui.MenuNode;
import java.io.IOException;
import java.util.Iterator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileAttributesController {

    private static final GlobalContext CONTEXT = GlobalContext.getDefault();
    @FXML
    private HBox attributesBox;
    @FXML
    private AnchorPane detailsBox;
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
        Pane pane = FXMLLoader.load(getClass().getResource("/fxml/ProfileSummary.fxml"));

        Platform.runLater(() -> {
            attributesBox.getChildren().add(1, pane);
            HBox.setHgrow(pane, Priority.NEVER);
        });

        lookupResult.addLookupListener(e -> {
            Iterator<? extends MenuNode> it = lookupResult.allInstances().iterator();

            if (it.hasNext()) {
                MenuNode node = it.next();
                
                if (node.getMenu()!= AppMenu.PROFILE) {
                    return;
                }
                
                Node detailsPane = node.getDetailsPane();

                Platform.runLater(() -> {
                    detailsBox.getChildren().setAll(detailsPane);

                    AnchorPane.setTopAnchor(detailsPane, 0.0);
                    AnchorPane.setRightAnchor(detailsPane, 0.0);
                    AnchorPane.setBottomAnchor(detailsPane, 0.0);
                    AnchorPane.setLeftAnchor(detailsPane, 0.0);
                });
            }
        });
    }

}
