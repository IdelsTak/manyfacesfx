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
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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
                
                add(new Profile(id, name, notes, ldt));
            }
        }

        @Override
        public Optional<Profile> create(String name) {
            String id = new Faker().internet().uuid();
            Profile profile = new Profile(id, name, "", LocalDate.now());

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
