/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 16.07.2009 19:36:44
 *
 * $Id$
 */
package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;

import java.util.HashSet;

public class MetadataHelperTest extends CubaTestCase {

    public void testWalkProperties() {
        Role role = new Role();
        role.setName("the_role");

        User user = new User();
        user.setLogin("the_login");
        user.setUserRoles(new HashSet<UserRole>());

        UserRole userRole = new UserRole();
        userRole.setRole(role);
        userRole.setUser(user);

        user.getUserRoles().add(userRole);
        
        MetadataHelper.walkProperties((Instance) user, new PropertyVisitor() {
            public void visit(Instance instance, MetaProperty property) {
                System.out.println("Instance=" + instance + ", property=" + property.getName());
            }
        });
    }
}
