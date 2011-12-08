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
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionTarget;
import com.haulmont.cuba.security.entity.PermissionVariant;
import org.apache.commons.lang.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ScreenPermissionTreeDatasource extends ScreenPermissionTargetsDatasource {

    private Tree<PermissionTarget> screensTree;
    private CollectionDatasource<Permission, UUID> permissionDs;

    public ScreenPermissionTreeDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

    @Override
    protected Tree<PermissionTarget> loadTree(Map params) {
        if (screensTree == null) {
            List<Node<PermissionTarget>> nodes = super.loadTree(params).getRootNode().getChildren();

            List<Node<PermissionTarget>> clonedNodes = new ArrayList<Node<PermissionTarget>>();
            for (Node<PermissionTarget> node : nodes)
                clonedNodes.add(cloneNode(node));

            screensTree = new Tree<PermissionTarget>(clonedNodes);
        }
        if (permissionDs != null)
            for (Node<PermissionTarget> node : screensTree.getRootNodes())
                applyPermissions(node);
        // Set permission variants for targets
        return screensTree;
    }

    private Node<PermissionTarget> cloneNode(Node<PermissionTarget> node) {
        Node<PermissionTarget> clone = new Node<PermissionTarget>();
        clone.setData(cloneTarget(node.data));
        for (Node<PermissionTarget> childNode : node.getChildren()) {
            clone.addChild(cloneNode(childNode));
        }
        return clone;
    }

    private PermissionTarget cloneTarget(PermissionTarget target) {
        return new PermissionTarget(
                target.getId(), target.getCaption(),
                target.getPermissionValue(), target.getPermissionVariant());
    }

    private void applyPermissions(Node<PermissionTarget> node) {
        loadPermissionVariant(node.data);
        for (Node<PermissionTarget> child : node.getChildren()) {
            applyPermissions(child);
        }
    }

    private void loadPermissionVariant(PermissionTarget target) {
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
