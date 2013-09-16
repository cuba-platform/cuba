/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.TextField;

import java.util.Map;

public class PermissionShow extends AbstractWindow{

    private TextField permissionsMessage;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        setHeight("400px");
        String message = (String)params.get("message");
        permissionsMessage = getComponent("permissionsMessage");
        if(message != null)
            permissionsMessage.setValue(message);
        permissionsMessage.setEditable(true);
    }
}
