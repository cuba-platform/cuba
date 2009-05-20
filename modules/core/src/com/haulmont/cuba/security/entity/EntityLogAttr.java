/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 20.05.2009 11:04:04
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.*;

@Entity(name = "sec$EntityLogAttr")
@Table(name = "SEC_ENTITY_LOG_ATTR")
public class EntityLogAttr extends BaseUuidEntity
{
    private static final long serialVersionUID = 4258700403293876630L;

    public static final int VALUE_LEN = 1500;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private EntityLogItem logItem;

    @Column(name = "NAME", length = 50)
    private String name;

    @Column(name = "VALUE", length = VALUE_LEN)
    private String value;

    public EntityLogItem getLogItem() {
        return logItem;
    }

    public void setLogItem(EntityLogItem logItem) {
        this.logItem = logItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
