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
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.chile.core.model.MetaClass;

import java.util.Collection;
import java.util.Map;
import java.util.Arrays;

public class UserEditor extends AbstractEditor {
    public UserEditor(Window frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        final DsContext dsContext = getDsContext();
        final Datasource userDs = dsContext.get("user");

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
        rolesTable.addAction(new AbstractAction("exclude") {
            public String getCaption() {
                return "Exclude";
            }

            public boolean isEnabled() {
                return true;
            }

            public void actionPerform(Component component) {
                
            }
        });

//        OptionsField field = getComponent("enum");
//        field.setOptionsList(Arrays.asList(Datasource.State.values()));
    }
}
