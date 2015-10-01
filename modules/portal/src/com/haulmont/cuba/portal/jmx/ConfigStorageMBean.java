/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedResource(description = "Manages configuration properties on Web Portal")
public interface ConfigStorageMBean {

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

    @ManagedOperation(description = "Invoke a getter method of configuration interface and print the result")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "className", description = "Fully qualified name of a configuration interface"),
            @ManagedOperationParameter(name = "value", description = "Getter method name")
    })
    String getConfigValue(String classFQN, String methodName);
}
