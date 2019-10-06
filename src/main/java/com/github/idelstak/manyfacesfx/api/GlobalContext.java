/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.api;

import java.util.Optional;
import java.util.logging.Logger;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public abstract class GlobalContext extends ProxyLookup {

    private static final Logger LOG = Logger.getLogger(GlobalContext.class.getName());
    private static final Lookup LOOKUP = Lookup.getDefault();
    private static final GlobalContext DEFAULT = new SimpleContext();

    protected GlobalContext(Lookup... lookups) {
        super(lookups);
    }

    public static GlobalContext getDefault() {
        return Optional.ofNullable(LOOKUP.lookup(GlobalContext.class)).orElse(DEFAULT);
    }

    public abstract <T> GlobalContext add(T inst);

    public abstract <T> GlobalContext remove(T inst);

    private static final class SimpleContext extends GlobalContext {

        private final InstanceContent content;

        private SimpleContext() {
            this(new InstanceContent());
        }

        private SimpleContext(InstanceContent content) {
            super(new AbstractLookup(content));

            this.content = content;
        }

        @Override
        public synchronized <T> GlobalContext add(T inst) {
            content.add(inst);
            return this;
        }

        @Override
        public synchronized <T> GlobalContext remove(T inst) {
            content.remove(inst);
            return this;
        }

    }

}
