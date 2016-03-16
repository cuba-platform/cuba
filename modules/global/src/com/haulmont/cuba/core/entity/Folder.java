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

import javax.persistence.*;
import javax.persistence.Entity;

@Entity(name = "sys$Folder")
@Table(name = "SYS_FOLDER")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "FOLDER_TYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("F")
@SystemLevel
public class Folder extends StandardEntity {

    private static final long serialVersionUID = -2038652558181851215L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    protected Folder parent;

    @Column(name = "NAME", length = 100)
    protected String name;

    @Column(name = "SORT_ORDER")
    protected Integer sortOrder;

    @Transient
    protected String itemStyle = null;

    @Column(name = "TAB_NAME")
    protected String tabName;

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return name;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getItemStyle() {
        return itemStyle;
    }

    public void setItemStyle(String itemStyle) {
        this.itemStyle = itemStyle;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }
}
