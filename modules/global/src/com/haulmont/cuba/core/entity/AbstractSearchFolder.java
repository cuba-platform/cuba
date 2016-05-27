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

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@SystemLevel
public abstract class AbstractSearchFolder extends Folder {

    private static final long serialVersionUID = -2234453892776090930L;

    @Column(name = "FILTER_COMPONENT")
    protected String filterComponentId;

    @Column(name = "FILTER_XML")
    protected String filterXml;

    @Column(name = "APPLY_DEFAULT")
    protected Boolean applyDefault = true;

    public void copyFrom(AbstractSearchFolder srcFolder) {
        setCreatedBy(srcFolder.getCreatedBy());
        setCreateTs(srcFolder.getCreateTs());
        setDeletedBy(srcFolder.getDeletedBy());
        setDeleteTs(srcFolder.getDeleteTs());
        setFilterComponentId(srcFolder.getFilterComponentId());
        setFilterXml(srcFolder.getFilterXml());
        setName(srcFolder.getCaption());
        setTabName(srcFolder.getTabName());
        setParent(srcFolder.getParent());
        setItemStyle(srcFolder.getItemStyle());
        setSortOrder(srcFolder.getSortOrder());
    }

    public String getFilterComponentId() {
        return filterComponentId;
    }

    public void setFilterComponentId(String filterComponentId) {
        this.filterComponentId = filterComponentId;
    }

    public String getFilterXml() {
        return filterXml;
    }

    public void setFilterXml(String filterXml) {
        this.filterXml = filterXml;
    }

    public Boolean getApplyDefault() {
        return applyDefault;
    }

    public void setApplyDefault(Boolean applyDefault) {
        this.applyDefault = applyDefault;
    }

    @MetaProperty
    public String getLocName() {
        Messages messages = AppBeans.get(Messages.NAME);
        return messages.getMainMessage(name);
    }
}