/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui;

import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.controllers.ProfileDetailsController;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public ProfileNode(Profile profile, SelectProfiles selectProfiles) {
        this(profile, selectProfiles, new InstanceContent());
    }

    private ProfileNode(Profile profile, SelectProfiles selectProfiles, InstanceContent content) {
        this.lookup = new AbstractLookup(content);
        this.profile = profile;
        
        content.add(profile);
        content.add(selectProfiles);
        content.add(getPane());
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
        if (!Objects.equals(this.profile, other.profile)) {
            return false;
        }
        return true;
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
