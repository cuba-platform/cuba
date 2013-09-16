/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.chile.core.loader;

import java.util.Collection;
import java.util.Collections;

public class MetadataObjectInfo<T> {
    private T object;
    private Collection<MetadataObjectInitTask> tasks;

    public MetadataObjectInfo(T object) {
        this.object = object;
        this.tasks = Collections.emptyList();
    }

    public MetadataObjectInfo(T object, Collection<? extends MetadataObjectInitTask> tasks) {
        this.object = object;
        this.tasks = (Collection<MetadataObjectInitTask>) tasks;
    }

    public T getObject() {
        return object;
    }

    public Collection<MetadataObjectInitTask> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<MetadataObjectInitTask> tasks) {
        this.tasks = tasks;
    }
}