/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.ProfileNode;
import com.github.idelstak.manyfacesfx.ui.SelectProfiles;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import java.time.LocalDate;
import java.time.format.FormatStyle;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Callable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.util.converter.LocalDateStringConverter;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileDetailsController {

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
    private final Lookup.Result<SelectProfiles> lookupResult;

    {
        lookupResult = GlobalContext.getDefault().lookupResult(SelectProfiles.class);
    }

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
        Profile profile = node.getLookup().lookup(Profile.class);

        titlePane.setId(profile.getName() + ":" + profile.getLastEdited());

        nameLabel.textProperty().bind(profile.nameProperty());
        idLabel.textProperty().bind(profile.idProperty());
        notesTextArea.textProperty().bindBidirectional(profile.notesProperty());
        
        StringBinding lastEditedStringProp = Bindings.createStringBinding(
                new LastEditedAsString(profile.getLastEdited()),
                profile.lastEditedProperty());
        lastEditedLabel.textProperty().bind(lastEditedStringProp);

        lookupResult.addLookupListener(e -> {
            Iterator<? extends SelectProfiles> it = lookupResult.allInstances().iterator();

            if (it.hasNext()) {
                SelectProfiles sp = it.next();

                selectCheckBoxPane.prefWidthProperty().bind(
                        Bindings.createDoubleBinding(
                                () -> sp.isVisible()
                                      ? 25.0
                                      : 0.0,
                                sp.visibleProperty()));

                sp.selectProperty().addListener((ob, ov, selected) -> {
                    selectCheckBox.setSelected(selected);
                });
            }
        });

        selectCheckBox.selectedProperty().addListener((ob, ov, selected) -> {
            GlobalContext context = GlobalContext.getDefault();

            if (selected) {
                context.add(node);
            } else {
                context.remove(node);
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
