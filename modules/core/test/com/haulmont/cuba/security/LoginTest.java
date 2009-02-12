/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.11.2008 15:27:10
 *
 * $Id$
 */
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.CubaTestCase;
import org.apache.commons.codec.digest.DigestUtils;

public class LoginTest extends CubaTestCase
{
    private static final String ADMIN_NAME = "admin";
    private static final String ADMIN_PASSW = DigestUtils.md5Hex("admin");

    protected void setUp() throws Exception {
        super.setUp();
//        System.setProperty(SecurityProvider.IMPL_PROP, "com.haulmont.cuba.core.sys.SecurityProviderImpl");
    }

    public void test() throws Exception {
//        LoginWorker lw = Locator.lookupLocal(LoginWorker.JNDI_NAME);
//
//        UserSession userSession = lw.login(ADMIN_NAME, ADMIN_PASSW, "Default", Locale.getDefault());
//        assertNotNull(userSession);
//        UUID sessionId = userSession.getId();
//
//        ServerSecurityUtils.setSecurityAssociation(ADMIN_NAME, sessionId);
//
//        BasicService bs = Locator.lookupLocal(BasicService.JNDI_NAME);
//        BasicInvocationContext ctx = new BasicInvocationContext().setEntityClass(Server.class);
//        ctx.setQueryString("select u from sec$User u");
//        List<User> list = bs.loadList(ctx);
//        assertTrue(list.size() > 0);
//
//        assertTrue("Not in role", SecurityProvider.currentUserInRole("Administrators"));
//
//        lw.logout();
    }

}
