/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
@NamePattern("%s|value")
@MetaClass(name = "sys$SetValue")
public class SetValueEntity extends AbstractNotPersistentEntity {
    private static final long serialVersionUID = -1652898874022057451L;

    public SetValueEntity(String value){
        this.value=value;
    }

    private String value;

    @MetaProperty
    public String getValue(){
        return value;
    }

    @MetaProperty
    public void setValue(String value){
        this.value=value;
    }
}
