/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.03.2009 18:29:51
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.app.ResourceRepositoryService;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.bali.datastruct.Tree;

import java.util.Locale;
import java.util.List;

public class PermissionConfigTest extends CubaTestCase
{
    public void test() {
        ResourceRepositoryService rr = ServiceLocator.lookup(ResourceRepositoryService.JNDI_NAME);
        PermissionConfig permissionConfig = new PermissionConfig(rr, "cuba", ClientType.WEB, "com.haulmont.cuba.web", Locale.getDefault());
        permissionConfig.compile();

        Tree<PermissionConfig.Target> screens = permissionConfig.getScreens();
        assertNotNull(screens);

        Tree<PermissionConfig.Target> entities = permissionConfig.getEntities();
        assertNotNull(entities);

        List<PermissionConfig.Target> operations = permissionConfig.getEntityOperations(new PermissionConfig.Target("core$Server", "Server"));
        assertNotNull(operations);

        List<PermissionConfig.Target> attributes = permissionConfig.getEntityAttributes(new PermissionConfig.Target("core$Server", "Server"));
        assertNotNull(attributes);

        Tree<PermissionConfig.Target> specific = permissionConfig.getSpecific();
        assertNotNull(specific);
    }
}
