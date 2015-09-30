/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.sys.javacl.JavaClassLoader;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component(Scripting.NAME)
public class ScriptingImpl extends AbstractScripting {

    private String[] scriptEngineRoots;

    @Inject
    public ScriptingImpl(JavaClassLoader javaClassLoader, Configuration configuration) {
        super(javaClassLoader, configuration);
        scriptEngineRoots = new String[] {
                configuration.getConfig(GlobalConfig.class).getConfDir(),
                configuration.getConfig(ServerConfig.class).getDbDir()
        };
    }

    @Override
    protected String[] getScriptEngineRootPath() {
        return scriptEngineRoots;
    }
}