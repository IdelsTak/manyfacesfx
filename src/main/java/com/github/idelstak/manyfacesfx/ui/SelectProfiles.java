/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui;

import javafx.beans.property.SimpleBooleanProperty;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class SelectProfiles {

    private final SimpleBooleanProperty selectProperty;
    private final SimpleBooleanProperty visibleProperty;

    public SelectProfiles() {
        this.selectProperty = new SimpleBooleanProperty(false);
        this.visibleProperty = new SimpleBooleanProperty(false);
    }

    public SimpleBooleanProperty selectProperty() {
        return selectProperty;
    }

    public SimpleBooleanProperty visibleProperty() {
        return visibleProperty;
    }

    public boolean isSelected() {
        return selectProperty.get();
    }

    public void setSelected(boolean selected) {
        selectProperty.set(selected);
    }

    public boolean isVisible(){
        return visibleProperty.get();
    }
    
    public void setVisible(boolean visible){
        visibleProperty.set(visible);
    }
}
