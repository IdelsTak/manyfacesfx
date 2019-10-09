/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.jfoenix.controls.JFXToggleNode;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class PluginsListRowController {

    private static final Logger LOG;
    @FXML
    private TitledPane titledPane;
    @FXML
    private HBox titleHbox;
    @FXML
    private AnchorPane pluginContentPane;
    @FXML
    private Label pluginNameLabel;
    @FXML
    private JFXToggleNode openContentToggle;
    @FXML
    private Label recommendedLabel;
    private Label sampleContentLabel;
    private double height;

    static {
        LOG = Logger.getLogger(PluginsListRowController.class.getName());
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        titleHbox.prefWidthProperty().bind(titledPane.widthProperty());
    }

    void setPluginName(String pluginName) {
        String message = "Plugin name should not be null";
        String name = Objects.requireNonNull(pluginName, message);

        pluginNameLabel.setText(name);
        titledPane.setId(name);
    }

    void setPluginContent(Node content) {
        String message = "Content pane should not be null";
        Node kontent = Objects.requireNonNull(content, message);

        pluginContentPane.getChildren().setAll(kontent);

        AnchorPane.setTopAnchor(kontent, 0.0);
        AnchorPane.setRightAnchor(kontent, 0.0);
        AnchorPane.setLeftAnchor(kontent, 0.0);
    }

    void setParentAccordion(Accordion accordion) {
        String message = "Plugins accordion should not be null";
        Accordion akkordion = Objects.requireNonNull(accordion, message);

        openContentToggle.setOnAction(e -> {
            Platform.runLater(() -> {
                boolean selected = openContentToggle.isSelected();
                akkordion.setExpandedPane(selected ? titledPane : null);
            });
        });

        akkordion.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            String className = e.getTarget()
                    .getClass()
                    .getSimpleName();

            LOG.log(Level.FINE, "Accordion event target: {0}", className);

            if (!className.equals("StackPane")
                    && !className.equals("JFXButton")
                    && !className.equals("VBox")
                    && !className.equals("Pane")
                    && !className.equals("LabeledText")
                    && !className.equals("GridPane")) {
                Platform.runLater(() -> akkordion.setExpandedPane(null));
            }
        });

        akkordion.expandedPaneProperty().addListener((o, ov, nv) -> {
            if (nv != null && nv.equals(titledPane)) {
                openContentToggle.setSelected(true);
            } else if (ov != null && ov.equals(titledPane)) {
                openContentToggle.setSelected(false);
            }
        });
    }
    
    void setRecommended(boolean recommended){
        recommendedLabel.setVisible(recommended);
    }
}
