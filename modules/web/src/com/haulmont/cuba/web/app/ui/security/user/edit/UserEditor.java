/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 05.02.2009 13:35:20
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.user.edit;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.security.entity.UserSubstitution;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class UserEditor extends AbstractEditor {

    private Datasource<User> userDs;
    private Table rolesTable;
    private Table substTable;

    public UserEditor(Window frame) {
        super(frame);
    }                                                                                                  

    protected void init(Map<String, Object> params) {
        userDs = getDsContext().get("user");

        rolesTable = getComponent("roles");
        rolesTable.addAction(new AddRoleAction());
        TableActionsHelper rolesTableActions = new TableActionsHelper(this, rolesTable);
        rolesTableActions.createRemoveAction(false);

        substTable = getComponent("subst");
        substTable.addAction(new AddSubstitutedAction());
        substTable.addAction(new EditSubstitutedAction());
        TableActionsHelper substTableActions = new TableActionsHelper(this, substTable);
        substTableActions.createRemoveAction(false);

    }

    private boolean _commit() {
        boolean isNew = PersistenceHelper.isNew(userDs.getItem());
        if (isNew) {
            TextField passwField = getComponent("passw");
            TextField confirmPasswField = getComponent("confirmPassw");

            String passw = passwField.getValue();
            String confPassw = confirmPasswField.getValue();
            if (ObjectUtils.equals(passw, confPassw)) {
                if (StringUtils.isEmpty(passw))
                    userDs.getItem().setPassword(null);
                else
                    userDs.getItem().setPassword(DigestUtils.md5Hex(passw));
                return true;
            } else {
                showNotification(getMessage("passwordsDoNotMatch"), NotificationType.WARNING);
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean commit() {
        return _commit() && super.commit();
    }

    @Override
    public void commitAndClose() {
        if (_commit()) {
            super.commitAndClose();
        }
    }

    private class AddRoleAction extends AbstractAction {

        public AddRoleAction() {
            super("add");
        }

        public void actionPerform(Component component) {
            final CollectionDatasource<UserRole, UUID> ds = rolesTable.getDatasource();
            openLookup("sec$Role.lookup", new Lookup.Handler() {
                public void handleLookup(Collection items) {
                    for (Object item : items) {
                        final MetaClass metaClass = ds.getMetaClass();

                        UserRole userRole = ds.getDataService().newInstance(metaClass);
                        userRole.setRole((Role) item);
                        userRole.setUser(userDs.getItem());

                        ds.addItem(userRole);
                    }
                }
            }, WindowManager.OpenType.THIS_TAB);
        }
    }

    private class AddSubstitutedAction extends AbstractAction {
        public AddSubstitutedAction() {
            super("add");
        }

        public void actionPerform(Component component) {
            final CollectionDatasource<UserSubstitution, UUID> usDs = substTable.getDatasource();

            final UserSubstitution substitution = new UserSubstitution();
            substitution.setUser(userDs.getItem());

            openEditor("sec$UserSubstitution.edit", substitution,
                    WindowManager.OpenType.DIALOG, usDs);
        }
    }

    private class EditSubstitutedAction extends AbstractAction {
        public EditSubstitutedAction() {
            super("edit");
        }

        public void actionPerform(Component component) {
            final CollectionDatasource<UserSubstitution, UUID> usDs = substTable.getDatasource();

            openEditor("sec$UserSubstitution.edit", usDs.getItem(),
                    WindowManager.OpenType.DIALOG, usDs);
        }
    }
}
