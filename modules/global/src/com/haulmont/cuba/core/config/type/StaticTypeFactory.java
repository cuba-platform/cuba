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

package com.haulmont.cuba.core.config.type;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * A factory that builds a type by invoking a static method, supplying
 * the string value as a parameter.
 *
 * @version 0.1, 2007/04/15
 */
public class StaticTypeFactory extends TypeFactory {
    /**
     * The type factory method.
     */
    private Method method;

    /**
     * Create a new StaticTypeFactory.
     *
     * @param method A static method that takes one string argument.
     */
    public StaticTypeFactory(Method method) {
        this.method = method;
    }

    @Override
    public Object build(String string) {
        if (string == null) {
            return null;
        }

        try {
            try {
                return method.invoke(null, string);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Throwable th) {
            throw new RuntimeException("Type build error", th);
        }
    }
}