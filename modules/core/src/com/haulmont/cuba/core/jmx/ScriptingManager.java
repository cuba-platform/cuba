/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.app.Authenticated;
import groovy.lang.Binding;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.Collections;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_ScriptingManagerMBean")
public class ScriptingManager implements ScriptingManagerMBean {

    protected Log log = LogFactory.getLog(getClass());

    @Inject
    protected Configuration configuration;

    @Inject
    protected Scripting scripting;

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    @Override
    public String getRootPath() {
        return configuration.getConfig(GlobalConfig.class).getConfDir();
    }

    @Authenticated
    @Override
    public String runGroovyScript(String scriptName) {
        try {
            Binding binding = new Binding();
            binding.setVariable("persistence", persistence);
            binding.setVariable("metadata", metadata);
            binding.setVariable("configuration", configuration);
            return scripting.runGroovyScript(scriptName, binding);
        } catch (Exception e) {
            log.error("Error runGroovyScript", e);
            return ExceptionUtils.getStackTrace(e);
        }
    }
}