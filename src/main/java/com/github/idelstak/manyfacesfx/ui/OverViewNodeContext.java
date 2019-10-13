/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui;

import com.github.idelstak.manyfacesfx.spi.NodeContext;
import com.github.idelstak.manyfacesfx.api.GlobalContext;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class OverViewNodeContext implements NodeContext<ProfileMenuNode> {

    private static final Logger LOG = Logger.getLogger(OverViewNodeContext.class.getName());
    private static final ProfileMenuNode OVERVIEW_NODE;

    static {
        Node detailsPane = null;
        URL location = OverViewNodeContext.class.getResource("/fxml/ProfileOverview.fxml");

        try {
            detailsPane = FXMLLoader.load(location);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        OVERVIEW_NODE = new ProfileMenuNode(
                "overview",
                "Overview",
                detailsPane);
    }

    /**
     Default constructor.
     */
    public OverViewNodeContext() {
    }

    @Override
    public ProfileMenuNode getNode() {
        return OVERVIEW_NODE;
    }

    @Override
    public void refreshContext() {
        GlobalContext.getDefault().set(ProfileMenuNode.class, OVERVIEW_NODE);
    }

}
