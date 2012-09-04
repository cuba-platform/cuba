/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * {@link ConfigStorage} JMX interface.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedResource(description = "Manages configuration properties on the middleware tier")
public interface ConfigStorageMBean {

    @ManagedOperation(description = "Print all DB-stored properties")
    String printDbProperties();

    @ManagedOperation(description = "Print DB-stored properties, filtering properties by beginning of name")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "prefix", description = "")})
    String printDbProperties(String prefix);

    @ManagedOperation(description = "Print a DB-stored property value")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "name", description = "")})
    String getDbPropertyJmx(String name);

    @ManagedOperation(description = "Store a property value into the database")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "name", description = ""),
            @ManagedOperationParameter(name = "value", description = "")
    })
    String setDbPropertyJmx(String name, String value);

    @ManagedOperation(description = "Remove a property from the database")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "name", description = "")})
    String removeDbPropertyJmx(String name);

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
}
