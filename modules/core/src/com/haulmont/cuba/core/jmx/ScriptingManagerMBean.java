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

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * JMX interface for {@link com.haulmont.cuba.core.global.Scripting}.
 *
 */
@ManagedResource(description = "Executes Groovy scripts on Middleware")
public interface ScriptingManagerMBean {

    String getRootPath();

    @ManagedOperation(description = "Execute a Groovy script. Binding contains variables: persistence, metadata, configuration")
    @ManagedOperationParameters(
            {@ManagedOperationParameter(name = "scriptName",
                    description = "path to the script relative to conf dir or to the classpath root")})
    String runGroovyScript(String scriptName);
}
