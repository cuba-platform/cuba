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

package com.haulmont.cuba.gui.app.security.ds;

import com.google.common.base.Predicate;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.app.security.entity.AttributePermissionVariant;
import com.haulmont.cuba.gui.app.security.entity.MultiplePermissionTarget;
import com.haulmont.cuba.gui.app.security.role.edit.PropertyPermissionValue;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.security.entity.Permission;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 */
public class MultiplePermissionTargetsDatasource extends CollectionDatasourceImpl<MultiplePermissionTarget, String> {

    private List<MultiplePermissionTarget> targets;

    private Predicate<MultiplePermissionTarget> permissionsFilter;

    private CollectionDatasource<Permission, UUID> permissionDs;

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    protected void loadData(Map<String, Object> params) {
        if (permissionDs == null)
            return;

        if (targets == null) {
            targets = new ArrayList<>();
            PermissionConfig permissionConfig = AppBeans.get(PermissionConfig.class);
            List<MultiplePermissionTarget> entityAttrs = permissionConfig.getEntityAttributes(AppBeans.get(UserSessionSource.class).getLocale());
            for (MultiplePermissionTarget target : entityAttrs) {
                try {
                    MultiplePermissionTarget cloneTarget = target.clone();
                    loadPermissionVariants(cloneTarget);
                    attachListener(cloneTarget);
                    targets.add(cloneTarget);
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        clear();

        for (MultiplePermissionTarget target : targets) {
            if ((permissionsFilter == null) || (permissionsFilter.apply(target))) {
                data.put(target.getId(), target);
            }
        }
    }

    private void loadPermissionVariants(final MultiplePermissionTarget target) {
        for (Permission p : permissionDs.getItems()) {
            String permissionTargetString = p.getTarget();
            if (StringUtils.isNotEmpty(permissionTargetString)) {
                int delimeterIndex = permissionTargetString.lastIndexOf(Permission.TARGET_PATH_DELIMETER);
                if (delimeterIndex >= 0) {
                    String attribute = permissionTargetString.substring(delimeterIndex + 1);
                    String permissionTarget = permissionTargetString.substring(0, delimeterIndex);
                    if (StringUtils.equals(permissionTarget, target.getPermissionValue())) {
                        target.assignPermissionVariant(attribute, getPermissionVariant(p));
                    }
                }
            }
        }
    }

    private AttributePermissionVariant getPermissionVariant(Permission permission) {
        if (permission.getValue() == PropertyPermissionValue.MODIFY.getValue())
            return AttributePermissionVariant.MODIFY;
        else if (permission.getValue() == PropertyPermissionValue.VIEW.getValue())
            return AttributePermissionVariant.READ_ONLY;
        else if (permission.getValue() == PropertyPermissionValue.DENY.getValue())
            return AttributePermissionVariant.HIDE;
        else
            return AttributePermissionVariant.NOTSET;
    }

    public Predicate<MultiplePermissionTarget> getFilter() {
        return permissionsFilter;
    }

    public void setFilter(Predicate<MultiplePermissionTarget> filter) {
        this.permissionsFilter = filter;
    }

    public CollectionDatasource<Permission, UUID> getPermissionDs() {
        return permissionDs;
    }

    public void setPermissionDs(CollectionDatasource<Permission, UUID> permissionDs) {
        this.permissionDs = permissionDs;
    }
}