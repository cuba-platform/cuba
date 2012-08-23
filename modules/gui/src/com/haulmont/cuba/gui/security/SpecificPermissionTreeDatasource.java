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
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.security.entity.ui.BasicPermissionTarget;

public class SpecificPermissionTreeDatasource extends BasicPermissionTreeDatasource {
    public SpecificPermissionTreeDatasource(DsContext context, DataService dataservice,
                                            String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

    @Override
    public Tree<BasicPermissionTarget> getPermissions() {
        return AppBeans.get(PermissionConfig.class).getSpecific(UserSessionProvider.getLocale());
    }
}