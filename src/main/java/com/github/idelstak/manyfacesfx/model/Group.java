/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.model;

import com.github.idelstak.manyfacesfx.api.ProfilesRepository;
import java.util.Objects;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableSet;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class Group {

    private static final Logger LOG = Logger.getLogger(Group.class.getName());
    private static final ProfilesRepository PROFILES_REPOSITORY = ProfilesRepository.getDefault();
    private final SimpleStringProperty nameProperty;
    private final ReadOnlyIntegerWrapper idProperty;
    private SimpleIntegerProperty numberOfProfilesProperty;

    public Group(int id, String name) {
        this.idProperty = new ReadOnlyIntegerWrapper(id);
        this.nameProperty = new SimpleStringProperty(name);
    }

    public ReadOnlyIntegerProperty idProperty() {
        return idProperty.getReadOnlyProperty();
    }

    public SimpleStringProperty nameProperty() {
        return nameProperty;
    }

    public SimpleIntegerProperty numberOfProfilesProperty() {
        return getNumberOfProfilesProperty();
    }

    public int getId() {
        return idProperty.get();
    }

    public String getName() {
        return nameProperty.get();
    }

    public int getNumberOfProfiles() {
        return getNumberOfProfilesProperty().get();
    }

    public void setName(String groupName) {
        nameProperty.set(groupName);
    }

    /**
     @return the numberOfProfilesProperty
     */
    private SimpleIntegerProperty getNumberOfProfilesProperty() {
        if (numberOfProfilesProperty == null) {
            numberOfProfilesProperty = new SimpleIntegerProperty();
            ObservableSet<Profile> profiles = PROFILES_REPOSITORY.findAll();

            numberOfProfilesProperty.bind(Bindings.createIntegerBinding(()
                    -> Integer.parseInt(
                            Long.toString(
                                    profiles.stream()
                                            .map(p -> p.getGroup())
                                            .filter(otherGroup -> Objects.equals(Group.this, otherGroup))
                                            .count())),
                    profiles));
        }
        return numberOfProfilesProperty;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + getId();
        hash = 97 * hash + Objects.hashCode(getName());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Group other = (Group) obj;
        if (getId() != other.getId()) {
            return false;
        }
        return Objects.equals(getName(), other.getName());
    }

    @Override
    public String toString() {
        return getName() + " (" + getNumberOfProfiles() + ")";
    }

}
