/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.03.2009 16:34:10
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Configuration element of <code>EntityLog</code> MBean.
 */
@Entity(name = "sec$LoggedAttribute")
@Table(name = "SEC_LOGGED_ATTR")
@SystemLevel
public class LoggedAttribute extends BaseUuidEntity
{
    private static final long serialVersionUID = -615000337312303671L;
                                     
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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
