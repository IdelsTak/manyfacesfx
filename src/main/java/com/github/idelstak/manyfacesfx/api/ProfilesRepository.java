/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.api;

import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.javafaker.Ancient;
import com.github.javafaker.Faker;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.openide.util.Lookup;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public abstract class ProfilesRepository {

    private static final Lookup LOOKUP = Lookup.getDefault();
    private static final BasicProfilesRepository DEFAULT = new BasicProfilesRepository();

    public static ProfilesRepository getDefault() {
        return Optional.ofNullable(LOOKUP.lookup(ProfilesRepository.class)).orElse(DEFAULT);
    }

    public abstract boolean add(Profile profile);

    public abstract Optional<Profile> create(String name);

    public abstract boolean delete(Profile profile);

    public abstract ObservableSet<Profile> findAll();

    public abstract Optional<Profile> findById(String id);

    private static class BasicProfilesRepository extends ProfilesRepository {

        private final ObservableSet<Profile> profiles;

        private BasicProfilesRepository() {
            this.profiles = FXCollections.observableSet(new HashSet<>());

            //Add bogus data
            Ancient ancient = new Faker().ancient();
            for (int i = 0; i < 10; i++) {
                create(ancient.titan());
            }
        }

        @Override
        public Optional<Profile> create(String name) {
            String id = new Faker().internet().uuid();
            Profile profile = new Profile(id, name, "", LocalDateTime.now());

            return profiles.add(profile)
                   ? Optional.of(profile)
                   : Optional.empty();
        }

        @Override
        public boolean add(Profile profile) {
            return profiles.add(profile);
        }

        @Override
        public boolean delete(Profile profile) {
            return profiles.remove(profile);
        }

        @Override
        public Optional<Profile> findById(String id) {
            return profiles.stream()
                    .filter(profile -> Objects.equals(id, profile.getId()))
                    .findFirst();
        }

        @Override
        public ObservableSet<Profile> findAll() {
            return FXCollections.unmodifiableObservableSet(profiles);
        }
    }
}
