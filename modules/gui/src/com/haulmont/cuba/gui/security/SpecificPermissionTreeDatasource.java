/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.security;

import com.haulmont.bali.datastruct.Tree;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.security.entity.BasicPermissionTarget;

/**
 * @author abramov
 * @version $Id$
 */
public class SpecificPermissionTreeDatasource extends BasicPermissionTreeDatasource {

    protected PermissionConfig permissionConfig = AppBeans.get(PermissionConfig.class);
    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);

    @Override
    public Tree<BasicPermissionTarget> getPermissions() {
        return permissionConfig.getSpecific(userSessionSource.getLocale());
    }
}