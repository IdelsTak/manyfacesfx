/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui;

import com.github.idelstak.manyfacesfx.api.ProfilesRepository;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.controllers.ProfileDetailsController;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class ProfileNode implements Lookup.Provider {

    private static final Logger LOG = Logger.getLogger(ProfileNode.class.getName());
    private final Lookup lookup;
    private final Profile profile;

    public ProfileNode(Profile profile, BulkProfilesSelect selectProfiles) {
        this(profile, selectProfiles, new InstanceContent());
    }

    private ProfileNode(Profile profile, BulkProfilesSelect selectProfiles, InstanceContent content) {
        this.lookup = new AbstractLookup(content);
        this.profile = profile;

        content.add(profile);
        content.add(selectProfiles);

        TitledPane pane = getPane();
        content.add(pane);

        ProfilesRepository.getDefault()
                .addListener((SetChangeListener.Change<? extends Profile> change) -> {
                    if (change.wasRemoved() && Objects.equals(profile, change.getElementRemoved())) {
                        content.remove(profile);
                        content.remove(selectProfiles);
                        content.remove(pane);
                    }
                });
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.profile);
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
        final ProfileNode other = (ProfileNode) obj;
        return Objects.equals(this.profile, other.profile);
    }

    @Override
    public String toString() {
        return "ProfileNode{"
                + "profile="
                + lookup.lookup(Profile.class).getName()
                + '}';
    }

    private TitledPane getPane() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProfileDetails.fxml"));
        TitledPane pane = null;

        try {
            pane = loader.<TitledPane>load();
            ProfileDetailsController controller = loader.<ProfileDetailsController>getController();

            controller.setProfileNode(this);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        return pane;
    }

}
