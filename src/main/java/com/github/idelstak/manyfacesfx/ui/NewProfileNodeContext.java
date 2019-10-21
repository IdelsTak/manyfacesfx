/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui;

import com.github.idelstak.manyfacesfx.api.GlobalContext;
import com.github.idelstak.manyfacesfx.model.Profile;
import com.github.javafaker.Faker;
import java.time.LocalDate;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class NewProfileNodeContext extends ProfileAttributesEditContext {

    private static final GlobalContext CONTEXT = GlobalContext.getDefault();

    public NewProfileNodeContext() {
        super();
    }

    @Override
    public void select() {
        String id = new Faker().internet().uuid();
        Profile profile = new Profile(id, "", "", LocalDate.now());

        CONTEXT.set(Profile.class, profile);
        CONTEXT.set(EditType.class, EditType.CREATE);

        super.select();
    }

}
