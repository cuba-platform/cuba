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

package com.haulmont.cuba.testmodel.severalfetchgroups;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@NamePattern("%s|name")
@Table(name = "TEST_SEVERAL_FETCH_GROUPS_TARIFF")
@Entity(name = "test$SeveralFetchGroups_Tariff")
public class SeveralFetchGroups_Tariff extends StandardEntity {
    private static final long serialVersionUID = -7877967845883729537L;

    @Column(name = "NAME")
    protected String name;

    @Column(name = "DESCRIPTION")
    protected String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTIVE_VERSION_ID")
    protected SeveralFetchGroups_TariffVersion activeVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    protected SeveralFetchGroups_Tariff parent;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public SeveralFetchGroups_Tariff getParent() {
        return parent;
    }

    public void setParent(SeveralFetchGroups_Tariff parent) {
        this.parent = parent;
    }

    public SeveralFetchGroups_TariffVersion getActiveVersion() {
        return activeVersion;
    }

    public void setActiveVersion(SeveralFetchGroups_TariffVersion activeVersion) {
        this.activeVersion = activeVersion;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}