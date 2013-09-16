/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.FtsChangeType;

import java.util.UUID;

public interface FtsSender {

    String NAME = "cuba_FtsSender";

    void enqueue(BaseEntity<UUID> entity, FtsChangeType changeType);

    void enqueue(String entityName, UUID entityId, FtsChangeType changeType);
    
    void emptyQueue(String entityName);

    void emptyQueue();

    void initDefault();
}
