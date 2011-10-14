/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.02.2010 10:40:20
 *
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.Column;
import javax.persistence.Table;

@javax.persistence.Entity(name = "core$LockDescriptor")
@Table(name = "SYS_LOCK_CONFIG")
@SystemLevel
public class LockDescriptor extends BaseUuidEntity {

    private static final long serialVersionUID = -5798715368435824090L;

    @Column(name = "NAME", length = 100)
    private String name;

    @Column(name = "TIMEOUT_SEC")
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
