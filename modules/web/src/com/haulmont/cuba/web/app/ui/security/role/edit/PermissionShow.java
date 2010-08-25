/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Valery Novikov
 * Created: 25.08.2010 15:10:21
 *
 * $Id$
 */

package com.haulmont.cuba.web.app.ui.security.role.edit;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.ui.*;

import java.util.Map;

public class PermissionShow extends AbstractWindow{

    private TextField permissionsMessage;
    private Button ok;

    public PermissionShow(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);
        setHeight("400px");
        ok = getComponent("ok");
        ok.setAction(new AbstractAction("ok"){
            public void actionPerform(Component component) {
                close("close");
            }
        });
        String message = (String)params.get("message");
        permissionsMessage = getComponent("permissionsMessage");
        if(message != null)
            permissionsMessage.setValue(message);
        ((com.vaadin.ui.TextField)WebComponentsHelper.unwrap(permissionsMessage)).setReadOnly(true);
    }
}
