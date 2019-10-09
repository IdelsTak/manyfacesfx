/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.model;

import com.github.idelstak.manyfacesfx.api.GroupsRepository;
import java.time.LocalDate;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class Profile {

    private static final Logger LOG = Logger.getLogger(Profile.class.getName());
    private final ReadOnlyStringWrapper idProperty;
    private final SimpleStringProperty nameProperty;
    private final SimpleStringProperty notesProperty;
    private final SimpleObjectProperty<LocalDate> lastEditedProperty;
    private final SimpleObjectProperty<Group> groupProperty;

    public Profile(String id, String name, String notes, LocalDate lastEdited) {
        this(
                id,
                name,
                notes,
                lastEdited,
                new FindGroup().find("Unassigned"));
    }

    public Profile(
            String id,
            String name,
            String notes,
            LocalDate lastEdited,
            Group group) {
        this.idProperty = new ReadOnlyStringWrapper(id);
        this.nameProperty = new SimpleStringProperty(name);
        this.notesProperty = new SimpleStringProperty(notes);
        this.lastEditedProperty = new SimpleObjectProperty<>(lastEdited);
        this.groupProperty = new SimpleObjectProperty<>(group);
    }

    public String getId() {
        return idProperty.get();
    }

    public String getName() {
        return nameProperty.get();
    }

    public void setName(String name) {
        nameProperty.set(name);
    }

    public String getNotes() {
        return notesProperty.get();
    }

    public void setNotes(String notes) {
        notesProperty.set(notes);
    }

    public LocalDate getLastEdited() {
        return lastEditedProperty.get();
    }

    public void setLastEdited(LocalDate lastEdited) {
        lastEditedProperty.set(lastEdited);
    }

    public Group getGroup() {
        return groupProperty.get();
    }

    public void setGroup(Group group) {
        groupProperty.set(group);
    }

    public ReadOnlyStringProperty idProperty() {
        return idProperty.getReadOnlyProperty();
    }

    public SimpleStringProperty nameProperty() {
        return nameProperty;
    }

    public SimpleStringProperty notesProperty() {
        return notesProperty;
    }

    public SimpleObjectProperty<LocalDate> lastEditedProperty() {
        return lastEditedProperty;
    }

    public SimpleObjectProperty<Group> groupProperty() {
        return groupProperty;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.idProperty);
        hash = 97 * hash + Objects.hashCode(this.nameProperty);
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
        final Profile other = (Profile) obj;
        if (!Objects.equals(this.idProperty.get(), other.idProperty.get())) {
            return false;
        }
        return Objects.equals(this.nameProperty.get(), other.nameProperty.get());
    }

    @Override
    public String toString() {
        return getName();
    }

    private static class FindGroup {

        private FindGroup() {
        }

        private Group find(String name) {

            Group group = GroupsRepository.getDefault()
                    .findByName(name)
                    .orElseGet(() -> GroupsRepository.getDefault().add(name));

            LOG.log(Level.FINE, "Found group: " + group);

            return group;
        }
    }

}
