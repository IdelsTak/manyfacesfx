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

    public abstract <T> void replace(Class<? extends T> type, T inst);

    public abstract <T> void set(Class<? extends T> type, T inst);

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
            lookupAll(inst.getClass())
                    .stream()
                    .filter(t -> t.equals(inst))
                    .forEach(content::remove);
            
            content.add(inst);
            
            return this;
        }

        @Override
        public synchronized <T> GlobalContext remove(T inst) {
            content.remove(inst);
            return this;
        }

        @Override
        public synchronized <T> void replace(Class<? extends T> type, T inst) {
            lookupAll(type).stream()
                    .filter(t -> !t.equals(inst))
                    .forEach(content::remove);

            //Check against the new lookup list
            if (lookupAll(type).isEmpty()) {
                content.add(inst);
            }
        }

        @Override
        public synchronized <T> void set(Class<? extends T> type, T inst) {
            lookupAll(type).stream().forEach(content::remove);

            //Check against the new lookup list
            if (lookupAll(type).isEmpty()) {
                content.add(inst);
            }
        }

    }

}
