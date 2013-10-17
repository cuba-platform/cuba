/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
}
