/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.UuidProvider;
import org.apache.openjpa.persistence.Persistent;

import javax.persistence.*;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
@javax.persistence.Entity(name = "sys$CategoryAttributeValue")
@Table(name = "SYS_ATTR_VALUE")
public class CategoryAttributeValue extends StandardEntity {

    private static final long serialVersionUID = -2861790889151226985L;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ATTR_ID")
    private CategoryAttribute categoryAttribute;


    @Column(name = "VALUE", length = 255)
    private String value;

    @Column(name = "ENTITY_ID")
    @Persistent
    private UUID entityId;

    @Column(name = "ENTITY_VALUE")
    @Persistent
    private UUID entityValue;

    public void setCategoryAttribute(CategoryAttribute categoryAttribute) {
        this.categoryAttribute = categoryAttribute;
    }



    public CategoryAttribute getCategoryAttribute() {
        return categoryAttribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public UUID getEntityValue() {
        return entityValue;
    }

    public void setEntityValue(UUID entityValue) {
        this.entityValue = entityValue;
    }
}
