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
public class ProxyNodeContext implements NodeContext<ProfileMenuNode> {

    private static final Logger LOG = Logger.getLogger(ProxyNodeContext.class.getName());
    private static final ProfileMenuNode PROXY_NODE;

    static {
        Node detailsNode = null;
        URL location = ProxyNodeContext.class.getResource("/fxml/ProfileProxy.fxml");

        try {
            detailsNode = FXMLLoader.load(location);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        PROXY_NODE = new ProfileMenuNode(
                "proxy",
                "Proxy",
                detailsNode);
    }

    @Override
    public ProfileMenuNode getNode() {
        return PROXY_NODE;
    }

    @Override
    public void select() {
        GlobalContext.getDefault().set(ProfileMenuNode.class, PROXY_NODE);
    }

}
