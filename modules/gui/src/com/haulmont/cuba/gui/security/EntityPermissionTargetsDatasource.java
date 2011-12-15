/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.03.2009 11:52:34
 * $Id$
 */

package com.haulmont.cuba.gui.security;

import com.google.common.base.Predicate;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionValue;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.ui.OperationPermissionTarget;
import com.haulmont.cuba.security.ui.PermissionVariant;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EntityPermissionTargetsDatasource extends CollectionDatasourceImpl<OperationPermissionTarget, String> {

    private List<OperationPermissionTarget> targets;

    public EntityPermissionTargetsDatasource(
            DsContext context, DataService dataservice,
            String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

    private Predicate<OperationPermissionTarget> filter;

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
            targets = new ArrayList<OperationPermissionTarget>();
            PermissionConfig permissionConfig = AppContext.getBean(PermissionConfig.class);
            List<OperationPermissionTarget> entities = permissionConfig.getEntities(UserSessionProvider.getLocale());
            for (OperationPermissionTarget target : entities) {
                try {
                    OperationPermissionTarget cloneTarget = target.clone();
                    loadPermissionVariants(cloneTarget);
                    attachListener(cloneTarget);
                    targets.add(cloneTarget);
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        data.clear();

        for (OperationPermissionTarget target : targets) {
            if ((filter == null) || (filter.apply(target)))
                data.put(target.getId(), target);
        }
    }

    private void loadPermissionVariants(OperationPermissionTarget target) {
        for (UUID id : permissionDs.getItemIds()) {
            Permission p = permissionDs.getItem(id);
            String permissionTarget = p.getTarget();
            if (StringUtils.isNotEmpty(permissionTarget) && permissionTarget.startsWith(target.getPermissionValue())) {
                int delimeterIndex = permissionTarget.lastIndexOf(Permission.TARGET_PATH_DELIMETER);
                if (delimeterIndex >= 0) {
                    String variant = permissionTarget.substring(delimeterIndex + 1);
                    PermissionVariant permissionVariant = getPermissionVariant(p);
                    if (EntityOp.CREATE.getId().equals(variant)) {
                        target.setCreatePermissionVariant(permissionVariant);
                    } else if (EntityOp.READ.getId().equals(variant)) {
                        target.setReadPermissionVariant(permissionVariant);
                    } else if (EntityOp.UPDATE.getId().equals(variant)) {
                        target.setUpdatePermissionVariant(permissionVariant);
                    } else if (EntityOp.DELETE.getId().equals(variant)) {
                        target.setDeletePermissionVariant(permissionVariant);
                    }
                }
            }
        }
    }

    private PermissionVariant getPermissionVariant(Permission permission) {
        if (permission.getValue() == PermissionValue.ALLOW.getValue())
            return PermissionVariant.ALLOWED;
        else if (permission.getValue() == PermissionValue.DENY.getValue())
            return PermissionVariant.DISALLOWED;
        else
            return PermissionVariant.NOTSET;
    }

    public Predicate<OperationPermissionTarget> getFilter() {
        return filter;
    }

    public void setFilter(Predicate<OperationPermissionTarget> filter) {
        this.filter = filter;
    }

    public CollectionDatasource<Permission, UUID> getPermissionDs() {
        return permissionDs;
    }

    public void setPermissionDs(CollectionDatasource<Permission, UUID> permissionDs) {
        this.permissionDs = permissionDs;
    }
}
