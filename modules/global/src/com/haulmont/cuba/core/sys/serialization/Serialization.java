/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.serialization;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author degtyarjov
 * @version $Id$
 *
 *  Defines the contract for platform serialization implementations.
 */
public interface Serialization {
    /**
     * Serialize object to stream
     */
    void serialize(Object object, OutputStream os);

    /**
     * Deserialize object from stream
     */
    Object deserialize(InputStream is);

    /**
     * Serialize object to byte array
     */
    byte[] serialize(Object object);

    /**
     * Deserialize object from byte array
     */
    Object deserialize(byte[] bytes);
}
