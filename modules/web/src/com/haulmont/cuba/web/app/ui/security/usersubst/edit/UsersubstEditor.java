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
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.UserSubstitution;

import java.util.Map;
import java.util.UUID;

public class UsersubstEditor extends AbstractEditor {

//    private CollectionDatasource<UserSubstitution, UUID> usersDs;

    public UsersubstEditor(Window frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
//        usersDs = getDsContext().get("users");

//        List existingIds = (List) params.get("existingIds");
//        if (existingIds != null) {
//            usersDs.setQuery(usersDs.getQuery() + " and u.id not in (:param$existingIds)");
//        }
//        usersDs.refresh();
        if (!PersistenceHelper.isNew(params.get("item"))) {
            Field substUserLookup = getComponent("substUser");
            substUserLookup.setEditable(false);
        }
    }

}
