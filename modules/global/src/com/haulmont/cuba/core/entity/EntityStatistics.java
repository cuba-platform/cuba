/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author krivopustov
 * @version $Id$
 */
@Entity(name = "sys$EntityStatistics")
@Table(name = "SYS_ENTITY_STATISTICS")
@SystemLevel
public class EntityStatistics extends BaseUuidEntity implements Updatable {

    private static final long serialVersionUID = -1734840995849860033L;

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