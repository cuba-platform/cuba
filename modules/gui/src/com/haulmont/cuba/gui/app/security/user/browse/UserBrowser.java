/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.user.browse;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.app.security.user.edit.UserEditor;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.collections.map.SingletonMap;
import org.apache.commons.lang.BooleanUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.UUID;

/**
 * @author abramov
 * @version $Id$
 */
public class UserBrowser extends AbstractLookup {

    @Inject
    protected Table usersTable;

    @Inject
    protected CollectionDatasource<User, UUID> usersDs;

    @Named("usersTable.remove")
    protected RemoveAction removeAction;

    @Inject
    protected UserSession userSession;

    @Inject
    protected Metadata metadata;

    @Inject
    protected DataService dataService;

    @Override
    public void init(Map<String, Object> params) {
        usersDs.addListener(new DsListenerAdapter<User>() {
            @Override
            public void itemChanged(Datasource<User> ds, User prevItem, User item) {
                if (removeAction != null)
                    removeAction.setEnabled(!(userSession.getUser().equals(item) ||
                            userSession.getCurrentOrSubstitutedUser().equals(item)));
            }
        });

        Boolean multiSelect = BooleanUtils.toBooleanObject((String) params.get("multiselect"));
        if (multiSelect != null)
            usersTable.setMultiSelect(multiSelect);
    }

    public void copy() {
        if (!usersTable.getSelected().isEmpty()) {
            User selectedUser = (User) usersTable.getSelected().iterator().next();
            selectedUser = dataService.reload(selectedUser, "user.edit");
            User newUser = metadata.create(User.class);
            if (selectedUser.getUserRoles() != null) {
                LinkedHashSet<UserRole> userRoles = new LinkedHashSet<UserRole>();
                for (UserRole oldUserRole : selectedUser.getUserRoles()) {
                    Role oldRole = dataService.reload(oldUserRole.getRole(), "_local");
                    if (BooleanUtils.isTrue(oldRole.getDefaultRole()))
                        continue;
                    UserRole role = new UserRole();
                    role.setUser(newUser);
                    role.setRole(oldRole);
                    userRoles.add(role);
                }
                newUser.setUserRoles(userRoles);
            }
            newUser.setGroup(selectedUser.getGroup());
            UserEditor editor = openEditor("sec$User.edit", newUser, WindowManager.OpenType.THIS_TAB);
            editor.initCopy();
            editor.addListener(new CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    usersDs.refresh();
                }
            });
        }
    }

    @SuppressWarnings("unused")
    public void copySettings() {
        if (!usersTable.getSelected().isEmpty()) {
            openWindow(
                    "sec$User.copySettings",
                    WindowManager.OpenType.DIALOG,
                    new SingletonMap("users", usersTable.getSelected())
            );
        }
    }

    @SuppressWarnings("unused")
    public void changePassword() {
        if (!usersTable.getSelected().isEmpty()) {
            openEditor(
                    "sec$User.changePassw",
                    (Entity) usersTable.getSelected().iterator().next(),
                    WindowManager.OpenType.DIALOG
            );
        }
    }
}
