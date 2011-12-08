/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.03.2009 11:52:34
 * $Id$
 */

package com.haulmont.cuba.gui.security;

import com.haulmont.bali.datastruct.Tree;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.AbstractTreeDatasource;
import com.haulmont.cuba.security.entity.PermissionTarget;

import java.util.Map;


/**
 * Datasource for manage screen permissions
 */
public class ScreenPermissionTargetsDatasource extends AbstractTreeDatasource<PermissionTarget, String> {
    public ScreenPermissionTargetsDatasource(
            DsContext context, DataService dataservice,
            String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    protected Tree<PermissionTarget> loadTree(Map params) {
        return AppContext.getBean(PermissionConfig.class).getScreens(UserSessionProvider.getLocale());
    }
}