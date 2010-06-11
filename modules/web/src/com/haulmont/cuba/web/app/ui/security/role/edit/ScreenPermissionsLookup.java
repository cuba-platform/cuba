/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 30.04.2010 14:39:56
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.role.edit;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;

import java.util.Map;

public class ScreenPermissionsLookup extends PermissionsLookup {

    public ScreenPermissionsLookup(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);

        final OptionsGroup access = getComponent("access");

        CollectionDatasource ds = getDsContext().get("permissions");
        ds.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                        PermissionConfig.Target target = (PermissionConfig.Target) item;
                        boolean enable = target != null/* && target.getId().startsWith("item:")*/;
                        access.setEnabled(enable);
                    }
                }
        );
    }
}
