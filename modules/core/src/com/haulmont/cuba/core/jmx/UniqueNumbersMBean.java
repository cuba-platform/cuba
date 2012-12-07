/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.jmx;

import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;

/**
 * {@link com.haulmont.cuba.core.app.UniqueNumbersAPI} JMX interface.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface UniqueNumbersMBean {

    @ManagedOperationParameters({@ManagedOperationParameter(name = "domain", description = "")})
    long getCurrentNumber(String domain);

    @ManagedOperationParameters({
                @ManagedOperationParameter(name = "domain", description = ""),
                @ManagedOperationParameter(name = "value", description = "")
        })
    void setCurrentNumber(String domain, long value);

    @ManagedOperationParameters({@ManagedOperationParameter(name = "domain", description = "")})
    long getNextNumber(String domain);
}
