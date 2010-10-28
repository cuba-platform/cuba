/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 24.06.2010 15:46:41
 *
 * $Id$
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
