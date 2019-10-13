/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui;

import javafx.scene.Node;


public class ProfileMenuNode extends MenuNode {

    public ProfileMenuNode(
            String name, 
            String displayName, 
            Node detailsPane) {
        super(name, displayName, false, detailsPane, AppMenu.PROFILE);
    }
    
     @Override
    public String toString() {
        return "[Profile menu node: " + getDisplayName() + ']';
    }
}
