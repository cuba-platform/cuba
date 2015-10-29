/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultLong;

/**
 * Middleware cluster configuration settings.
 *
 * @author Alexander Budarov
 * @version $Id$
 */
@Source(type = SourceType.APP)
public interface ClusterConfig extends Config {

    /**
     * @return timeout to receive state from cluster when node starts, in milliseconds
     */
    @Property("cuba.cluster.stateTransferTimeout")
    @DefaultLong(10000)
    long getStateReceiveTimeout();

}
