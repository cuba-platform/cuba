/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.security;

import com.haulmont.bali.datastruct.Tree;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.security.entity.ui.BasicPermissionTarget;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ScreenPermissionTreeDatasource extends BasicPermissionTreeDatasource {
    public ScreenPermissionTreeDatasource(DsContext context, DataService dataservice,
                                          String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

    @Override
    public Tree<BasicPermissionTarget> getPermissions() {
        return AppContext.getBean(PermissionConfig.class).getScreens(UserSessionProvider.getLocale());
    }
}
