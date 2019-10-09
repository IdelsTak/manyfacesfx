/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.api;

import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.javafaker.Ancient;
import com.github.javafaker.DateAndTime;
import com.github.javafaker.Faker;
import com.github.javafaker.Internet;
import com.github.javafaker.Lorem;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.openide.util.Lookup;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public abstract class ProfilesRepository {

    private static final Logger LOG = Logger.getLogger(ProfilesRepository.class.getName());
    private static final Lookup LOOKUP = Lookup.getDefault();
    private static final ProfilesRepository DEFAULT = new BasicProfilesRepository();

    public static ProfilesRepository getDefault() {
        LOG.log(Level.FINE, "Asking for a default profiles repository...");
        ProfilesRepository orElse = Optional.ofNullable(LOOKUP.lookup(ProfilesRepository.class)).orElse(DEFAULT);

        LOG.log(Level.FINE, "========= Found: {0} ==============", orElse);

        return orElse;
    }

    public abstract boolean add(Profile profile);

    public abstract Optional<Profile> create(String name);

    public abstract boolean delete(Profile profile);

    public abstract ObservableSet<Profile> findAll();

    public abstract Optional<Profile> findById(String id);

    private static final class BasicProfilesRepository extends ProfilesRepository {

        private final ObservableSet<Profile> profiles;

        private BasicProfilesRepository() {
            this.profiles = FXCollections.observableSet(new HashSet<>());

            Service<Collection<Profile>> service = new Service<Collection<Profile>>() {
                @Override
                protected Task<Collection<Profile>> createTask() {
                    return new Task<Collection<Profile>>() {
                        @Override
                        protected Collection<Profile> call() throws Exception {
                            Collection<Profile> result = new ArrayList<>();
                            //Add bogus data
                            Internet internet = new Faker().internet();
                            Ancient ancient = new Faker().ancient();
                            DateAndTime dat = new Faker().date();
                            Lorem lorem = new Faker().lorem();

                            for (int i = 0; i < 10; i++) {
                                String id = internet.uuid();
                                String name = ancient.titan();
                                String notes = lorem.paragraph();
                                Date d = dat.past(1000, TimeUnit.DAYS);
                                LocalDate ldt = new java.sql.Timestamp(d.getTime()).toLocalDateTime().toLocalDate();
                                
                                result.add(new Profile(id, name, notes, ldt));
                            }

                            return result;
                        }
                    };
                }
            };

            service.setOnSucceeded(e -> {
                LOG.log(Level.FINE, "++++++++++++succeeded++++++++++++");

                Platform.runLater(() -> {
                    @SuppressWarnings("unchecked")
                    Collection<? extends Profile> value = (Collection<? extends Profile>) e.getSource().getValue();
                    value.forEach(this::add);
                });
            });

            service.start();
        }

        @Override
        public synchronized Optional<Profile> create(String name) {
            String id = new Faker().internet().uuid();
            Profile profile = new Profile(id, name, "", LocalDate.now());

            return profiles.add(profile)
                   ? Optional.of(profile)
                   : Optional.empty();
        }

        @Override
        public synchronized boolean add(Profile profile) {
            LOG.log(Level.FINE, "Adding {0} to list", profile);
            
            return profiles.add(profile);
        }

        @Override
        public synchronized boolean delete(Profile profile) {
            return profiles.remove(profile);
        }

        @Override
        public synchronized Optional<Profile> findById(String id) {
            return profiles.stream()
                    .filter(profile -> Objects.equals(id, profile.getId()))
                    .findFirst();
        }

        @Override
        public synchronized ObservableSet<Profile> findAll() {
            return FXCollections.unmodifiableObservableSet(profiles);
        }

        @Override
        public String toString() {
            return "Inner basic profiles repo";
        }

    }
}
