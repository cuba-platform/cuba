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
package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.Column;
import javax.persistence.Table;

@javax.persistence.Entity(name = "sys$LockDescriptor")
@Table(name = "SYS_LOCK_CONFIG")
@SystemLevel
public class LockDescriptor extends BaseUuidEntity {

    private static final long serialVersionUID = -5798715368435824090L;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "TIMEOUT_SEC", nullable = false)
    private Integer timeoutSec;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTimeoutSec() {
        return timeoutSec;
    }

    public void setTimeoutSec(Integer timeoutSec) {
        this.timeoutSec = timeoutSec;
    }
}
