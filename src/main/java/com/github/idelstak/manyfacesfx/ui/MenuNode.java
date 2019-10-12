/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class MenuNode {

    private final SimpleStringProperty displayNameProperty;
    private final boolean showsNotifications;
    private final Node detailsPane;
    private final String name;
    private final AppMenu menu;

    public MenuNode(
            String name,
            String displayName,
            boolean showsNotifications,
            Node detailsPane,
            AppMenu menu) {
        this.name = name;
        this.displayNameProperty = new SimpleStringProperty(displayName);
        this.showsNotifications = showsNotifications;
        this.detailsPane = detailsPane;
        this.menu = menu;
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

    public Pane getMenuPane() {
        return menu.get();
    }

    public AppMenu getMenu() {
        return menu;
    }

    @Override
    public String toString() {
        return "[Menu node: " + displayNameProperty.get() + ']';
    }

}
