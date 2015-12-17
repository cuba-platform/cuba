/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.serialization;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.io.OutputStream;

import static java.lang.String.format;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class SerializationSupport {
    private final static Serialization serialization;

    static {
        String serializationClassStr = AppContext.getProperty("cuba.serialization.impl");
        if (StringUtils.isNotBlank(serializationClassStr)) {
            try {
                Class<Serialization> aClass = ReflectionHelper.getClass(serializationClassStr);
                serialization = ReflectionHelper.newInstance(aClass);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(
                        format("Could not create an instance of serialization class [%s]", serializationClassStr));
            }
        } else {
            serialization = new KryoSerialization();
        }
    }

    public static void serialize(Object object, OutputStream os) {
        serialization.serialize(object, os);
    }

    public static Object deserialize(InputStream is) {
        return serialization.deserialize(is);
    }

    public static byte[] serialize(Object object) {
        return serialization.serialize(object);
    }

    public static Object deserialize(byte[] bytes) {
        return serialization.deserialize(bytes);
    }
}
