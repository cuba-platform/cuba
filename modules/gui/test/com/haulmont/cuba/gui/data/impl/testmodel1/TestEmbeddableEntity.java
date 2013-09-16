/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl.testmodel1;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.cuba.core.entity.EmbeddableEntity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Embeddable
@MetaClass(name = "test$EmbeddableEntity")
public class TestEmbeddableEntity extends EmbeddableEntity {

    @Column(name = "EMBEDDABLE_NAME")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
