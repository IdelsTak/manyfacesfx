/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.spi.NodeContext;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class HomeNodeContext implements NodeContext<MenuNode> {

    private static final Logger LOG = Logger.getLogger(HomeNodeContext.class.getName());
    private static final MenuNode HOME_NODE;

    static {
        Node detailsPane = null;
        URL location = HomeNodeContext.class.getResource("/fxml/HomeDetails.fxml");

        try {
            detailsPane = FXMLLoader.load(location);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        HOME_NODE = new MenuNode(
                "home",
                "Home",
                true,
                detailsPane,
                AppMenu.HOME);
    }

    /**
     Default constructor.
     */
    public HomeNodeContext() {
    }

    @Override
    public MenuNode getNode() {
        return HOME_NODE;
    }

    @Override
    public void select() {
        GlobalContext.getDefault().set(MenuNode.class, HOME_NODE);
    }
}
