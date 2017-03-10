/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.testmodel.fetchsameentity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;


@Table(name = "TEST_FETCH_SAME_LINK_A_ENTITY")
@Entity(name = "test$FetchSameLinkAEntity")
public class FetchSameLinkAEntity extends StandardEntity {
    private static final long serialVersionUID = 1236279239009133835L;

    @Column(name = "NAME")
    protected String name;

    @Column(name = "DESCRIPTION")
    protected String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAIN_ENTITY_ID")
    protected FetchSameMainEntity mainEntity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FetchSameMainEntity getMainEntity() {
        return mainEntity;
    }

    public void setMainEntity(FetchSameMainEntity mainEntity) {
        this.mainEntity = mainEntity;
    }
}
