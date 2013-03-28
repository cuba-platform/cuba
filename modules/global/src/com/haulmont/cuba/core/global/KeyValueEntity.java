/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import java.io.Serializable;

/**
 * Simple key:value entity
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@NamePattern("%s|keyValue")
@MetaClass(name = "sys$KeyValueItem")
@SystemLevel
public class KeyValueEntity extends AbstractNotPersistentEntity implements Serializable {

    @MetaProperty
    private String key;

    @MetaProperty
    private String keyValue;

    public KeyValueEntity(String key, String keyValue) {
        this.key = key;
        this.keyValue = keyValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
}
