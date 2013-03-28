/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 17.08.2010 11:12:08
 * $Id$
 */

package com.haulmont.cuba.web.jmx.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

@MetaClass(name = "jmxcontrol$ManagedBeanDomain")
@SystemLevel
public class ManagedBeanDomain extends AbstractNotPersistentEntity {
    private static final long serialVersionUID = 6806872453828281965L;

    @MetaProperty
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
