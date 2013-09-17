/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
        Boolean multiSelect = BooleanUtils.toBooleanObject((String) params.get("multiSelect"));
        if (multiSelect != null)
            usersTable.setMultiSelect(multiSelect);
    }
}

