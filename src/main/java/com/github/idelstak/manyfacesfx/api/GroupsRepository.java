/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.api;

import com.github.idelstak.manyfacesfx.model.Group;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.openide.util.Lookup;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public abstract class GroupsRepository {

    private static final Lookup LOOKUP = Lookup.getDefault();
    private static final GroupsRepository DEFAULT = new SimpleGroupsRepository();

    public static GroupsRepository getDefault() {
        return Optional.ofNullable(LOOKUP.lookup(GroupsRepository.class)).orElse(DEFAULT);
    }

    public abstract Group add(String groupName);

    public abstract void update(Group group);

    public abstract void updateWithPosition(int index, Group group);

    public abstract Optional<Group> findbyId(int id);

    public abstract Optional<Group> findByName(String name);

    public abstract ObservableList<Group> findAll();

    public abstract void delete(Group group);

    private static class SimpleGroupsRepository extends GroupsRepository {

        private static final Logger LOG;
        private final ObservableList<Group> groups;
        private int id;

        static {
            LOG = Logger.getLogger(SimpleGroupsRepository.class.getName());
        }

        private SimpleGroupsRepository() {
            this.groups = FXCollections.observableArrayList();

            groups.addListener((ListChangeListener.Change<? extends Group> change) -> {
                LOG.log(Level.INFO, "Groups list change occured: {0}", change);
            });
        }

        @Override
        public synchronized Group add(String groupName) {
            LOG.log(Level.INFO, "Adding group with name: {0}", groupName);

            id += 1;
            Group newGroup = new Group(id, groupName);
            groups.add(newGroup);
            return newGroup;
        }

        @Override
        public synchronized void update(Group group) {
            groups.stream()
                    .filter(g -> g.getId() == group.getId())
                    .findFirst()
                    .ifPresent(g -> {
                        int thisId = g.getId();
                        int thisIdx = groups.indexOf(g);
                        String otherName = group.getName();

                        if (groups.remove(g)) {
                            groups.add(thisIdx, new Group(thisId, otherName));
                        }
                    });
        }

        @Override
        public synchronized void updateWithPosition(int index, Group group) {
            groups.stream()
                    .filter(g -> g.getId() == group.getId())
                    .findFirst()
                    .ifPresent(g -> {
                        if (groups.remove(g)) {
                            groups.add(index, new Group(g.getId(), group.getName()));
                        }
                    });
        }

        @Override
        public synchronized Optional<Group> findbyId(int id) {
            return groups.stream()
                    .filter(g -> g.getId() == id)
                    .findFirst();
        }

        @Override
        public synchronized Optional<Group> findByName(String name) {
            return groups.stream()
                    .filter(group -> name == null
                                     ? group.getName() == null
                                     : group.getName().equals(name))
                    .findFirst();
        }

        @Override
        public synchronized ObservableList<Group> findAll() {
            return FXCollections.unmodifiableObservableList(groups);
        }

        @Override
        public synchronized void delete(Group group) {
            groups.remove(group);
        }

        @Override
        public String toString() {
            return "Simple groups repo";
        }

    }

}