/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.api;

import java.util.Optional;
import java.util.logging.Logger;
import org.openide.util.Lookup;
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

//    @Override
//    protected void beforeLookup(Template<?> template) {
//        super.beforeLookup(template);
//        
//        LOG.log(Level.INFO, "Before lookup: {0}", template);
//    }
    private static class SimpleContext extends GlobalContext {

        private SimpleContext() {
            super(Lookup.EMPTY);
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
        public synchronized GlobalContext addLookup(Lookup lookup) {
            Lookup[] newLookup = null;
            Lookup[] currentLookup = getLookups();

            if ((currentLookup != null) && (currentLookup.length > 0)) {
                newLookup = new Lookup[currentLookup.length + 1];
                for (int i = newLookup.length - 2; i >= 0; i--) {
                    newLookup[i] = currentLookup[i];
                }
                newLookup[currentLookup.length] = lookup;
            } else {
                newLookup = new Lookup[]{lookup};
            }

            if (newLookup != null) {
                setLookups(newLookup);
            }

            return this;
        }

        @Override
        public synchronized GlobalContext removeLookup(Lookup lookup) {
            Lookup[] currentLookup = getLookups();
            if ((currentLookup != null) && (currentLookup.length > 0)) {
                int removedIndex = -1;
                for (int i = currentLookup.length - 1; i >= 0; i--) {
                    if (currentLookup[i].equals(lookup)) {
                        removedIndex = i;
                        break;
                    }
                }
                if (removedIndex > 0) {
                    Lookup[] newLookup = new Lookup[currentLookup.length - 1];
                    int newIndex = 0;
                    for (int i = currentLookup.length - 1; i >= 0; i--) {
                        if (i != removedIndex) {
                            newLookup[newIndex] = currentLookup[i];
                            newIndex++;
                        }
                    }
                    if (newLookup != null) {
                        setLookups(newLookup);
                    }
                }
            }
            return this;
        }

        @Override
        public synchronized GlobalContext resetLookup() {
            setLookups(new Lookup[]{});
            return this;
        }

    }

}
