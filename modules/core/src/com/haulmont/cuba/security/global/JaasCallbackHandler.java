/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 27.11.2008 16:29:26
 *
 * $Id$
 */
package com.haulmont.cuba.security.global;

import javax.security.auth.callback.*;
import java.io.IOException;
import java.util.UUID;

public class JaasCallbackHandler implements CallbackHandler
{
    private String login;
    private UUID sessionId;

    public JaasCallbackHandler(String login, UUID sessionId) {
        this.login = login;
        this.sessionId = sessionId;
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback cb : callbacks) {
            if (cb instanceof NameCallback) {
                NameCallback ncb = (NameCallback) cb;
                ncb.setName(login);
            } else if (cb instanceof PasswordCallback) {
                PasswordCallback pcb = (PasswordCallback) cb;
                pcb.setPassword(sessionId.toString().toCharArray());
            } else {
                throw new UnsupportedCallbackException(cb);
            }
        }
    }
}
