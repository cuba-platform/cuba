/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.ScreenPermissionsFrame;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Tabsheet;

import javax.inject.Inject;
import java.util.Map;

public class RoleEditor extends AbstractEditor {

    @Inject
    private Tabsheet permissionsTabsheet;

    @Inject
    private ScreenPermissionsFrame screensTabFrame;

    @Override
    public void setItem(Entity item) {
        super.setItem(item);

        screensTabFrame.loadPermissions();

        if (!PersistenceHelper.isNew(item)) {
            getComponent("name").setEnabled(false);
        }
    }

    @Override
    public void init(Map<String, Object> params) {
        permissionsTabsheet.addListener(new Tabsheet.TabChangeListener() {
            @Override
            public void tabChanged(Tabsheet.Tab newTab) {
                // do not remove, it needs for lazy initialization
            }
        });
    }
}