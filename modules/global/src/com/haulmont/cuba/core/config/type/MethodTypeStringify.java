/*
 * A High-Level Framework for Application Configuration
 *
 * Copyright 2007 Merlin Hughes / Learning Objects, Inc.
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
 */

package com.haulmont.cuba.core.config.type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A stringifier that converts a type to a string by invoking a method
 * on it and converting the result directly to a string.
 *
 * @author Merlin Hughes
 * @version $Id$
 */
public class MethodTypeStringify extends TypeStringify
{
    /**
     * The stringify method.
     */
    private Method method;

    /**
     * Create a new MethodTypeStringify.
     *
     * @param method A method that takes no argument.
     */
    public MethodTypeStringify(Method method) {
        this.method = method;
    }

    /* Inherited. */
    public String stringify(Object value) {
        try {
            try {
                return String.valueOf(method.invoke(value));
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Throwable th) {
            throw new RuntimeException("Type stringify error", th);
        }
    }
}
