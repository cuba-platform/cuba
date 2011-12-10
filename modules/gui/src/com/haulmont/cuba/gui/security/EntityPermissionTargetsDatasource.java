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
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.ui.OperationPermissionTarget;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EntityPermissionTargetsDatasource extends CollectionDatasourceImpl<OperationPermissionTarget, String> {
    public EntityPermissionTargetsDatasource(
            DsContext context, DataService dataservice,
            String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

    private Predicate<OperationPermissionTarget> filter;

    private CollectionDatasource<Permission, UUID> permissionDs;

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

    @Override
    protected void loadData(Map<String, Object> params) {
        List<OperationPermissionTarget> targets =
                AppContext.getBean(PermissionConfig.class).getEntities(UserSessionProvider.getLocale());

        if (filter != null) {
            for (OperationPermissionTarget target : targets) {
                if (filter.apply(target))
                    data.put(target, target.getId());
            }
        } else {
            for (OperationPermissionTarget target : targets) {
                data.put(target, target.getId());
            }
        }
    }
}
