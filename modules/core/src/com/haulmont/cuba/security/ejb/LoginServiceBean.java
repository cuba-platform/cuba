/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.11.2008 13:30:56
 *
 * $Id$
 */
package com.haulmont.cuba.security.ejb;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.intf.LoginService;
import com.haulmont.cuba.security.intf.UserSession;
import com.haulmont.cuba.security.intf.LoginException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.Stateless;
import java.util.List;
import java.util.Locale;

@Stateless(name = LoginService.JNDI_NAME)
public class LoginServiceBean implements LoginService
{
    private Log log = LogFactory.getLog(LoginServiceBean.class);

    private LoginWorker getLoginWorker() {
        return Locator.lookupLocal(LoginWorker.JNDI_NAME);
    }

    public List<Profile> authenticate(String login, String password, Locale locale) throws LoginException {
        try {
            return getLoginWorker().authenticate(login, password, null);
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    public UserSession login(String login, String password, String profileName, Locale locale) throws LoginException {
        try {
            return getLoginWorker().login(login, password, profileName, null);
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }
}
