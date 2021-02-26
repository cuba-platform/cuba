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

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.cuba.gui.app.security.entity.BasicPermissionTarget;
import com.haulmont.cuba.gui.app.security.entity.PermissionVariant;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionValue;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.AbstractTreeDatasource;
import com.haulmont.cuba.security.entity.Permission;

import java.util.*;

public abstract class BasicPermissionTreeDatasource extends AbstractTreeDatasource<BasicPermissionTarget, String> {

    protected static final String ROOT_PREFIX = "root:";
    protected static final String CATEGORY_PREFIX = "category:";

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

        Boolean filtering = (Boolean) params.get("filtering");
        if (filtering == null || filtering) {
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
        Node<BasicPermissionTarget> clone = new Node<>();
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
            if (Objects.equals(p.getTarget(), target.getPermissionValue())) {
                permission = p;
                break;
            }
        }
        if (permission != null && permission.getValue() != null) {
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

    protected boolean isRoot(BasicPermissionTarget target) {
        return target.getId().startsWith(ROOT_PREFIX);
    }

    protected boolean isCategory(BasicPermissionTarget target) {
        return target.getId().startsWith(CATEGORY_PREFIX);
    }
}