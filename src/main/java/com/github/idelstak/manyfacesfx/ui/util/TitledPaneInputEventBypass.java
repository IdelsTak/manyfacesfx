/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui.util;

import javafx.css.Styleable;
import javafx.event.EventHandler;
import javafx.scene.input.InputEvent;

/**
 Consumes the event if the target is named {@code title}.

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class TitledPaneInputEventBypass implements EventHandler<InputEvent> {

    @Override
    public void handle(InputEvent event) {
        //If the target of the event is the title, ignore it
        //we want to be able to expand the titled pane using
        //our custom button only
        if (((Styleable) event.getTarget())
                .getStyleClass()
                .contains("title")) {
            event.consume();
        }
    }

}
