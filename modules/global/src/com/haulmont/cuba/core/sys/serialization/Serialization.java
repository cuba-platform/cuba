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
 */
public interface Serialization {
    public void serialize(Object object, OutputStream os);

    public Object deserialize(InputStream is);

    public byte[] serialize(Object object);

    public Object deserialize(byte[] bytes);
}
