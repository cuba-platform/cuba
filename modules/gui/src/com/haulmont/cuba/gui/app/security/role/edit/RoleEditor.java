/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.ScreenPermissionsFrame;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.security.entity.Role;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author abramov
 * @version $Id$
 */
public class RoleEditor extends AbstractEditor<Role> {

    @Inject
    protected ScreenPermissionsFrame screensTabFrame;

    @Named("name")
    protected TextField nameField;

    @Inject
    protected TextField locName;

    @Override
    protected void postInit() {
        setCaption(PersistenceHelper.isNew(getItem()) ?
                getMessage("createCaption") : formatMessage("editCaption", getItem().getName()));

        screensTabFrame.loadPermissions();
    }
}