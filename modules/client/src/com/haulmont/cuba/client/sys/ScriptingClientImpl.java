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

package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.sys.AbstractScripting;
import com.haulmont.cuba.core.sys.javacl.JavaClassLoader;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

/**
 *
 */
@Component(Scripting.NAME)
public class ScriptingClientImpl extends AbstractScripting {

    private String[] scriptEngineRoots;

    @Inject
    public ScriptingClientImpl(JavaClassLoader javaClassLoader, Configuration configuration) {
        super(javaClassLoader, configuration);
        scriptEngineRoots = new String[] {
                configuration.getConfig(GlobalConfig.class).getConfDir()
        };
    }

    @Override
    protected String[] getScriptEngineRootPath() {
        return scriptEngineRoots;
    }
}
