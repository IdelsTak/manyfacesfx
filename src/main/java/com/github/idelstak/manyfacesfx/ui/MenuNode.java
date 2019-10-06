/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class MenuNode implements Lookup.Provider {

    private final InstanceContent content;
    private final Lookup lookup;
    private final SimpleStringProperty displayNameProperty;
    private final boolean showsNotifications;
    private final Node detailsPane;
    private final String name;

    public MenuNode(String name, String displayName, boolean showsNotifications, Node detailsPane) {
        this.content = new InstanceContent();
        this.lookup = new AbstractLookup(content);
        this.name = name;
        this.displayNameProperty = new SimpleStringProperty(displayName);
        this.showsNotifications = showsNotifications;
        this.detailsPane = detailsPane;
    }

    public SimpleStringProperty displayNameProperty() {
        return displayNameProperty;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayNameProperty.get();
    }

    public void setDisplayName(String displayName) {
        displayNameProperty.set(displayName);
    }

    public boolean showsNotifications() {
        return showsNotifications;
    }

    public Node getDetailsPane() {
        return detailsPane;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public String toString() {
        return displayNameProperty.get();
    }

}
