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
import java.util.Set;

/**
 * Configuration element of <code>EntityLog</code> bean.
 */
@Entity(name = "sec$LoggedEntity")
@Table(name = "SEC_LOGGED_ENTITY")
@SystemLevel
public class LoggedEntity extends BaseUuidEntity {

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