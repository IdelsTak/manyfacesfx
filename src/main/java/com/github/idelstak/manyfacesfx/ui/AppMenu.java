/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui;

import java.io.IOException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public enum AppMenu implements Supplier<Pane> {

    HOME("/fxml/HomeMenu.fxml"),
    PROFILE("/fxml/ProfileMenu.fxml");

    private static final Logger LOG = Logger.getLogger(AppMenu.class.getName());
    private final String path;
    private Pane pane;
//    private MenuNode homeNode;

    private AppMenu(String path) {
        this.path = path;
    }

    @Override
    public Pane get() {
        if (pane == null) {
            try {
                pane = FXMLLoader.load(getClass().getResource(path));
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        return pane;
    }

//    public MenuNode getHomeNode() {
//        if (homeNode == null) {
//            Node detailsPane = null;
//
//            try {
//                detailsPane = FXMLLoader.load(getClass().getResource("/fxml/HomeDetails.fxml"));
//            } catch (IOException ex) {
//                LOG.log(Level.SEVERE, null, ex);
//            }
//
//            homeNode = new MenuNode(
//                    "home",
//                    "Home",
//                    true,
//                    detailsPane,
//                    AppMenu.HOME);
//        }
//        return homeNode;
//    }
//
//    public void setHomeAsOnlyContext() {
//        GlobalContext context = GlobalContext.getDefault();
//        MenuNode node = context.lookup(MenuNode.class);
//
//        if (node != null && !Objects.equals(node, getHomeNode())) {
//            context.remove(node);
//        }
//        
//        context.add(getHomeNode());
//    }

}
