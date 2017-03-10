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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Table(name = "TEST_FETCH_SAME_MAIN_ENTITY")
@Entity(name = "test$FetchSameMainEntity")
public class FetchSameMainEntity extends StandardEntity {
    private static final long serialVersionUID = -463279237482816313L;

    @Column(name = "NAME")
    protected String name;

    @Column(name = "DESCRIPTION")
    protected String description;

    @OneToMany(mappedBy = "mainEntity")
    protected List<FetchSameLinkAEntity> linkAEntities;

    @OneToMany(mappedBy = "mainEntity")
    protected List<FetchSameLinkBEntity> linkBEntities;

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

    public List<FetchSameLinkAEntity> getLinkAEntities() {
        return linkAEntities;
    }

    public void setLinkAEntities(List<FetchSameLinkAEntity> linkAEntities) {
        this.linkAEntities = linkAEntities;
    }

    public List<FetchSameLinkBEntity> getLinkBEntities() {
        return linkBEntities;
    }

    public void setLinkBEntities(List<FetchSameLinkBEntity> linkBEntities) {
        this.linkBEntities = linkBEntities;
    }
}
