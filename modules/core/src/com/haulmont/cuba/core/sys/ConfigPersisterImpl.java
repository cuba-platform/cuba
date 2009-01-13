/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.01.2009 17:15:23
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.config.ConfigPersister;
import com.haulmont.cuba.core.config.SourceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigPersisterImpl implements ConfigPersister
{
    private final Log log = LogFactory.getLog(ConfigPersisterImpl.class);

    public String getProperty(SourceType sourceType, String name) {
        log.debug("Getting property '" + name + "', source=" + sourceType.name());
        if (SourceType.SYSTEM.equals(sourceType)) {
            return System.getProperty(name);
        }
        else {
            throw new UnsupportedOperationException("Unsupported config source type: " + sourceType);
        }
    }

    public void setProperty(SourceType sourceType, String name, String value) {
        log.debug("Setting property '" + name + "' to '" + value + "', source=" + sourceType.name());
        if (SourceType.SYSTEM.equals(sourceType)) {
            System.setProperty(name, value);
        }
        else {
            throw new UnsupportedOperationException("Unsupported config source type: " + sourceType);
        }
    }
}
