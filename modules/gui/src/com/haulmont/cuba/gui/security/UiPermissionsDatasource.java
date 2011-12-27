/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.security;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionValue;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.GroupDatasourceImpl;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.ui.UiPermissionTarget;
import com.haulmont.cuba.security.entity.ui.UiPermissionVariant;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class UiPermissionsDatasource extends GroupDatasourceImpl<UiPermissionTarget, String> {

    private CollectionDatasource<Permission, UUID> permissionDs;

    public UiPermissionsDatasource(DsContext context, DataService dataservice,
                                   String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    protected void loadData(Map<String, Object> params) {
        if (permissionDs == null)
            return;

        if (!data.isEmpty())
            return;

        clear();

        for (UUID id : permissionDs.getItemIds()) {
            Permission p = permissionDs.getItem(id);
            String permissionTarget = p.getTarget();
            if (StringUtils.isNotEmpty(permissionTarget)) {
                int delimeterIndex = permissionTarget.lastIndexOf(Permission.TARGET_PATH_DELIMETER);
                if (delimeterIndex >= 0) {
                    String component = permissionTarget.substring(delimeterIndex + 1);
                    String screen = permissionTarget.substring(0, delimeterIndex);
                    UiPermissionVariant permissionVariant = getPermissionVariant(p);
                    String permissionValue = screen + Permission.TARGET_PATH_DELIMETER + component;
                    UiPermissionTarget target = new UiPermissionTarget("ui:" + permissionValue,
                            permissionValue, permissionValue);

                    target.setPermissionVariant(permissionVariant);
                    target.setComponent(component);
                    target.setScreen(screen);

                    includeItem(target);
                }
            }
        }
    }

    private UiPermissionVariant getPermissionVariant(Permission permission) {
        if (permission.getValue() == UiPermissionValue.READ_ONLY.getValue())
            return UiPermissionVariant.READ_ONLY;
        else if (permission.getValue() == UiPermissionValue.HIDE.getValue())
            return UiPermissionVariant.HIDE;
        else
            return UiPermissionVariant.NOTSET;
    }

    public CollectionDatasource<Permission, UUID> getPermissionDs() {
        return permissionDs;
    }

    public void setPermissionDs(CollectionDatasource<Permission, UUID> permissionDs) {
        this.permissionDs = permissionDs;
    }
}