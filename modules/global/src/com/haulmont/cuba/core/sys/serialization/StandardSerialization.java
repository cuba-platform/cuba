/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.serialization;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.SerializationUtils;

import java.io.*;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class StandardSerialization implements Serialization {
    @Override
    public void serialize(Object object, OutputStream os) {
        SerializationUtils.serialize((Serializable) object, os);
    }

    //To work properly must itself be loaded by the application classloader (i.e. by classloader capable of loading
    //all the other application classes). For web application it means placing this class inside webapp folder.
    @Override
    public Object deserialize(InputStream is) {
        try {
            ObjectInputStream ois = new ObjectInputStream(is) {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    return ClassUtils.getClass(StandardSerialization.class.getClassLoader(), desc.getName());
                }
            };
            return ois.readObject();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to deserialize object", ex);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Failed to deserialize object type", ex);
        }
    }

    @Override
    public byte[] serialize(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        serialize(object, bos);
        return bos.toByteArray();
    }

    @Override
    public Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        return deserialize(new ByteArrayInputStream(bytes));
    }
}
