/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import java.util.Objects;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ChooseOSDialogController {

    private static final Logger LOG;
    @FXML
    private JFXCheckBox windowsCheckBox;
    @FXML
    private JFXCheckBox linuxCheckBox;
    @FXML
    private JFXCheckBox macCheckBox;
    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    private final ObservableList<JFXCheckBox> checkBoxes;
    private JFXDialog dialog;

    static {
        LOG = Logger.getLogger(ChooseOSDialogController.class.getName());
    }

    {
        checkBoxes = FXCollections.observableArrayList();
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        checkBoxes.addAll(windowsCheckBox, linuxCheckBox, macCheckBox);

        checkBoxes.forEach(cb -> cb.setOnAction(e -> {
            deselectApartFrom((JFXCheckBox) e.getSource());
        }));

        windowsCheckBox.setId("Windows");
        linuxCheckBox.setId("Linux");
        macCheckBox.setId("Mac OS");
    }

    void setDialog(JFXDialog fXDialog) {
        String message = "Dialog should not be null";
        dialog = Objects.requireNonNull(fXDialog, message);

        closeButton.setOnAction(e -> dialog.close());
        cancelButton.setOnAction(e -> dialog.close());
    }

    void setOSLabel(Label osLabel) {
        if (dialog == null) {
            throw new IllegalArgumentException("Dialog has not been set yet");
        }

        checkBoxes.stream()
                .filter(cb -> cb.getId().equals(osLabel.getText()))
                .findFirst()
                .ifPresent(cb -> Platform.runLater(() -> cb.setSelected(true)));

        saveButton.setOnAction(e -> {
            checkBoxes.stream()
                    .filter(cb -> cb.isSelected())
                    .findFirst()
                    .ifPresent(cb -> {
                        dialog.close();
                        Platform.runLater(() -> osLabel.setText(cb.getId()));
                    });
        });
    }

    private void deselectApartFrom(JFXCheckBox checkBox) {
        if (!checkBox.isSelected()) {
            Platform.runLater(() -> checkBox.setSelected(true));
        }

        checkBoxes.stream()
                .filter(cb -> !cb.equals(checkBox))
                .forEach(cb -> cb.setSelected(false));
    }

}
