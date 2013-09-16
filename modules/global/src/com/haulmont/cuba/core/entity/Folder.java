/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
