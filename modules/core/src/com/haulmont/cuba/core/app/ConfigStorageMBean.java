/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;

/**
 * {@link ConfigStorage} JMX interface.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface ConfigStorageMBean {

    String printDbProperties();

    @ManagedOperationParameters({@ManagedOperationParameter(name = "prefix", description = "")})
    String printDbProperties(String prefix);

    @ManagedOperationParameters({@ManagedOperationParameter(name = "name", description = "")})
    String getDbPropertyJmx(String name);

    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "name", description = ""),
            @ManagedOperationParameter(name = "value", description = "")
    })
    String setDbPropertyJmx(String name, String value);

    @ManagedOperationParameters({@ManagedOperationParameter(name = "name", description = "")})
    String removeDbPropertyJmx(String name);

    String printAppProperties();

    @ManagedOperationParameters({@ManagedOperationParameter(name = "prefix", description = "")})
    String printAppProperties(String prefix);

    @ManagedOperationParameters({@ManagedOperationParameter(name = "name", description = "")})
    String getAppProperty(String name);

    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "name", description = ""),
            @ManagedOperationParameter(name = "value", description = "")
    })
    String setAppProperty(String name, String value);

    void clearCache();
}
