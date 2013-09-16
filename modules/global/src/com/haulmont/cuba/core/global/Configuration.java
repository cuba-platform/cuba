/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.config.Config;

/**
 * Central interface of the configuration parameters framework.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface Configuration {

    String NAME = "cuba_Configuration";

    /**
     * Get a configuration parameters interface implementation.
     * @param configInterface   class of configuration interface
     * @return                  an instance to work with parameters
     */
    <T extends Config> T getConfig(Class<T> configInterface);
}
