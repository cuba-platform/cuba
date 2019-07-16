/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.core.sys.serialization;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.defaults.DefaultLong;

/**
 * @see KryoSerialization
 */
@Source(type = SourceType.APP)
public interface KryoSerializationConfig extends Config {

    /**
     * @return maximum number of Kryo instances available for concurrent use.
     */
    @DefaultInt(100)
    @Property("cuba.kryo.maxPoolSize")
    int getMaxPoolSize();

    /**
     * @return timeout to borrow Kryo instance from object pool.
     */
    @DefaultLong(10000)
    @Property("cuba.kryo.maxBorrowWaitMillis")
    long getMaxBorrowWaitMillis();
}
