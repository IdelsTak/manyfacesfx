/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GroupsRepository;
import com.github.idelstak.manyfacesfx.model.Group;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleNode;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class GroupListDetailsController {

    private static final Logger LOG = Logger.getLogger(GroupListDetailsController.class.getName());
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXToggleNode nameToggle;
    @FXML
    private JFXToggleNode noOfProfilesToggle;
    @FXML
    private Accordion groupListAccordion;

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            GroupsRepository.getDefault()
                    .findAll()
                    .stream()
                    .map(this::getPane)
                    .forEach(o -> o.ifPresent(groupListAccordion.getPanes()::add));
        });
    }

    private Optional<TitledPane> getPane(Group group) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GroupListRow.fxml"));
        TitledPane pane = null;

        try {
            pane = loader.<TitledPane>load();
            GroupListRowController controller = loader.<GroupListRowController>getController();
            controller.setGroup(group);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        return Optional.ofNullable(pane);
    }

}
