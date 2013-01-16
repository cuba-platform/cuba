/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.security;

import com.haulmont.bali.datastruct.Tree;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.security.entity.ui.BasicPermissionTarget;

/**
 * @author artamonov
 * @version $Id$
 */
public class ScreenPermissionTreeDatasource extends BasicPermissionTreeDatasource {

    protected PermissionConfig permissionConfig = AppBeans.get(PermissionConfig.class);
    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);

    @Override
    public Tree<BasicPermissionTarget> getPermissions() {
        return permissionConfig.getScreens(userSessionSource.getLocale());
    }
}
