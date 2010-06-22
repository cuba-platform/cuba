/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 14.10.2009 16:05:08
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity(name = "sec$Filter")
@Table(name = "SEC_FILTER")
@NamePattern("%s|name")
public class FilterEntity extends StandardEntity {

    @Column(name = "COMPONENT")
    private String componentId;

    @Column(name = "NAME", length = 255)
    private String name;

    @Column(name = "XML")
    private String xml;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Transient
    private Boolean isDefault;

    @Transient
    private AbstractSearchFolder folder;

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public AbstractSearchFolder getFolder() {
        return folder;
    }

    public void setFolder(AbstractSearchFolder folder) {
        this.folder = folder;
    }
}
