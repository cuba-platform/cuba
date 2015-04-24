/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.ds;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionValue;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.AbstractTreeDatasource;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.gui.app.security.entity.BasicPermissionTarget;
import com.haulmont.cuba.gui.app.security.entity.PermissionVariant;
import org.apache.commons.lang.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public abstract class BasicPermissionTreeDatasource extends AbstractTreeDatasource<BasicPermissionTarget, String> {

    protected Tree<BasicPermissionTarget> permissionsTree;
    protected CollectionDatasource<Permission, UUID> permissionDs;

    @Override
    public boolean isModified() {
        return false;
    }

    public abstract Tree<BasicPermissionTarget> getPermissions();

    @Override
    protected Tree<BasicPermissionTarget> loadTree(Map params) {
        if (permissionDs == null)
            return new Tree<>();

        if (permissionsTree == null) {
            Tree<BasicPermissionTarget> permissions = getPermissions();

            List<Node<BasicPermissionTarget>> nodes = permissions.getRootNodes();

            List<Node<BasicPermissionTarget>> clonedNodes = new ArrayList<>();
            for (Node<BasicPermissionTarget> node : nodes)
                clonedNodes.add(cloneNode(node));

            permissionsTree = new Tree<>(clonedNodes);
        }
        if (permissionDs != null)
            for (Node<BasicPermissionTarget> node : permissionsTree.getRootNodes())
                applyPermissions(node);
        // Set permission variants for targets
        return permissionsTree;
    }

    private Node<BasicPermissionTarget> cloneNode(Node<BasicPermissionTarget> node) {
        Node<BasicPermissionTarget> clone = new Node<BasicPermissionTarget>();
        try {
            BasicPermissionTarget targetClone = node.data.clone();
            clone.setData(targetClone);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        for (Node<BasicPermissionTarget> childNode : node.getChildren()) {
            clone.addChild(cloneNode(childNode));
        }
        return clone;
    }

    private void applyPermissions(Node<BasicPermissionTarget> node) {
        loadPermissionVariant(node.data);
        for (Node<BasicPermissionTarget> child : node.getChildren()) {
            applyPermissions(child);
        }
    }

    private void loadPermissionVariant(BasicPermissionTarget target) {
        Permission permission = null;
        for (Permission p : permissionDs.getItems()) {
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