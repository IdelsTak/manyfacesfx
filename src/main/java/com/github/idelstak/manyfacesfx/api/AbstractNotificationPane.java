/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.api;

import java.util.Optional;
import org.controlsfx.control.NotificationPane;
import org.openide.util.Lookup;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public abstract class AbstractNotificationPane {

    private static final AbstractNotificationPane DEFAULT = new BasicPane();
    private static final Lookup LOOKUP = Lookup.getDefault();

    public static AbstractNotificationPane getDefault() {
        return Optional.ofNullable(LOOKUP.lookup(AbstractNotificationPane.class)).orElse(DEFAULT);
    }

    public abstract NotificationPane getNotificationPane();

    public abstract void notify(String message);

    private static class BasicPane extends AbstractNotificationPane {

        private final NotificationPane pane;

        private BasicPane() {
            this.pane = new NotificationPane(Stackable.getDefault().getStackPane());
            this.pane.getStylesheets()
                    .add(getClass()
                            .getResource("/styles/app.css")
                            .toExternalForm());
        }

        @Override
        public NotificationPane getNotificationPane() {
            return pane;
        }

        @Override
        public void notify(String message) {
            pane.show(message);
        }
    }
}
