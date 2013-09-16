/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import org.apache.commons.lang.ClassUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * Utility class for object deserialization.
 *
 * <p>To work properly must itself be loaded by the application classloader (i.e. by classloader capable of loading
 * all the other application classes). For web application it means placing this class inside webapp folder.</p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class Deserializer {

    /**
     * Deserialize an object from byte array, using classloader of this class.
     * @param bytes byte array
     * @return      deserialized object
     * @throws      RuntimeException in case of error
     */
    public static Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes)) {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    return ClassUtils.getClass(Deserializer.class.getClassLoader(), desc.getName());
                }
            };
            return ois.readObject();
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Failed to deserialize object", ex);
        }
        catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Failed to deserialize object type", ex);
        }
    }
}
