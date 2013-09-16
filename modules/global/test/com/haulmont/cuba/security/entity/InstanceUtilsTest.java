/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.security.entity;

import junit.framework.TestCase;

/**
 * @author krivopustov
 * @version $Id$
 */
public class InstanceUtilsTest extends TestCase {

    private User user;
    private UserSubstitution userSubst;
    private Group group;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        user = new User();
        user.setLogin("testLogin");
        user.setName("testName");

        userSubst = new UserSubstitution();
        userSubst.setUser(user);

        group = new Group();
        group.setName("testGroup");
        user.setGroup(group);
    }

    public void test() {
        assertNull(userSubst.getValueEx("user.defaultSubstitutedUser.login"));

        assertEquals("testName", userSubst.getValueEx("user.name.login"));

        assertEquals("testGroup", userSubst.getValueEx("user.group.name"));

        userSubst.setValueEx("user.group.name", "newName");
        assertEquals("newName", userSubst.getValueEx("user.group.name"));

        try {
            userSubst.setValueEx("user.defaultSubstitutedUser.login", "newLogin");
            fail();
        } catch (IllegalStateException e) {
            // ok
        }
    }
}
