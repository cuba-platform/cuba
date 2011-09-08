/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.config.Config;

/**
 * Central interface to provide specific config interfaces
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface Configuration {

    String NAME = "cuba_Configuration";

    /**
     * Get reference to a configuration interface implementation.
     * @param configInterface class of configuration interface
     * @return the interface implementation which can be used to get/set parameters
     */
    <T extends Config> T getConfig(Class<T> configInterface);
}
