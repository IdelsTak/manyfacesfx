/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class FontsDialogController {

    private static final Logger LOG;
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private VBox fontsListBox;
    private String[] fonts;

    static {
        LOG = Logger.getLogger(FontsDialogController.class.getName());
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        searchField.textProperty().addListener((o, ov, nv) -> {
            if (fonts == null) {
                throw new IllegalArgumentException("Fonts may not have been initialized.");
            }

            if (nv == null || nv.trim().isEmpty()) {
                refreshFontsList(fonts);
            } else {
                List<String> filtered = Arrays.stream(fonts)
                        .filter(font -> font.toLowerCase().contains(nv.toLowerCase()))
                        .collect(Collectors.toList());
                String[] fontNames = filtered.toArray(new String[filtered.size()]);
                Platform.runLater(() -> refreshFontsList(fontNames));
            }
        });
    }

    void setDialog(JFXDialog fXDialog) {
        String message = "Dialog should not be null";
        JFXDialog dialog = Objects.requireNonNull(fXDialog, message);

        closeButton.setOnAction(e -> dialog.close());
        cancelButton.setOnAction(e -> dialog.close());
    }

    void setFontFamilies(String[] fontz) {
        String message = "Fonts should not be null";
        if (fontz == null) {
            throw new IllegalArgumentException(message);
        }

        fonts = Arrays.copyOf(fontz, fontz.length);

        refreshFontsList(fonts);
    }

    private void refreshFontsList(String[] fonts) {
        List<Pane> fontPanes = Arrays.stream(fonts)
                .map(font -> getFontPane(font))
                .collect(Collectors.toList());

        fontsListBox.getChildren().setAll(fontPanes);
    }

    private Pane getFontPane(String font) {
        URL location = getClass().getResource("/fxml/FontListCell.fxml");
        FXMLLoader loader = new FXMLLoader(location);

        Pane pane = null;

        try {
            pane = loader.load();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        loader.<FontListCellController>getController().setFontFamilyName(font);

        return pane;
    }

}
