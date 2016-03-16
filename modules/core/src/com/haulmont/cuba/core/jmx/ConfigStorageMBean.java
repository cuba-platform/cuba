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

import com.haulmont.cuba.security.app.Authenticated;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * JMX interface for {@link com.haulmont.cuba.core.app.ConfigStorageAPI}.
 *
 */
@ManagedResource(description = "Manages configuration properties on Middleware")
public interface ConfigStorageMBean {

    @ManagedOperation(description = "Print all DB-stored properties")
    String printDbProperties();

    @ManagedOperation(description = "Print DB-stored properties, filtering properties by beginning of name")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "prefix", description = "")})
    String printDbProperties(String prefix);

    @ManagedOperation(description = "Print a DB-stored property value")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "name", description = "")})
    String getDbProperty(String name);

    @ManagedOperation(description = "Store a property value into the database")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "name", description = ""),
            @ManagedOperationParameter(name = "value", description = "")
    })
    String setDbProperty(String name, String value);

    @ManagedOperation(description = "Remove a property from the database")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "name", description = "")})
    String removeDbProperty(String name);

    @ManagedOperation(description = "Print all file-stored properties")
    String printAppProperties();

    @ManagedOperation(description = "Print file-stored properties, filtering properties by beginning of name")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "prefix", description = "")})
    String printAppProperties(String prefix);

    @ManagedOperation(description = "Print a file-stored property value")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "name", description = "")})
    String getAppProperty(String name);

    @ManagedOperation(description = "Set a file-stored property value in memory until the server restart")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "name", description = ""),
            @ManagedOperationParameter(name = "value", description = "")
    })
    String setAppProperty(String name, String value);

    @ManagedOperation(description = "Clear cache of DB-based properties")
    void clearCache();

    @ManagedOperation(description = "Invoke a getter method of configuration interface and print the result")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "classFQN", description = "Fully qualified name of a configuration interface"),
            @ManagedOperationParameter(name = "methodName", description = "Getter method name")
    })
    String getConfigValue(String classFQN, String methodName);
}
