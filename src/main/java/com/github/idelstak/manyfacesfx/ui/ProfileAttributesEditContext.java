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
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public abstract class ProfileAttributesEditContext implements NodeContext<MenuNode> {

    public static final String NAME = "new profile";
    private static final Logger LOG = Logger.getLogger(EditProfileNodeContext.class.getName());
    private static final MenuNode NODE;
    private static final GlobalContext CONTEXT = GlobalContext.getDefault();

    static {
        Node detailsPane = null;
        URL location = EditProfileNodeContext.class.getResource("/fxml/ProfileAttributes.fxml");

        try {
            detailsPane = FXMLLoader.load(location);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        NODE = new MenuNode(
                NAME,
                "New browser profile",
                false,
                detailsPane,
                AppMenu.PROFILE);
    }

    protected ProfileAttributesEditContext() {
    }

    @Override
    public MenuNode getNode() {
        return NODE;
    }

    @Override
    public void select() {
        CONTEXT.set(MenuNode.class, NODE);

        Platform.runLater(() -> new OverViewNodeContext().select());
    }

}
