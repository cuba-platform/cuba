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
 */

package com.haulmont.cuba.testmodel.selfinherited;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.List;

@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("R")
@Table(name = "TEST_ROOT_ENTITY")
@Entity(name = "test$RootEntity")
public class RootEntity extends StandardEntity {
    private static final long serialVersionUID = -7617820146992722574L;

    @Column(name = "DESCRIPTION")
    protected String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTITY_ID")
    protected ChildEntity entity;

    @OneToMany(mappedBy = "master")
    protected List<RootEntityDetail> details;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ChildEntity getEntity() {
        return entity;
    }

    public void setEntity(ChildEntity entity) {
        this.entity = entity;
    }

    public List<RootEntityDetail> getDetails() {
        return details;
    }

    public void setDetails(List<RootEntityDetail> details) {
        this.details = details;
    }
}
