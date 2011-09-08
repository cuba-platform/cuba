/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.09.2010 12:00:07
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.ScriptingProvider;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import java.util.Collections;

@ManagedBean("cuba_ScriptingManager")
public class ScriptingManager implements ScriptingManagerMBean {

    private static Log log = LogFactory.getLog(ScriptingManager.class);

    public String getRootPath() {
        return ConfigProvider.getConfig(GlobalConfig.class).getConfDir();
    }

    public String runGroovyScript(String scriptName) {
        try {
            return ScriptingProvider.runGroovyScript(scriptName, Collections.<String, Object>emptyMap());
        } catch (Exception e) {
            log.error("Error runGroovyScript", e);
            return ExceptionUtils.getStackTrace(e);
        }
    }
}
