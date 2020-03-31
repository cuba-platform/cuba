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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.app.security.entity.AttributePermissionVariant;
import com.haulmont.cuba.gui.app.security.entity.MultiplePermissionTarget;
import com.haulmont.cuba.gui.app.security.role.edit.PropertyPermissionValue;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Predicate;

public class MultiplePermissionTargetsDatasource extends CollectionDatasourceImpl<MultiplePermissionTarget, String> {

    protected List<MultiplePermissionTarget> targets;

    protected Predicate<MultiplePermissionTarget> permissionsFilter;

    /**
     * Filters out entities which user doesn't have permission to any CRUD operation for
     */
    protected Predicate<MultiplePermissionTarget> permittedEntityFilter;

    protected CollectionDatasource<Permission, UUID> permissionDs;

    protected UserSessionSource userSessionSource;

    public MultiplePermissionTargetsDatasource() {
        userSessionSource = AppBeans.get(UserSessionSource.class);
        UserSession session = userSessionSource.getUserSession();
        permittedEntityFilter = target -> {
            if (target == null) {
                return false;
            }

            MetaClass metaClass = target.getEntityMetaClass();
            return session.isEntityOpPermitted(metaClass, EntityOp.READ)
                    || session.isEntityOpPermitted(metaClass, EntityOp.CREATE)
                    || session.isEntityOpPermitted(metaClass, EntityOp.DELETE)
                    || session.isEntityOpPermitted(metaClass, EntityOp.UPDATE);
        };
    }

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

        targets.stream()
                .filter(permittedEntityFilter)
                .filter(t -> permissionsFilter == null || permissionsFilter.test(t))
                .forEach(t -> data.put(t.getId(), t));
    }

    private void loadPermissionVariants(final MultiplePermissionTarget target) {
        for (Permission p : permissionDs.getItems()) {
            String permissionTargetString = p.getTarget();
            if (StringUtils.isNotEmpty(permissionTargetString)) {
                int delimiterIndex = permissionTargetString.lastIndexOf(Permission.TARGET_PATH_DELIMETER);
                if (delimiterIndex >= 0) {
                    String attribute = permissionTargetString.substring(delimiterIndex + 1);
                    String permissionTarget = permissionTargetString.substring(0, delimiterIndex);
                    if (Objects.equals(permissionTarget, target.getPermissionValue())) {
                        target.assignPermissionVariant(attribute, getPermissionVariant(p));
                    }
                }
            }
        }
    }

    private AttributePermissionVariant getPermissionVariant(Permission permission) {
        if (permission.getValue() == null)
            return AttributePermissionVariant.NOTSET;
        else if (permission.getValue() == PropertyPermissionValue.MODIFY.getValue())
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