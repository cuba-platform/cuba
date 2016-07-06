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
package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity(name = "sys$EntityStatistics")
@Table(name = "SYS_ENTITY_STATISTICS")
@SystemLevel
public class EntityStatistics extends BaseUuidEntity implements Creatable, Updatable {

    private static final long serialVersionUID = -1734840995849860033L;

    @Column(name = "CREATE_TS")
    protected Date createTs;

    @Column(name = "CREATED_BY", length = LOGIN_FIELD_LEN)
    protected String createdBy;

    @Column(name = "UPDATE_TS")
    private Date updateTs;

    @Column(name = "UPDATED_BY", length = LOGIN_FIELD_LEN)
    private String updatedBy;

    @Column(name = "NAME", length = 50)
    private String name;

    @Column(name = "INSTANCE_COUNT")
    private Long instanceCount;

    @Column(name = "FETCH_UI")
    private Integer fetchUI;

    @Column(name = "MAX_FETCH_UI")
    private Integer maxFetchUI;

    @Column(name = "LAZY_COLLECTION_THRESHOLD")
    private Integer lazyCollectionThreshold;

    @Column(name = "LOOKUP_SCREEN_THRESHOLD")
    private Integer lookupScreenThreshold;

    @Override
    public Date getCreateTs() {
        return createTs;
    }

    @Override
    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public Date getUpdateTs() {
        return updateTs;
    }

    @Override
    public void setUpdateTs(Date updateTs) {
        this.updateTs = updateTs;
    }

    public Long getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(Long instanceCount) {
        this.instanceCount = instanceCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFetchUI() {
        return fetchUI;
    }

    public void setFetchUI(Integer fetchUI) {
        this.fetchUI = fetchUI;
    }

    public Integer getMaxFetchUI() {
        return maxFetchUI;
    }

    public void setMaxFetchUI(Integer maxFetchUI) {
        this.maxFetchUI = maxFetchUI;
    }

    public Integer getLazyCollectionThreshold() {
        return lazyCollectionThreshold;
    }

    public void setLazyCollectionThreshold(Integer lazyCollectionThreshold) {
        this.lazyCollectionThreshold = lazyCollectionThreshold;
    }

    public Integer getLookupScreenThreshold() {
        return lookupScreenThreshold;
    }

    public void setLookupScreenThreshold(Integer lookupScreenThreshold) {
        this.lookupScreenThreshold = lookupScreenThreshold;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(StringUtils.trimToEmpty(name));
        sb.append(": instanceCount=").append(instanceCount != null ? instanceCount : 0);
        if (lazyCollectionThreshold != null)
            sb.append(", lazyCollectionThreshold=").append(lazyCollectionThreshold);
        if (maxFetchUI != null)
            sb.append(", maxFetchUI=").append(maxFetchUI);
        return sb.toString();
    }
}