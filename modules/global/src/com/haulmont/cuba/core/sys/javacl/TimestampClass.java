/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.javacl;

import java.util.Collection;
import java.util.Date;

class TimestampClass {
    Class clazz;
    Date timestamp;
    Collection<String> dependencies;
    Collection<String> dependent;

    TimestampClass(Class clazz, Date timestamp, Collection<String> dependencies, Collection<String> dependent) {
        this.clazz = clazz;
        this.timestamp = timestamp;
        this.dependencies = dependencies;
        this.dependent = dependent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimestampClass that = (TimestampClass) o;

        if (clazz != null ? !clazz.equals(that.clazz) : that.clazz != null) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clazz != null ? clazz.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}
