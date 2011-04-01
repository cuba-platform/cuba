/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.11.2009 10:20:32
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;

public class ScriptingProviderImpl extends AbstractScriptingProvider {

    private String[] scriptEngineRoots;

    public ScriptingProviderImpl(ConfigProvider configProvider) {
        super(configProvider);
        scriptEngineRoots = new String[] {
                configProvider.doGetConfig(GlobalConfig.class).getConfDir(),
                configProvider.doGetConfig(ServerConfig.class).getDbDir()
        };
    }

    protected String[] getScriptEngineRootPath() {
        return scriptEngineRoots;
    }
}
