/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.03.11 17:25
 *
 * $Id$
 */
package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.core.config.ConfigPersister;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigPersisterClientImpl implements ConfigPersister {

    private final Log log = LogFactory.getLog(ConfigPersisterClientImpl.class);

    public String getProperty(SourceType sourceType, String name) {
        log.trace("Getting property '" + name + "', source=" + sourceType.name());
        String value;
        switch (sourceType) {
            case SYSTEM:
                value = System.getProperty(name);
                break;
            case APP:
                value = AppContext.getProperty(name);
                break;
            case DATABASE:
//todo                value = getConfigStorageAPI().getConfigProperty(name);
                value = null;
                break;
            default:
                throw new UnsupportedOperationException("Unsupported config source type: " + sourceType);
        }
        return value;
    }

    public void setProperty(SourceType sourceType, String name, String value) {
        log.debug("Setting property '" + name + "' to '" + value + "', source=" + sourceType.name());
        switch (sourceType) {
            case SYSTEM:
                System.setProperty(name, value);
                break;
            case APP:
                AppContext.setProperty(name, value);
                break;
            case DATABASE:
//todo                getConfigStorageAPI().setConfigProperty(name, value);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported config source type: " + sourceType);
        }
    }
}
