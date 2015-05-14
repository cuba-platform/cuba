/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * Base class for entities with fixed category
 * Please consider  implementation of  {@link com.haulmont.cuba.core.entity.Categorized} instead of extension of this class
 *
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
@MappedSuperclass
public abstract class CategorizedEntity extends BaseUuidEntity implements Categorized {

    private static final long serialVersionUID = -4359158051274491070L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    protected Category category;

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public void setCategory(Category category) {
        this.category = category;
    }
}
