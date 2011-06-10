/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.entity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
@MappedSuperclass
public abstract class CategorizedEntity extends BaseUuidEntity {

    private static final long serialVersionUID = -4359158051274491070L;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    protected Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
