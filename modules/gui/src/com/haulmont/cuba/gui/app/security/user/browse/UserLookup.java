/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.user.browse;

import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Table;

import javax.inject.Named;
import java.util.Map;

public class UserLookup extends AbstractLookup {

    @Named("users")
    protected Table table;

    public void init(Map<String, Object> params) {
        setHeight("400px");
        String multiSelect = (String) params.get("param$multiSelect");
        if ("true".equals(multiSelect)) {
            table.setMultiSelect(true);
        }
    }
}

