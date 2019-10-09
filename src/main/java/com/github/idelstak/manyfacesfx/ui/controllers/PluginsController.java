/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class PluginsController {

    private static final Logger LOG;
    @FXML
    private JFXTextField searchField;
    @FXML
    private Accordion accordion;

    static {
        LOG = Logger.getLogger(PluginsController.class.getName());
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        searchField.textProperty().addListener((o, ov, nv) -> {
            if (nv == null || nv.trim().isEmpty()) {
                Platform.runLater(() -> refreshRows());
            } else {
                List<TitledPane> list = accordion.getPanes()
                        .stream()
                        .filter(tp -> tp.getId().contains(nv))
                        .collect(Collectors.toList());

                Platform.runLater(() -> accordion.getPanes().setAll(list));
            }
        });
        
         Platform.runLater(() -> refreshRows());
    }

//    public void setPageHeaderController(PageHeaderController phc) {
//        if (phc == null) {
//            String message = "PageHeaderController should not be null";
//            throw new IllegalArgumentException(message);
//        }
//        phc.setHeaderText("Plugins");
//
//        Platform.runLater(() -> refreshRows());
//    }

    private TitledPane getPluginRow(String name, Node content) {
        URL location = getClass().getResource("/fxml/PluginsListRow.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        TitledPane pane = null;

        try {
            pane = loader.load();
            PluginsListRowController controller = loader.getController();
            controller.setPluginName(name);
            controller.setPluginContent(content);
            if (name.equals("Luminati") || name.equals("GeoSurf")) {
                controller.setRecommended(true);
            }
            controller.setParentAccordion(accordion);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        return pane;
    }

    private void refreshRows() {
        accordion.getPanes().clear();
        accordion.getPanes().add(getPluginRow("IP Teleport", getIPTeleportPane()));
        accordion.getPanes().add(getPluginRow("Luminati", getLuminatiPane()));
        accordion.getPanes().add(getPluginRow("GeoSurf", getGeoSurfPane()));
        accordion.getPanes().add(getPluginRow("POSSH (Proxy Over SSH)", getPosshPane()));
    }

    private Node getIPTeleportPane() {
        return from("/fxml/IPTelePortPane.fxml");
    }

    private Node getLuminatiPane() {
        return from("/fxml/LuminatiPane.fxml");
    }

    private Node getGeoSurfPane() {
        return from("/fxml/GeoSurfPane.fxml");
    }

    private Node getPosshPane() {
        return from("/fxml/PosshPane.fxml");
    }

    private Node from(String path) {
        URL location = getClass().getResource(path);
        FXMLLoader loader = new FXMLLoader(location);
        AnchorPane pane = null;

        try {
            pane = loader.load();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        return pane;
    }

}
