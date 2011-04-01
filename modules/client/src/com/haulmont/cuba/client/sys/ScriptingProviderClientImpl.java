/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.sys.AbstractScriptingProvider;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ScriptingProviderClientImpl extends AbstractScriptingProvider {

    private String[] scriptEngineRoots;

    public ScriptingProviderClientImpl(ConfigProvider configProvider) {
        super(configProvider);
        scriptEngineRoots = new String[] {
                configProvider.doGetConfig(GlobalConfig.class).getConfDir()
        };
    }

    @Override
    protected String[] getScriptEngineRootPath() {
        return scriptEngineRoots;
    }
}
