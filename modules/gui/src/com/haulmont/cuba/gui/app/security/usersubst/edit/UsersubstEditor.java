/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.app.security.usersubst.edit;

import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Field;

import java.util.Map;

public class UsersubstEditor extends AbstractEditor {

    public void init(Map<String, Object> params) {
        if (!PersistenceHelper.isNew(params.get("item"))) {
            Field substUserLookup = getComponent("substUser");
            substUserLookup.setEditable(false);
        }
    }

}
