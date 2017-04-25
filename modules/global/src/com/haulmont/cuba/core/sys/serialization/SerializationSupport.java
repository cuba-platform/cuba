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

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.io.OutputStream;

import static java.lang.String.format;

/**
 *
 * Static holder for serialization object
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
