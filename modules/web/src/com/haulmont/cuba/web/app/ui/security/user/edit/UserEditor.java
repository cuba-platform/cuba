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
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Map;

public class UserEditor extends AbstractEditor {
    private Datasource<User> userDs;

    public UserEditor(Window frame) {
        super(frame);
    }                                                                                                  

    protected void init(Map<String, Object> params) {
        final Table rolesTable = getComponent("roles");
        userDs = getDsContext().get("user");

        rolesTable.addAction(new AbstractAction("include") {
            public String getCaption() {
                return "Include";
            }

            public boolean isEnabled() {
                return true;
            }

            public void actionPerform(Component component) {
                final CollectionDatasource ds = rolesTable.getDatasource();
                openLookup("sec$Role.lookup", new Lookup.Handler() {
                    public void handleLookup(Collection items) {
                        for (Object item : items) {
                            final MetaClass metaClass = ds.getMetaClass();

                            UserRole userRole = ds.getDataService().newInstance(metaClass);
                            userRole.setRole((Role) item);
                            userRole.setUser((User) userDs.getItem());

                            ds.addItem(userRole);
                        }
                    }
                }, WindowManager.OpenType.THIS_TAB);
            }
        });

        final TableActionsHelper rolesTableActions = new TableActionsHelper(this, rolesTable);
        rolesTableActions.createRemoveAction();
    }

    public boolean commit() {
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
                return super.commit();
            } else {
                showNotification(getMessage("passwordsDoNotMatch"), NotificationType.WARNING);
                return false;
            }
        } else {
            return super.commit();
        }
    }
}
