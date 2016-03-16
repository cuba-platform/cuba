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

import com.haulmont.bali.util.ReflectionHelper;
import org.apache.commons.lang.StringUtils;

/**
 * Factory for obtaining implementations of DBMS-specific objects, particularly {@link DbmsFeatures},
 * {@link SequenceSupport} and {@link DbTypeConverter}.
 *
 * <p>You can also get DBMS-specific implementations of arbitrary interfaces by calling {@link #create(Class)} method.
 * These implementation classes must be located in the same package as interface and have names of the form
 * {@code Type[Version]Interface} where Type is capitalized DBMS type , Version is DBMS version and Interface is an interface name.
 * For example, if you have com.company.app.Foo interface, its implementation for PostgreSQL must be
 * {@code com.company.app.PostgresFoo}, and for MS SQL Server 2012 - {@code com.company.app.Mssql2012Foo}.
 *
 */
public class DbmsSpecificFactory {

    public static DbmsFeatures getDbmsFeatures() {
        return create(DbmsFeatures.class);
    }

    public static SequenceSupport getSequenceSupport() {
        return create(SequenceSupport.class);
    }

    public static DbTypeConverter getDbTypeConverter() {
        return create(DbTypeConverter.class);
    }

    public static <T> T create(Class<T> intf) {
        return create(intf, DbmsType.getType(), StringUtils.trimToEmpty(DbmsType.getVersion()));
    }

    public static <T> T create(Class<T> intf, String dbmsType, String dbmsVersion) {
        String intfName = intf.getName();
        String packageName = intfName.substring(0, intfName.lastIndexOf('.') + 1);

        String name = packageName + StringUtils.capitalize(dbmsType) + dbmsVersion + intf.getSimpleName();
        Class<?> aClass;
        try {
            aClass = ReflectionHelper.loadClass(name);
        } catch (ClassNotFoundException e) {
            name = packageName + StringUtils.capitalize(dbmsType) + intf.getSimpleName();
            try {
                aClass = ReflectionHelper.loadClass(name);
            } catch (ClassNotFoundException e1) {
                throw new RuntimeException("Error creating " + intfName + " implementation", e1);
            }
        }
        try {
            //noinspection unchecked
            return (T) aClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Error creating " + intfName + " implementation", e);
        }
    }
}
