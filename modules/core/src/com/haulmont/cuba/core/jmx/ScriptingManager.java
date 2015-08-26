/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.app.Authenticated;
import groovy.lang.Binding;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.Collections;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_ScriptingManagerMBean")
public class ScriptingManager implements ScriptingManagerMBean {

    protected Logger log = LoggerFactory.getLogger(getClass());

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
            Object result = scripting.runGroovyScript(scriptName, binding);
            return String.valueOf(result);
        } catch (Exception e) {
            log.error("Error runGroovyScript", e);
            return ExceptionUtils.getStackTrace(e);
        }
    }
}