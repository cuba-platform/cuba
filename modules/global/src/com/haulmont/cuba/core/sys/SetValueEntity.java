/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
@NamePattern("%s|value")
@MetaClass(name = "sys$SetValue")
@SystemLevel
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
