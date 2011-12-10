/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.inject.Inject;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class EntityPermissionsFrame extends AbstractFrame {

    @Inject
    private CollectionDatasource entityPermissionsDs;

    @Inject
    private Table entityPermissionsTable;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
    }
}
