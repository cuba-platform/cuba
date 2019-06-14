/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.ScreenPermissionsFrame;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.Role;

import javax.inject.Inject;

public class RoleEditor extends AbstractEditor<Role> {
    @Inject
    protected ScreenPermissionsFrame screensTabFrame;
    @Inject
    protected EntityStates entityStates;

    @Inject
    protected Datasource<Role> roleDs;

    @Inject
    protected Button windowCommitAndClose;

    @Inject
    protected Button windowCommit;

    @Inject
    protected TextField name;

    @Inject
    protected LookupField typeLookup;

    @Inject
    protected TextArea description;

    @Inject
    protected TextField locName;

    @Inject
    protected CheckBox defaultRole;

    @Override
    protected void postInit() {
        setCaption(entityStates.isNew(getItem()) && !getItem().isPredefined() ?
                getMessage("createCaption") : formatMessage("editCaption", getItem().getName()));

        screensTabFrame.loadPermissions();

        if (getItem().isPredefined()) {
            restrictAccessForPredefinedRole();
        }
    }

    protected void restrictAccessForPredefinedRole() {
        windowCommit.setVisible(false);
        windowCommitAndClose.setVisible(false);
        name.setEditable(false);
        typeLookup.setEditable(false);
        description.setEditable(false);
        locName.setEditable(false);
        defaultRole.setEditable(false);

        showNotification(getMessage("predefinedRoleIsUnchangeable"));
    }
}