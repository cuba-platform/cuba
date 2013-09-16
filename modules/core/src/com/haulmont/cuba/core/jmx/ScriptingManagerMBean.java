/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * JMX interface for {@link com.haulmont.cuba.core.global.Scripting}.
 *
 * @author krivopustov
 * @version $Id$
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
