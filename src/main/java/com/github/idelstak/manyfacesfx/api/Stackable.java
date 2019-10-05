/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.api;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import org.openide.util.Lookup;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public abstract class Stackable {
    private static final Logger LOG = Logger.getLogger(Stackable.class.getName());
    private static final Stackable DEFAULT = new BasicStackable();
    private static final Lookup LOOKUP = Lookup.getDefault();

    public static Stackable getDefault() {
        return Optional.ofNullable(LOOKUP.lookup(Stackable.class)).orElse(DEFAULT);
    }

    public abstract StackPane getStackPane();

    private static class BasicStackable extends Stackable {

        private StackPane stackPane;
        private Exception graphSceneLoadException;

        private BasicStackable() {
            try {
                URL location = getClass().getResource("/fxml/AppView.fxml");
                stackPane = FXMLLoader.<StackPane>load(location);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                graphSceneLoadException = ex;
            }
        }

        @Override
        public StackPane getStackPane() {
            if (stackPane == null) {
                throw new IllegalStateException(graphSceneLoadException);
            }
            
            return stackPane;
        }
    }

}
