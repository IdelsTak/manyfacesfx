/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.ui.util.TitledPaneInputEventBypass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleNode;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.HBox;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileListDetailsController  {

    @FXML
    private TitledPane actionsPane;
    @FXML
    private HBox actionsBox;
    @FXML
    private HBox searchPane;
    @FXML
    private JFXTextField searchField;
    @FXML
    private HBox spacerBox;
    @FXML
    private JFXToggleNode showActionsToggle;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private HBox headersBox;
    @FXML
    private Accordion profileListAccordion;

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        actionsPane.addEventFilter(InputEvent.ANY, new TitledPaneInputEventBypass());
        actionsPane.expandedProperty().bind(showActionsToggle.selectedProperty());
    }    
    
}