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

package com.haulmont.cuba.core.sys.persistence;

import org.eclipse.persistence.exceptions.ConversionException;
import org.eclipse.persistence.platform.database.oracle.Oracle10Platform;

import java.util.UUID;

/**
 */
public class CubaOraclePlatform extends Oracle10Platform {

    @Override
    public Object convertObject(Object sourceObject, Class javaClass) throws ConversionException {
        if (sourceObject instanceof UUID && javaClass == String.class) {
            return sourceObject.toString().replace("-", "");
        }
        return super.convertObject(sourceObject, javaClass);
    }
}
