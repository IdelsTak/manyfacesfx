/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.SelectProfiles;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import java.util.Iterator;
import java.util.Objects;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
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
    private final Lookup.Result<SelectProfiles> lookupResult;

    {
        lookupResult = GlobalContext.getDefault().lookupResult(SelectProfiles.class);
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        titleBox.prefWidthProperty().bind(titlePane.widthProperty());
    }

    public void setProfile(Profile inst) {
        String message = "Profile should not be null";
        Profile profile = Objects.requireNonNull(inst, message);

        nameLabel.textProperty().bind(profile.nameProperty());
        idLabel.textProperty().bind(profile.idProperty());

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

                sp.selectProperty().addListener((ob, ov, nv) -> {
                    selectCheckBox.setSelected(nv);
                });
            }
        });
    }

}
