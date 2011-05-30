/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.user.browse;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.EntityFactory;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.app.security.user.edit.UserEditor;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ExcelAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import org.apache.commons.collections.map.SingletonMap;
import org.apache.commons.lang.BooleanUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserBrowser extends AbstractLookup {

    public UserBrowser(Window frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        final Table table  = getComponent("users");

        ComponentsHelper.createActions(table);

        final Action removeAction = table.getAction(RemoveAction.ACTION_ID);
        table.getDatasource().addListener(new DsListenerAdapter() {
            @Override
            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                super.itemChanged(ds, prevItem, item);
                User user = (User) item;
                if (removeAction != null)
                    removeAction.setEnabled(!(UserSessionProvider.getUserSession().getUser().equals(user) ||
                            UserSessionProvider.getUserSession().getCurrentOrSubstitutedUser().equals(user)));
            }
        });

        table.addAction(new ExcelAction(table));

        table.addAction(
                new AbstractAction("changePassw")
                {
                    public void actionPerform(Component component)  {
                        if (!table.getSelected().isEmpty()) {
                            openEditor (
                                    "sec$User.changePassw",
                                    (Entity) table.getSelected().iterator().next(),
                                    WindowManager.OpenType.DIALOG
                            );
                        }
                    }
                }
        );

        String multiSelect = (String)params.get("multiselect");
        if ("true".equals(multiSelect)) {
            table.setMultiSelect(true);
        }

        table.addAction(
                new AbstractAction("copy"){
                    public void actionPerform(Component component){
                        if (!table.getSelected().isEmpty()){
                            User selectedUser = (User) table.getSelected().iterator().next();
                            selectedUser = getDsContext().getDataService().reload(selectedUser, "user.edit");
                            User newUser = EntityFactory.create(User.class);
                            if(selectedUser.getUserRoles()!=null){
                                Set<UserRole> userRoles = new HashSet<UserRole>();
                                for (UserRole oldUserRole : selectedUser.getUserRoles()) {
                                    Role oldRole = getDsContext().getDataService().reload(oldUserRole.getRole(), "_local");
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
                }
        );

        table.addAction(new AbstractAction("copySettings") {
            public void actionPerform(Component component) {
                if (!table.getSelected().isEmpty()) {
                    openWindow(
                            "sec$User.copySettings",
                            WindowManager.OpenType.DIALOG,
                            new SingletonMap("users", table.getSelected())
                    );

                }
            }
        }
        );
    }
}
