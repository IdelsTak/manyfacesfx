/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.api;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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

    public abstract GlobalContext setLookupz(Lookup... lookups);

    public abstract GlobalContext setLookup(Lookup lookup);

    public abstract GlobalContext addLookup(Lookup lookup);

    public abstract GlobalContext removeLookup(Lookup lookup);

    public abstract GlobalContext resetLookup();

    public abstract <T> GlobalContext add(T inst);

    public abstract <T> GlobalContext remove(T inst);

//    @Override
//    protected void beforeLookup(Template<?> template) {
//        super.beforeLookup(template);
//        
//        LOG.log(Level.INFO, "Before lookup: {0}", template);
//    }
    private static final class SimpleContext extends GlobalContext {

        private final InstanceContent content;
        private final Set<Lookup> localLookups = new HashSet<>();

        private SimpleContext() {
            this(new InstanceContent());
        }

        private SimpleContext(InstanceContent content) {
            super(Lookup.EMPTY);

            this.content = content;

            addLookup(new AbstractLookup(content));
        }

        @Override
        public synchronized GlobalContext setLookupz(Lookup... lookups) {
            setLookups(lookups);
            return this;
        }

        @Override
        public synchronized GlobalContext setLookup(Lookup lookup) {
            resetLookup();
            addLookup(lookup);
            return this;
        }

        @Override
        public synchronized GlobalContext addLookup(Lookup lkp) {
            String message = "Should not add a null lookup";
            Lookup lookup = Objects.requireNonNull(lkp, message);

            if (localLookups.add(lookup)) {
                setLookups(localLookups.toArray(new Lookup[localLookups.size()]));
            }

            return this;
        }

        @Override
        public synchronized GlobalContext removeLookup(Lookup lkp) {
            String message = "Should not remove a null lookup";
            Lookup lookup = Objects.requireNonNull(lkp, message);

            if (localLookups.remove(lookup)) {
                setLookups(localLookups.toArray(new Lookup[localLookups.size()]));
            }

            return this;
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

        @Override
        public synchronized GlobalContext resetLookup() {
            setLookups(new Lookup[]{});
            return this;
        }

    }

}
