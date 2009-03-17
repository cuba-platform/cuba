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

import com.haulmont.bali.datastruct.Tree;
import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.app.ResourceRepositoryService;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.gui.ApplicationProperties;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.config.PermissionConfig;

import java.util.List;

public class PermissionConfigTest extends CubaTestCase
{
    @Override
    protected void setUp() throws Exception {
        System.setProperty(ApplicationProperties.PERMISSION_CONFIG_XML_PROP, "cuba/permission-config.xml");
        System.setProperty(ApplicationProperties.MENU_CONFIG_XML_PROP, "cuba/client/web/menu-config.xml");
        System.setProperty(ApplicationProperties.WINDOW_CONFIG_XML_PROP, "cuba/client/web/screen-config.xml");
        System.setProperty(ApplicationProperties.WINDOW_CONFIG_IMPL_PROP, "com.haulmont.cuba.web.WindowConfig");
        System.setProperty(ApplicationProperties.CLIENT_TYPE_PROP, ClientType.WEB.toString());
        System.setProperty(ApplicationProperties.MESSAGES_PACKAGE_PROP, "com.haulmont.cuba.web");
    }

    public void test() {
        ResourceRepositoryService rr = ServiceLocator.lookup(ResourceRepositoryService.JNDI_NAME);
        PermissionConfig permissionConfig = new PermissionConfig();
        permissionConfig.compile();

        Tree<PermissionConfig.Target> screens = permissionConfig.getScreens();
        assertNotNull(screens);

        Tree<PermissionConfig.Target> entities = permissionConfig.getEntities();
        assertNotNull(entities);

        List<PermissionConfig.Target> operations = permissionConfig.getEntityOperations(new PermissionConfig.Target("core$Server", "Server", "core$Server"));
        assertNotNull(operations);

        List<PermissionConfig.Target> attributes = permissionConfig.getEntityAttributes(new PermissionConfig.Target("core$Server", "Server", "core$Server"));
        assertNotNull(attributes);

        Tree<PermissionConfig.Target> specific = permissionConfig.getSpecific();
        assertNotNull(specific);
    }
}
