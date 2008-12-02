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
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.BasicService;
import com.haulmont.cuba.core.global.BasicInvocationContext;
import com.haulmont.cuba.core.global.SecurityProvider;
import com.haulmont.cuba.security.ejb.LoginWorker;
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.JaasCallbackHandler;
import com.haulmont.cuba.security.global.JaasConfiguration;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.codec.digest.DigestUtils;

import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class LoginTest extends CubaTestCase
{
    private static final String ADMIN_NAME = "admin";
    private static final String ADMIN_PASSW = DigestUtils.md5Hex("admin");

    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty(SecurityProvider.IMPL_PROP, "com.haulmont.cuba.core.impl.SecurityProviderImpl");
    }

    public void test() throws Exception {
        LoginWorker lw = Locator.lookupLocal(LoginWorker.JNDI_NAME);

        List<Profile> profiles = lw.authenticate(ADMIN_NAME, ADMIN_PASSW, Locale.getDefault());
        assertNotNull(profiles);
        assertTrue(profiles.size() > 0);
        Profile profile = profiles.get(0);

        UserSession userSession = lw.login(ADMIN_NAME, ADMIN_PASSW, profile.getName(), Locale.getDefault());
        assertNotNull(userSession);
        UUID sessionId = userSession.getId();
        

        Configuration.setConfiguration(new JaasConfiguration());

        LoginContext lc = new LoginContext(
                JaasConfiguration.CONTEXT_NAME,
                new JaasCallbackHandler(ADMIN_NAME, sessionId)
        );
        lc.login();

        BasicService bs = Locator.lookupLocal(BasicService.JNDI_NAME);
        BasicInvocationContext ctx = new BasicInvocationContext()
                .setEntityClass(Server.class)
                .setQueryString("select u from sec$User u");
        List<User> list = bs.loadList(ctx);
        assertTrue(list.size() > 0);

        assertTrue(SecurityProvider.currentUserInRole("Administrators"));

        lw.logout();
    }

}
