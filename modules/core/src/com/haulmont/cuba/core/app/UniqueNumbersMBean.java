/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

/*
 * Author: Konstantin Krivopustov
 * Created: 15.05.2009 22:10:48
 * 
 * $Id$
 */
package com.haulmont.cuba.core.app;

import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;

/**
 * {@link UniqueNumbers} JMX interface.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface UniqueNumbersMBean
{
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
