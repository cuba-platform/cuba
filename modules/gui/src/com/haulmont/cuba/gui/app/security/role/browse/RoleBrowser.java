/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.security.role.browse;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;

import javax.inject.Inject;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class RoleBrowser extends AbstractLookup {

    private static final String DEFAULT_ROLE_PROPERTY = "defaultRole";

    @Inject
    protected Table rolesTable;

    @Inject
    protected Security security;

    @Inject
    protected Metadata metadata;

    @Inject
    protected DataManager dataManager;

    @Inject
    protected CollectionDatasource<Role, UUID> rolesDs;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        rolesTable.addAction(new ItemTrackingAction("assignToUsers") {
            @Override
            public void actionPerform(Component component) {
                if (target.getSelected().isEmpty()) {
                    showNotification(getMessage("selectRole.msg"), NotificationType.HUMANIZED);
                    return;
                }

                final Role role = (Role) target.getSingleSelected();
                Map<String, Object> params = new HashMap<>();
                WindowParams.MULTI_SELECT.set(params, true);
                openLookup("sec$User.lookup", new Handler() {
                    @Override
                    public void handleLookup(Collection items) {
                        if (items == null) return;
                        List<Entity> toCommit = new ArrayList<>();
                        for (Object item : items) {
                            User user = (User) item;
                            LoadContext<UserRole> ctx = new LoadContext<>(UserRole.class).setView("user.edit");
                            LoadContext.Query query = ctx.setQueryString("select ur from sec$UserRole ur where ur.user.id = :user");
                            query.setParameter("user", user);
                            List<UserRole> userRoles = dataManager.loadList(ctx);

                            boolean roleExist = false;
                            for (UserRole userRole : userRoles) {
                                if (role.equals(userRole.getRole())) {
                                    roleExist = true;
                                    break;
                                }
                            }
                            if (!roleExist) {
                                UserRole ur = metadata.create(UserRole.class);
                                ur.setUser(user);
                                ur.setRole(role);
                                toCommit.add(ur);
                            }
                        }

                        if (!toCommit.isEmpty()) {
                            dataManager.commit(new CommitContext(toCommit));
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

        boolean hasPermissionsToCreateUserRole = security.isEntityOpPermitted(UserRole.class, EntityOp.CREATE);

        Action copy = rolesTable.getAction("assignToUsers");
        if (copy != null) {
            copy.setEnabled(hasPermissionsToCreateUserRole);
        }

        String windowOpener = (String) params.get("param$windowOpener");
        if ("sec$User.edit".equals(windowOpener)) {
            rolesTable.setMultiSelect(true);
        }

        rolesDs.addItemPropertyChangeListener(e -> {
            if (DEFAULT_ROLE_PROPERTY.equals(e.getProperty())) {
                Role reloadedRole = dataManager.reload(e.getItem(), View.LOCAL);
                reloadedRole.setDefaultRole(e.getItem().getDefaultRole());
                rolesDs.updateItem(reloadedRole);
                rolesDs.modifyItem(reloadedRole);
                rolesDs.commit();
            }
        });
    }
}