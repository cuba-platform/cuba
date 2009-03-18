/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.03.2009 16:23:18
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.Set;

import org.apache.commons.lang.BooleanUtils;

@Entity(name = "sec$LoggedEntity")
@Table(name = "SEC_LOGGED_ENTITY")
public class LoggedEntity extends BaseUuidEntity
{
    private static final long serialVersionUID = 2189206984294705835L;

    @Column(name = "NAME", length = 100)
    private String name;

    @Column(name = "AUTO")
    private Boolean auto;

    @Column(name = "MANUAL")
    private Boolean manual;

    @OneToMany(mappedBy = "entity")
    private Set<LoggedAttribute> attributes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAuto() {
        return BooleanUtils.isTrue(auto);
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public boolean isManual() {
        return BooleanUtils.isTrue(manual);
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public Set<LoggedAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<LoggedAttribute> attributes) {
        this.attributes = attributes;
    }
}
