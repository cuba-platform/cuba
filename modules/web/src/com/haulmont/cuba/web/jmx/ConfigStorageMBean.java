/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedResource(description = "Manages configuration properties on Web Client")
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
            @ManagedOperationParameter(name = "classFQN", description = "Fully qualified name of a configuration interface"),
            @ManagedOperationParameter(name = "methodName", description = "Getter method name"),
            @ManagedOperationParameter(name = "userLogin", description = "User login that will be used for creating a user session. " +
                    "You can leave this field blank when using JMX console from CUBA application.")
    })
    String getConfigValue(String classFQN, String methodName, String userLogin);
}
