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
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FtsChangeType;

import java.util.UUID;

/**
 * Interface that is used to enque changed entities for indexing in Full Text Search engine.
 * It is implemented outside CUBA in the FTS project.
 *
 */
public interface FtsSender {

    String NAME = "cuba_FtsSender";

    void enqueue(Entity entity, FtsChangeType changeType);

    void enqueue(String entityName, Object entityId, FtsChangeType changeType);

    void enqueueFake(String entityName, Object entityId);

    void emptyQueue(String entityName);

    void emptyFakeQueue(String entityName);

    void emptyQueue();

    void initDefault();
}
