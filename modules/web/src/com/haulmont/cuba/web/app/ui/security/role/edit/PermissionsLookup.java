/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.03.2009 14:44:01
 * $Id$
 */

package com.haulmont.cuba.web.app.ui.security.role.edit;

import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.Arrays;
import java.util.Map;

public class PermissionsLookup extends AbstractLookup {

    protected OptionsGroup accessOptions;

    public PermissionsLookup(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);
        final Tree entityPermissionsTree = getComponent("permissions-tree");

        @SuppressWarnings({"unchecked"})
        CollectionDatasource<PermissionConfig.Target, String> entityPermissionsDs =
                entityPermissionsTree.getDatasource();

        entityPermissionsDs.refresh();
        entityPermissionsTree.expandTree();

        accessOptions = getComponent("access");
        fillAccessOptions();
        accessOptions.setValue(accessOptions.getOptionsList().get(0));
    }

    protected void fillAccessOptions() {
        accessOptions.setOptionsList(Arrays.asList(PermissionValue.values()));
    }

    public Integer getPermissionValue() {
        PermissionValue option = accessOptions.getValue();
        return option == null ? null : option.getValue();
    }
}
