/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.api.Stackable;
import com.github.idelstak.manyfacesfx.ui.EditType;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileSummaryController {

    private static final Logger LOG = Logger.getLogger(ProfileSummaryController.class.getName());
    private static final GlobalContext CONTEXT = GlobalContext.getDefault();
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXButton updateButton;
    private final Lookup.Result<EditType> editTypeResult;

    {
        editTypeResult = CONTEXT.lookupResult(EditType.class);
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        refreshUpdateButtonText();

        editTypeResult.addLookupListener(e -> refreshUpdateButtonText());
        cancelButton.setOnAction(e -> showCancelProfileChangeDialog());
    }

    private void refreshUpdateButtonText() {
        Iterator<? extends EditType> it = editTypeResult.allInstances().iterator();

        if (it.hasNext()) {
            EditType editType = it.next();

            switch (editType) {
                case CREATE:
                    setUpdateButtonText("Create");
                    break;
                case UPDATE:
                    setUpdateButtonText("Update");
                    break;
                default:
                    throw new IllegalArgumentException("Edit type not known");
            }
        }
    }

    private void setUpdateButtonText(String text) {
        Platform.runLater(() -> updateButton.setText(text + " profile"));
    }

    private void showCancelProfileChangeDialog() {
        URL location = getClass().getResource("/fxml/CancelProfileEditDialog.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Optional<Region> dialogContent = Optional.empty();

        try {
            dialogContent = Optional.ofNullable(fxmlLoader.load());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        dialogContent.ifPresent(content -> {
            JFXDialog dialog = new JFXDialog();
            dialog.setContent(content);
            
            CancelProfileEditDialogController controller = fxmlLoader.getController();
            controller.setDialog(dialog);
            
            dialog.show(Stackable.getDefault().getStackPane());
        });
    }

}
