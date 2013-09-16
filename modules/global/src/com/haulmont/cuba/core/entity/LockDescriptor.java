/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
