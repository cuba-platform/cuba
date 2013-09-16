/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.security;

import com.google.common.base.Predicate;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.app.security.role.edit.PropertyPermissionValue;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.gui.security.entity.AttributePermissionVariant;
import com.haulmont.cuba.gui.security.entity.MultiplePermissionTarget;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public class MultiplePermissionTargetsDatasource extends CollectionDatasourceImpl<MultiplePermissionTarget, String> {

    private List<MultiplePermissionTarget> targets;

    private Predicate<MultiplePermissionTarget> filter;

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
            List<MultiplePermissionTarget> entityAttrs = permissionConfig.getEntityAttributes(UserSessionProvider.getLocale());
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
            if ((filter == null) || (filter.apply(target)))
                data.put(target.getId(), target);
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
        return filter;
    }

    public void setFilter(Predicate<MultiplePermissionTarget> filter) {
        this.filter = filter;
    }

    public CollectionDatasource<Permission, UUID> getPermissionDs() {
        return permissionDs;
    }

    public void setPermissionDs(CollectionDatasource<Permission, UUID> permissionDs) {
        this.permissionDs = permissionDs;
    }
}