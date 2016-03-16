/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.google.common.base.Predicate;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.app.security.entity.AssignableTarget;
import com.haulmont.cuba.gui.app.security.entity.EntityPermissionTarget;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;

/**
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