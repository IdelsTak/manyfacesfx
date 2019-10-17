/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.model;

import com.github.idelstak.manyfacesfx.api.ProfilesRepository;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class Group {

    private static final Logger LOG = Logger.getLogger(Group.class.getName());
    private static final ProfilesRepository PROFILES_REPO = ProfilesRepository.getDefault();
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
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(getId());
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
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return getName() + " (" + getNumberOfProfiles() + ")";
    }

    private SimpleIntegerProperty getNumberOfProfilesProperty() {
        if (numberOfProfilesProperty == null) {
            numberOfProfilesProperty = new SimpleIntegerProperty();

            numberOfProfilesProperty.bind(Bindings.createIntegerBinding(
                    new ProfilesCount(this),
                    PROFILES_REPO.findAll()));
        }
        return numberOfProfilesProperty;
    }

    private static class ProfilesCount implements Callable<Integer> {

        private final Group group;

        private ProfilesCount(Group group) {
            this.group = group;
        }

        @Override
        public Integer call() throws Exception {
            long count = PROFILES_REPO.findAll()
                    .stream()
                    .map(Profile::getGroup)
                    .filter(this::equalTo)
                    .count();
            return Integer.parseInt(Long.toString(count));
        }

        public boolean equalTo(Group otherGroup) {
            return Objects.equals(group, otherGroup);
        }
    }

}
