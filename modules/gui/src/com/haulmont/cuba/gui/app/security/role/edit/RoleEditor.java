/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.ScreenPermissionsFrame;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Tabsheet;
import com.haulmont.cuba.security.entity.Role;

import javax.inject.Inject;
import java.util.Map;

public class RoleEditor extends AbstractEditor<Role> {

    @Inject
    private Tabsheet permissionsTabsheet;

    @Inject
    private ScreenPermissionsFrame screensTabFrame;

    @Override
    public void init(Map<String, Object> params) {
        permissionsTabsheet.addListener(new Tabsheet.TabChangeListener() {
            @Override
            public void tabChanged(Tabsheet.Tab newTab) {
                // do not remove, it is needed for lazy initialization
            }
        });
    }

    @Override
    protected void postInit() {
        screensTabFrame.loadPermissions();

        if (!PersistenceHelper.isNew(getItem())) {
            getComponent("name").setEnabled(false);
        }
    }
}