/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 09.12.2008 10:38:49
 * $Id: MetadataObjectInfo.java 425 2009-06-22 13:20:24Z krivopustov $
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