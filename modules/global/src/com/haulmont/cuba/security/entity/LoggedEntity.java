/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.*;
import java.util.Set;

import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import org.apache.commons.lang.BooleanUtils;

/**
 * Configuration element of <code>EntityLog</code> MBean.
 */
@Entity(name = "sec$LoggedEntity")
@Table(name = "SEC_LOGGED_ENTITY")
@SystemLevel
public class LoggedEntity extends BaseUuidEntity
{
    private static final long serialVersionUID = 2189206984294705835L;

    @Column(name = "NAME", length = 100)
    private String name;

    @Column(name = "AUTO")
    private Boolean auto;

    @Column(name = "MANUAL")
    private Boolean manual;

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL)
    private Set<LoggedAttribute> attributes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAuto() {
        return auto;
    }

    public void setAuto(Boolean auto) {
        this.auto = auto;
    }

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

    public Set<LoggedAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<LoggedAttribute> attributes) {
        this.attributes = attributes;
    }
}
