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
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.*;

/**
 * Configuration element of <code>EntityLog</code> bean.
 *
 */
@Entity(name = "sec$LoggedAttribute")
@Table(name = "SEC_LOGGED_ATTR")
@SystemLevel
public class LoggedAttribute extends BaseUuidEntity {

    private static final long serialVersionUID = -615000337312303671L;
                                     
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTITY_ID")
    private LoggedEntity entity;

    @Column(name = "NAME")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LoggedEntity getEntity() {
        return entity;
    }

    public void setEntity(LoggedEntity entity) {
        this.entity = entity;
    }
}