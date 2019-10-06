/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui;

import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.idelstak.manyfacesfx.ui.controllers.ProfileDetailsController;
import java.io.IOException;
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
    private final Profile profile;
    private final InstanceContent content;
    private final Lookup lookup;

    public ProfileNode(Profile profile) {
        this(profile, new InstanceContent());
    }

    private ProfileNode(Profile profile, InstanceContent content) {
        this.profile = profile;
        this.content = content;
        this.lookup = new AbstractLookup(this.content);
        
        this.content.add(this.profile);
        this.content.add(getPane());
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public String toString() {
        return "ProfileNode{" + "profile=" + profile.getName() + '}';
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
