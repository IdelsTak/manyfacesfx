/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.controllers;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.ui.EditType;
import com.jfoenix.controls.JFXButton;
import java.util.Iterator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import org.openide.util.Lookup;

/**
 FXML Controller class

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileSummaryController {

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

}
