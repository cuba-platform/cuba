/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.google.common.base.Predicate;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.security.entity.AssignableTarget;
import com.haulmont.cuba.gui.security.entity.EntityPermissionTarget;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;

/**
 * @author artamonov
 * @version $Id$
 */
public class EntityNameFilter<T extends AssignableTarget> implements Predicate<T> {

    protected Metadata metadata;

    protected final CheckBox assignedOnlyCheckBox;
    protected final CheckBox systemLevelCheckBox;

    protected final TextField entityFilter;

    public EntityNameFilter(Metadata metadata, CheckBox assignedOnlyCheckBox, CheckBox systemLevelCheckBox,
                            TextField entityFilter) {
        this.metadata = metadata;
        this.assignedOnlyCheckBox = assignedOnlyCheckBox;
        this.systemLevelCheckBox = systemLevelCheckBox;
        this.entityFilter = entityFilter;
    }

    @Override
    public boolean apply(@Nullable T target) {
        if (target != null) {
            if (Boolean.TRUE.equals(assignedOnlyCheckBox.getValue()) && !target.isAssigned()) {
                return false;
            }

            if (Boolean.FALSE.equals(systemLevelCheckBox.getValue())
                    && (target instanceof EntityPermissionTarget)
                    && !target.isAssigned()) {
                Class entityClass = ((EntityPermissionTarget) target).getEntityClass();
                MetaClass metaClass = metadata.getSession().getClassNN(entityClass);
                if (metadata.getTools().isSystemLevel(metaClass)) {
                    return false;
                }
            }

            String filterValue = StringUtils.trimToEmpty(entityFilter.<String>getValue());
            return StringUtils.isBlank(filterValue)
                    || StringUtils.containsIgnoreCase(target.getCaption(), filterValue);
        }
        return false;
    }
}