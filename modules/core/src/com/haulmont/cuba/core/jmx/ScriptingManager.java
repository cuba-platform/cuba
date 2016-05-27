/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.app.Authenticated;
import groovy.lang.Binding;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.Collections;

@Component("cuba_ScriptingManagerMBean")
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