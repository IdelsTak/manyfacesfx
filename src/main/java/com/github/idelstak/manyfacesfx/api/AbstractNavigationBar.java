/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public abstract class AbstractNavigationBar implements Lookup.Provider {

    private static final Logger LOG = Logger.getLogger(AbstractNavigationBar.class.getName());
    private static final BasicNavigationBar DEFAULT = new BasicNavigationBar();
    private static final Lookup LOOKUP = Lookup.getDefault();

    public static AbstractNavigationBar getDefault() {
        return Optional.ofNullable(LOOKUP.lookup(AbstractNavigationBar.class)).orElse(DEFAULT);
    }

    @Override
    public abstract Lookup getLookup();

    public abstract void setMenuPane(Pane menuPane);

    private static final class BasicNavigationBar extends AbstractNavigationBar {

        private Pane menuPane;
        private final InstanceContent content;
        private final Lookup lookup;

        private BasicNavigationBar() {
            this.content = new InstanceContent();
            this.lookup = new AbstractLookup(content);

            content.add(getNavigationMenu());
        }

        @Override
        public void setMenuPane(Pane menuPane) {
            String message = "Pane should not be null";
            this.menuPane = Objects.requireNonNull(menuPane, message);

            content.set(Arrays.asList(this.menuPane), null);
        }

        @Override
        public Lookup getLookup() {
            return lookup;
        }

        private Pane getNavigationMenu() {
            if (menuPane == null) {
                try {
                    menuPane = FXMLLoader.load(getClass().getResource("/fxml/HomeMenu.fxml"));
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }
            return menuPane;
        }

    }
}
