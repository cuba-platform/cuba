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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 */
public class EnumClassFactory extends TypeFactory {

    private TypeFactory idFactory;
    private Method fromIdMethod;

    public EnumClassFactory(TypeFactory idFactory, Method fromIdMethod) {
        this.idFactory = idFactory;
        this.fromIdMethod = fromIdMethod;
    }

    @Override
    public Object build(String string) {
        try {
            Object id = idFactory.build(string);
            return fromIdMethod.invoke(null, id);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("TypeFactory build error", e);
        }
    }
}
