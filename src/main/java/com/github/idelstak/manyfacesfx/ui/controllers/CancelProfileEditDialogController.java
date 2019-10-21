/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.ui.EditType;
import com.github.idelstak.manyfacesfx.ui.HomeNodeContext;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import java.util.Iterator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class CancelProfileEditDialogController {

    @FXML
    private JFXButton closeButton;
    @FXML
    private Text messageText;
    @FXML
    private JFXButton yesButton;
    @FXML
    private JFXButton noButton;
    private final Lookup.Result<EditType> editTypeResult;

    {
        editTypeResult = GlobalContext.getDefault().lookupResult(EditType.class);
    }

    /**
     Initializes the controller class.
     */
    @FXML
    public void initialize() {
        // TODO
    }

    void setDialog(JFXDialog dialog) {
        if (dialog == null) {
            throw new IllegalArgumentException("Dialog should not be null");
        }

        noButton.setOnAction(e -> dialog.close());
        closeButton.setOnAction(e -> dialog.close());
        yesButton.setOnAction(e -> {
            dialog.close();
            Platform.runLater(() -> new HomeNodeContext().select());
        });

        updateMessageText();
    }

    private void updateMessageText() throws IllegalArgumentException {
        Iterator<? extends EditType> it = editTypeResult.allInstances().iterator();

        if (it.hasNext()) {
            EditType nextEditType = it.next();

            switch (nextEditType) {
                case CREATE:
                    setMessageText("creating");
                    break;
                case UPDATE:
                    setMessageText("editing");
                    break;
                default:
                    throw new IllegalArgumentException("Edit type not known");
            }
        }
    }

    private void setMessageText(String text) {
        Platform.runLater(() -> {
            messageText.setText("Do you really want to cancel " + text + " this profile?");
        });
    }

}
