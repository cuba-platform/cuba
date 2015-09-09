/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author krivopustov
 * @version $Id$
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
