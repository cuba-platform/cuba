/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.security;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionValue;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.config.PermissionVariant;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.security.entity.Permission;
import org.apache.commons.lang.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ScreenPermissionTreeDatasource extends ScreenPermissionTargetsDatasource {

    private Tree<PermissionConfig.Target> screensTree;
    private CollectionDatasource<Permission, UUID> permissionDs;

    public ScreenPermissionTreeDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

    @Override
    protected Tree<PermissionConfig.Target> loadTree(Map params) {
        if (screensTree == null) {
            List<Node<PermissionConfig.Target>> nodes = super.loadTree(params).getRootNode().getChildren();
            screensTree = new Tree<PermissionConfig.Target>(nodes);
        }
        if (permissionDs != null)
            for (Node<PermissionConfig.Target> node : screensTree.getRootNodes())
                applyPermissions(node);
        // Set permission variants for targets
        return screensTree;
    }

    private void applyPermissions(Node<PermissionConfig.Target> node) {
        loadPermissionVariant(node.data);
        for (Node<PermissionConfig.Target> child : node.getChildren()) {
            applyPermissions(child);
        }
    }

    private void loadPermissionVariant(PermissionConfig.Target target) {
        Permission permission = null;
        for (UUID id : permissionDs.getItemIds()) {
            Permission p = permissionDs.getItem(id);
            if (ObjectUtils.equals(p.getTarget(), target.getPermissionValue())) {
                permission = p;
                break;
            }
        }
        if (permission != null) {
            if (permission.getValue() == PermissionValue.ALLOW.getValue())
                target.setPermissionVariant(PermissionVariant.ALLOWED);
            else if (permission.getValue() == PermissionValue.DENY.getValue())
                target.setPermissionVariant(PermissionVariant.DISALLOWED);
        } else {
            target.setPermissionVariant(PermissionVariant.NOTSET);
        }
    }

    public CollectionDatasource<Permission, UUID> getPermissionDs() {
        return permissionDs;
    }

    public void setPermissionDs(CollectionDatasource<Permission, UUID> permissionDs) {
        this.permissionDs = permissionDs;
    }
}
