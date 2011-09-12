/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.app.security.usersubst.edit;

import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.*;

import java.util.Map;

public class UsersubstEditor extends AbstractEditor {

//    private CollectionDatasource<UserSubstitution, UUID> usersDs;

    public UsersubstEditor(Window frame) {
        super(frame);
    }

    public void init(Map<String, Object> params) {
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
