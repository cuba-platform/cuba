/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.showinfo;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import java.io.Serializable;

/**
 * Simple key:value entity
 *
 * @author artamonov
 * @version $Id$
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
