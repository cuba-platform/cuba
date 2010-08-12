/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Valery Novikov
 * Created: 01.07.2010 15:35:43
 *
 * $Id$
 */

package com.haulmont.cuba.web.app.ui.security.user.browse;

import com.haulmont.cuba.gui.components.*;

import java.util.Map;

public class UserLookup extends AbstractLookup {
    public UserLookup(Window frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        final Table table  = getComponent("users");
        setHeight("400px");
        String multiSelect = (String) params.get("param$multiSelect");
        if ("true".equals(multiSelect)) {
            table.setMultiSelect(true);
        }
    }
}

