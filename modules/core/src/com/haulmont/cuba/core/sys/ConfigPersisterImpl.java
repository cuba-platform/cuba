/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.app.ConfigStorageAPI;
import com.haulmont.cuba.core.config.ConfigPersister;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.global.AppBeans;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ConfigPersisterImpl implements ConfigPersister {
    protected static final Logger log = LoggerFactory.getLogger(ConfigPersisterImpl.class);

    @Override
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
                value = AppContext.getProperty(name);
                if (StringUtils.isEmpty(value))
                    value = getConfigStorageAPI().getDbProperty(name);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported config source type: " + sourceType);
        }
        return value;
    }

    @Override
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
                getConfigStorageAPI().setDbProperty(name, value);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported config source type: " + sourceType);
        }
    }

    private ConfigStorageAPI getConfigStorageAPI() {
        return AppBeans.get(ConfigStorageAPI.NAME);
    }
}