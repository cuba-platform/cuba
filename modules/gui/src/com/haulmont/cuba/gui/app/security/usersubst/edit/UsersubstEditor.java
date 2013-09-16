/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.security.usersubst.edit;

import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Field;

import java.util.Map;

public class UsersubstEditor extends AbstractEditor {

    public void init(Map<String, Object> params) {
        if (!PersistenceHelper.isNew(WindowParams.ITEM.getEntity(params))) {
            Field substUserLookup = getComponent("substUser");
            substUserLookup.setEditable(false);
        }
    }

}
