/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class Profile {

    private final ReadOnlyStringWrapper idProperty;
    private final SimpleStringProperty nameProperty;
    private final SimpleStringProperty notesProperty;
    private final SimpleObjectProperty<LocalDate> lastEditedProperty;

    public Profile(String id, String name, String notes, LocalDate lastEdited) {
        this.idProperty = new ReadOnlyStringWrapper(id);
        this.nameProperty = new SimpleStringProperty(name);
        this.notesProperty = new SimpleStringProperty(notes);
        this.lastEditedProperty = new SimpleObjectProperty<>(lastEdited);
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

}
