/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.config;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.datatypes.impl.BooleanDatatype;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

/**
 *
 */
@SystemLevel
@MetaClass(name = "sys$AppPropertyEntity")
public class AppPropertyEntity extends AbstractNotPersistentEntity implements Comparable<AppPropertyEntity> {

    @MetaProperty
    private AppPropertyEntity parent;

    @MetaProperty
    private String name;

    @MetaProperty
    private String defaultValue;

    @MetaProperty
    private String currentValue;

    @MetaProperty
    private Boolean category = true;

    @MetaProperty
    private Boolean overridden = false;

    public AppPropertyEntity getParent() {
        return parent;
    }

    public void setParent(AppPropertyEntity parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public Boolean getCategory() {
        return category;
    }

    public void setCategory(Boolean category) {
        this.category = category;
    }

    public Boolean getOverridden() {
        return overridden;
    }

    public void setOverridden(Boolean overridden) {
        this.overridden = overridden;
    }

    @Override
    public int compareTo(AppPropertyEntity o) {
        return name.compareTo(o.name);
    }
}
