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
import com.haulmont.cuba.security.app.SecurityScopesService;
import com.haulmont.cuba.security.entity.Access;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.SecurityScope;
import com.haulmont.cuba.security.role.RolesService;

import javax.inject.Inject;
import java.util.stream.Collectors;

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
    protected LookupField securityScopeLookup;

    @Inject
    protected TextArea description;

    @Inject
    protected TextField locName;

    @Inject
    protected CheckBox defaultRole;

    @Inject
    protected SecurityScopesService securityScopesService;

    @Inject
    protected RolesService rolesService;

    @Inject
    protected GridLayout defaultAccessGrid;

    @Override
    protected void initNewItem(Role item) {
        if (!item.isPredefined()) {
            item.setDefaultScreenAccess(Access.DENY);
            item.setDefaultEntityCreateAccess(Access.DENY);
            item.setDefaultEntityReadAccess(Access.DENY);
            item.setDefaultEntityUpdateAccess(Access.DENY);
            item.setDefaultEntityDeleteAccess(Access.DENY);
            item.setDefaultEntityAttributeAccess(EntityAttrAccess.DENY);
            item.setDefaultSpecificAccess(Access.DENY);
        }
    }

    @Override
    protected void postInit() {
        Role role = getItem();
        if (entityStates.isNew(role) && !role.isPredefined()) {
            setCaption(getMessage("createCaption"));
            role.setSecurityScope(SecurityScope.DEFAULT_SCOPE_NAME);
        } else {
            setCaption(formatMessage("editCaption", role.getName()));
        }

        screensTabFrame.loadPermissions();

        if (role.isPredefined()) {
            restrictAccessForPredefinedRole();
        }
        initSecurityScopes();
    }

    @Override
    public boolean preCommit() {
        String roleName = getItem().getName();
        if (rolesService.getRoleByName(roleName) != null) {
            showNotification(getMessage("roleNameIsUsed"), NotificationType.WARNING);
            return false;
        }
        return true;
    }

    protected void restrictAccessForPredefinedRole() {
        windowCommit.setVisible(false);
        windowCommitAndClose.setVisible(false);
        name.setEditable(false);
        securityScopeLookup.setEditable(false);
        description.setEditable(false);
        locName.setEditable(false);
        defaultRole.setEditable(false);
        defaultAccessGrid.getComponents().stream()
                .filter(component -> component instanceof LookupField)
                .forEach(component -> ((LookupField) component).setEditable(false));
    }

    protected void initSecurityScopes() {
        //noinspection unchecked
        securityScopeLookup.setOptionsMap(securityScopesService.getAvailableSecurityScopes()
                .stream()
                .collect(Collectors.toMap(SecurityScope::getLocName, SecurityScope::getName)));
        if (securityScopesService.isOnlyDefaultScope()) {
            securityScopeLookup.setEditable(false);
        }
    }
}