/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.api;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.openide.util.Lookup;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public abstract class AbstractProfileListDetails {

    private static final Logger LOG = Logger.getLogger(AbstractProfileListDetails.class.getName());
    private static final BasicProfileListDetails DEFAULT = new BasicProfileListDetails();
    private static final Lookup LOOKUP = Lookup.getDefault();

    public static AbstractProfileListDetails getDefault() {
        return Optional.ofNullable(LOOKUP.lookup(AbstractProfileListDetails.class)).orElse(DEFAULT);
    }

    public abstract Pane getPane();

    private static class BasicProfileListDetails extends AbstractProfileListDetails {

        private Pane pane;

        private BasicProfileListDetails() {
            try {
                pane = FXMLLoader.load(getClass().getResource("/fxml/ProfileListDetails.fxml"));
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public Pane getPane() {
            return pane;
        }

    }
}
