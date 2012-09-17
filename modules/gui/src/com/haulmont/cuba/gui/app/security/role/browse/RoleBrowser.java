/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.app.security.role.browse;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ExcelAction;
import com.haulmont.cuba.gui.components.actions.ListActionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;

import java.util.*;
import java.util.List;

public class RoleBrowser extends AbstractLookup {

    private Table table;

    public void init(Map<String, Object> params) {
        table = getComponent("roles");

        ComponentsHelper.createActions(table, EnumSet.allOf(ListActionType.class));
        table.addAction(new ExcelAction(table));

        table.addAction(new AbstractAction("assignToUsers") {
            public void actionPerform(Component component) {
                if (table.getSelected().size() < 1) {
                    showNotification(getMessage("selectRole.msg"), NotificationType.HUMANIZED);
                    return;
                }
                final Role role = table.<Role>getSelected().iterator().next();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("multiSelect", "true");
                openLookup("sec$User.lookup", new Handler() {
                    public void handleLookup(Collection items) {
                        if (items == null) return;
                        List<Entity> toCommit = new ArrayList<Entity>();
                        for (Object item : items) {
                            User user = (User) item;
                            LoadContext ctx = new LoadContext(UserRole.class).setView("user.edit");
                            LoadContext.Query query = ctx.setQueryString("select ur from sec$UserRole ur where ur.user.id = :user");
                            query.addParameter("user", user);
                            List<UserRole> userRoles = ServiceLocator.getDataService().loadList(ctx);

                            boolean roleExist = false;
                            for (UserRole userRole : userRoles) {
                                if (role.equals(userRole.getRole())) {
                                    roleExist = true;
                                    break;
                                }
                            }
                            if (!roleExist) {
                                UserRole ur = new UserRole();
                                ur.setUser(user);
                                ur.setRole(role);
                                toCommit.add(ur);
                            }
                        }

                        if (!toCommit.isEmpty()) {
                            ServiceLocator.getDataService().commit(new CommitContext(toCommit));
                        }

                        showNotification(getMessage("rolesAssigned.msg"), NotificationType.HUMANIZED);
                    }
                }, WindowManager.OpenType.THIS_TAB, params);
            }

            @Override
            public String getCaption() {
                return getMessage("assignToUsers");
            }
        });

        table.refresh();

        String windowOpener = (String) params.get("param$windowOpener");
        if ("sec$User.edit".equals(windowOpener)) {
            table.setMultiSelect(true);
        }
    }
}
