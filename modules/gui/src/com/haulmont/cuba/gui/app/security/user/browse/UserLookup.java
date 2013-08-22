/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.user.browse;

import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Table;
import org.apache.commons.lang.BooleanUtils;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author abramov
 * @version $Id$
 */
public class UserLookup extends AbstractLookup {

    @Inject
    protected Table usersTable;

    @Override
    public void init(Map<String, Object> params) {
        setHeight("400px");
        Boolean multiSelect = BooleanUtils.toBooleanObject(params.get("multiSelect").toString());
        if (multiSelect != null)
            usersTable.setMultiSelect(multiSelect);
    }
}

