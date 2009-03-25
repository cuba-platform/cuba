/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 05.02.2009 13:35:20
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.user.edit;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.chile.core.model.MetaClass;

import java.util.Collection;
import java.util.Map;
import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

public class UserEditor extends AbstractEditor {

    private Datasource<User> userDs;

    public UserEditor(Window frame) {
        super(frame);
    }

    public void setItem(Object item) {
        super.setItem(item);

        boolean isNew = PersistenceHelper.isNew(userDs.getItem());

        getComponent("passwLab").setVisible(isNew);
        getComponent("confirmPasswLab").setVisible(isNew);

        TextField passwField = getComponent("passw");
        TextField confirmPasswField = getComponent("confirmPassw");
        passwField.setVisible(isNew);
        confirmPasswField.setVisible(isNew);

        if (isNew) {
            passwField.addListener(
                    new ValueListener()
                    {
                        public void valueChanged(Object source, String property, Object prevValue, Object value) {
                            if (StringUtils.isBlank((String) value))
                                userDs.getItem().setPassword(null);
                            else
                                userDs.getItem().setPassword(DigestUtils.md5Hex((String) value));
                        }
                    }
            );
            confirmPasswField.addListener(
                    new ValueListener()
                    {
                        public void valueChanged(Object source, String property, Object prevValue, Object value) {
                            // TODO KK: implement comparison after Bug#3305 is fixed
                        }
                    }
            );

        }
    }

    protected void init(Map<String, Object> params) {
        userDs = getDsContext().get("user");

        final Table rolesTable = getComponent("roles");

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
}
