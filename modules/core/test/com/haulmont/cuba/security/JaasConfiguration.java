/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 27.11.2008 15:51:03
 *
 * $Id$
 */
package com.haulmont.cuba.security;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import java.util.HashMap;
import java.util.Map;

public class JaasConfiguration extends Configuration
{
    public static final String CONTEXT_NAME = "cuba";

    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        if (CONTEXT_NAME.equals(name)) {
            Map<String, Object> props = new HashMap<String, Object>();
            props.put("multi-threaded", "false");
            props.put("restore-login-identity", "false");
            return new AppConfigurationEntry[] {
                    new AppConfigurationEntry(
                            "org.jboss.security.ClientLoginModule",
                            AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                            props
                    )
            };
        }
        else {
            return new AppConfigurationEntry[0];
        }
    }

    public void refresh() {
    }
}
