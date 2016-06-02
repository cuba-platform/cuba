/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.sys.serialization;

import org.apache.commons.lang.ClassUtils;

import java.io.*;

/**
 * The serialization implementation using standard Java serialization
 */
public class StandardSerialization implements Serialization {
    @Override
    public void serialize(Object object, OutputStream os) {
        ObjectOutputStream out = null;
        boolean isObjectStream = os instanceof ObjectOutputStream;
        try {
            out = isObjectStream ? (ObjectOutputStream)os : new ObjectOutputStream(os);
            out.writeObject(object);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to deserialize object", ex);
        } finally {
            //Prevent close stream. Stream closed only by:
            //com.haulmont.cuba.core.sys.remoting.HttpServiceExporter,
            //com.haulmont.cuba.core.sys.remoting.ClusteredHttpInvokerRequestExecutor()
            //Only flush buffer to output stream
            if (!isObjectStream && out != null) {
                try {
                    out.flush();
                } catch (IOException ex) {
                    throw new IllegalStateException("Failed to deserialize object", ex);
                }
            }
        }
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
