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
public class GeolocationNodeContext implements NodeContext<ProfileMenuNode> {

    private static final Logger LOG = Logger.getLogger(GeolocationNodeContext.class.getName());
    private static final ProfileMenuNode NODE;

    static {
        Node detailsNode = null;
        URL location = GeolocationNodeContext.class.getResource("/fxml/ProfileGeolocation.fxml");

        try {
            detailsNode = FXMLLoader.load(location);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        NODE = new ProfileMenuNode(
                "geolocation",
                "Geolocation",
                detailsNode);
    }

    @Override
    public ProfileMenuNode getNode() {
        return NODE;
    }

    @Override
    public void select() {
        GlobalContext.getDefault().set(ProfileMenuNode.class, NODE);
    }

}
