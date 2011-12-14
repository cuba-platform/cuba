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
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
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

public class UserBrowser extends AbstractLookup {

    @Named("users")
    protected Table table;

    @Named("users.remove")
    protected RemoveAction removeAction;

    @Inject
    protected UserSession userSession;

    @Inject
    protected Metadata metadata;

    @Inject
    protected DataService dataService;

    public UserBrowser(Window frame) {
        super(frame);
    }

    public void init(Map<String, Object> params) {
        table.getDatasource().addListener(new DsListenerAdapter() {
            @Override
            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                super.itemChanged(ds, prevItem, item);
                User user = (User) item;
                if (removeAction != null)
                    removeAction.setEnabled(!(userSession.getUser().equals(user) ||
                            userSession.getCurrentOrSubstitutedUser().equals(user)));
            }
        });

        String multiSelect = (String) params.get("multiselect");
        if ("true".equals(multiSelect)) {
            table.setMultiSelect(true);
        }
    }

    public void copy() {
        if (!table.getSelected().isEmpty()) {
            User selectedUser = (User) table.getSelected().iterator().next();
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
                public void windowClosed(String actionId) {
                    getDsContext().get("users").refresh();
                }
            });
        }
    }

    public void copySettings() {
        if (!table.getSelected().isEmpty()) {
            openWindow(
                    "sec$User.copySettings",
                    WindowManager.OpenType.DIALOG,
                    new SingletonMap("users", table.getSelected())
            );
        }
    }

    public void changePassword() {
        if (!table.getSelected().isEmpty()) {
            openEditor(
                    "sec$User.changePassw",
                    (Entity) table.getSelected().iterator().next(),
                    WindowManager.OpenType.DIALOG
            );
        }
    }
}
