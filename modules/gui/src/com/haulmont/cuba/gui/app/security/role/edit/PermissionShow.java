/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.TextField;

import java.util.Map;

public class PermissionShow extends AbstractWindow{

    private TextField permissionsMessage;

    public PermissionShow(IFrame frame) {
        super(frame);
    }

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
