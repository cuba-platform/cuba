/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.config.ConfigHandler;
import com.haulmont.cuba.core.global.Configuration;

import javax.annotation.ManagedBean;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(Configuration.NAME)
public class ConfigurationImpl extends AbstractConfiguration {

    @Override
    protected ConfigHandler getConfigHandler(Class configInterface) {
        return new ConfigHandler(new ConfigPersisterImpl(), configInterface);
    }
}
