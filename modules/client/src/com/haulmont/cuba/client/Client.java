/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 27.11.2008 14:50:04
 *
 * $Id$
 */
package com.haulmont.cuba.client;

import com.haulmont.cuba.security.global.LoginService;
import com.haulmont.cuba.security.global.JaasConfiguration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.Configuration;
import javax.security.auth.Subject;
import java.util.Properties;
import java.io.IOException;

public class Client
{
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.connect();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws LoginException, NamingException {
        Configuration.setConfiguration(new JaasConfiguration());
        LoginContext lc = new LoginContext(JaasConfiguration.CONTEXT_NAME, new ClientCallBackHandler("username","password"));
        lc.login();
        Subject subject = lc.getSubject();
        System.out.println(subject);


        Properties connProps = new Properties();
        connProps.put("java.naming.factory.initial", "org.jboss.naming.NamingContextFactory");
        connProps.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
        connProps.put("java.naming.provider.url", "localhost:1099");

        Context ctx = new InitialContext(connProps);
        LoginService ls = (LoginService) ctx.lookup("cuba/security/LoginService/remote");

        try {
            ls.authenticate(null, null, null);
        } catch (com.haulmont.cuba.security.global.LoginException e) {
            throw new RuntimeException(e);
        }
    }

    private class ClientCallBackHandler implements CallbackHandler
    {
        private String user, pass;

        private ClientCallBackHandler(String user, String pass) {
            this.user = user;
            this.pass = pass;
        }

        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for (Callback cb : callbacks) {
                if (cb instanceof NameCallback) {
                    NameCallback ncb = (NameCallback) cb;
                    ncb.setName(user);
                } else if (cb instanceof PasswordCallback) {
                    PasswordCallback pcb = (PasswordCallback) cb;
                    pcb.setPassword(pass.toCharArray());
                } else {
                    throw new UnsupportedCallbackException(cb, "Don't know what to do with this!!");
                }
            }
        }
    }
}
