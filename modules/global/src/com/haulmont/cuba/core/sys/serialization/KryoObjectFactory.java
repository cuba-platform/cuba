/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.core.sys.serialization;

import com.esotericsoftware.kryo.Kryo;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Object factory for kryo contexts object pool.
 */
public class KryoObjectFactory extends BasePooledObjectFactory<Kryo> {
    private final KryoSerialization kryoSerialization;

    public KryoObjectFactory(KryoSerialization kryoSerialization) {
        this.kryoSerialization = kryoSerialization;
    }

    @Override
    public Kryo create() {
        return kryoSerialization.newKryoInstance();
    }

    @Override
    public PooledObject<Kryo> wrap(Kryo obj) {
        return new DefaultPooledObject<>(obj);
    }
}
