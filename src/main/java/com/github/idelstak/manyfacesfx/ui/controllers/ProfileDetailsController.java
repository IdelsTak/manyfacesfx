/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.api.ProfilesRepository;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.ProfileNode;
import com.github.idelstak.manyfacesfx.ui.SelectProfiles;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import java.time.LocalDate;
import java.time.format.FormatStyle;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.util.converter.LocalDateStringConverter;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileDetailsController {

    private static final Logger LOG = Logger.getLogger(ProfileDetailsController.class.getName());
    private static final GlobalContext CONTEXT = GlobalContext.getDefault();

    @FXML
    private TitledPane titlePane;
    @FXML
    private HBox titleBox;
    @FXML
    private HBox selectCheckBoxPane;
    @FXML
    private JFXCheckBox selectCheckBox;
    @FXML
    private Label nameLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label membersLabel;
    @FXML
    private Label lastEditedLabel;
    @FXML
    private HBox actionBox;
    @FXML
    private JFXButton actionButton;
    @FXML
    private HBox menuBox;
    @FXML
    private JFXButton menuButton;
    @FXML
    private Label idLabel;
    @FXML
    private JFXTextArea notesTextArea;
    private SelectProfiles selectProfiles;
    private Profile profile;

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        titleBox.minWidthProperty().bind(titlePane.widthProperty());
    }

    public void setProfileNode(ProfileNode profileNode) {
        String message = "Profile node should not be null";
        ProfileNode node = Objects.requireNonNull(profileNode, message);

        message = "Profile should not be null";
        profile = Objects.requireNonNull(node.getLookup().lookup(Profile.class), message);

        titlePane.idProperty()
                .bind(Bindings.createStringBinding(
                        () -> profile.getName() + ":" + profile.getLastEdited(),
                        new Observable[]{
                            profile.nameProperty(),
                            profile.lastEditedProperty()}));

        nameLabel.textProperty().bind(profile.nameProperty());
        idLabel.textProperty().bind(profile.idProperty());
        notesTextArea.textProperty().bindBidirectional(profile.notesProperty());
        lastEditedLabel.textProperty().bind(Bindings.createStringBinding(
                new LastEditedAsString(profile.getLastEdited()),
                profile.lastEditedProperty()));

        message = "Select profiles should not be null";
        selectProfiles = Objects.requireNonNull(node.getLookup().lookup(SelectProfiles.class), message);

        updateSelection();

        selectCheckBox.selectedProperty().addListener((ob, ov, selected) -> {
            if (selected) {
                CONTEXT.add(node);
            } else {
                CONTEXT.remove(node);
            }
        });
    }

    private void updateSelection() {
        Callable<Double> widthCalculator = () -> selectProfiles.isVisible()
                                                 ? 25.0
                                                 : 0.0;
        DoubleBinding widthBinding = Bindings.createDoubleBinding(
                widthCalculator,
                selectProfiles.visibleProperty());

        selectCheckBoxPane.prefWidthProperty().bind(widthBinding);
        selectProfiles.selectProperty().addListener((ob, ov, nv) -> {
            Platform.runLater(() -> selectCheckBox.setSelected(false));
            
            if (profile != null) {
                ProfilesRepository.getDefault()
                        .findById(profile.getId())
                        .ifPresent(p -> Platform.runLater(() -> selectCheckBox.setSelected(nv)));
            }
        });
    }

    private static class LastEditedAsString implements Callable<String> {

        private final LocalDate lastEdited;

        private LastEditedAsString(LocalDate lastEdited) {
            this.lastEdited = lastEdited;
        }

        @Override
        public String call() throws Exception {
            LocalDateStringConverter ldsc = new LocalDateStringConverter(FormatStyle.SHORT);
            return ldsc.toString(lastEdited);
        }
    }

}
