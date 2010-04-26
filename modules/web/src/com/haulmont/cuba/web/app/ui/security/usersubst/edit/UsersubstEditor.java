/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: FIRSTNAME LASTNAME
 * Created: 23.04.2010 17:43:34
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.usersubst.edit;

import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.UserSubstitution;

import java.util.Map;
import java.util.UUID;

public class UsersubstEditor extends AbstractEditor {

    private CollectionDatasource<UserSubstitution, UUID> user;

    public UsersubstEditor(Window frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        user = getDsContext().get("users");
        StringBuffer str = new StringBuffer();
        if (params.size() / 2 > 1) {
            str.append("( ");
            for (int i = 0; i < params.size() / 2 - 1; i++) {
                if (i != params.size() / 2 - 2) {
                    str.append(":param$id" + i + ".substitutedUser , ");
                } else {
                    str.append(":param$id" + i + ".substitutedUser");
                }
            }
            str.append(" )");
            user.setQuery("select u from sec$User u where u.id <> :param$item.user and u.id not in " + str.toString());
        }
        user.refresh();
        if (!PersistenceHelper.isNew(params.get("item"))) {
            Field substUserLookup = getComponent("substUser");
            substUserLookup.setEditable(false);
        }
    }

}
