/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.security;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionValue;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.AbstractTreeDatasource;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.ui.BasicPermissionTarget;
import com.haulmont.cuba.security.entity.ui.PermissionVariant;
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
public class ScreenPermissionTreeDatasource extends AbstractTreeDatasource<BasicPermissionTarget, String> {

    private Tree<BasicPermissionTarget> screensTree;
    private CollectionDatasource<Permission, UUID> permissionDs;

    public ScreenPermissionTreeDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    protected Tree<BasicPermissionTarget> loadTree(Map params) {
        if (screensTree == null) {
            Tree<BasicPermissionTarget> screens =
                    AppContext.getBean(PermissionConfig.class).getScreens(UserSessionProvider.getLocale());

            List<Node<BasicPermissionTarget>> nodes = screens.getRootNode().getChildren();

            List<Node<BasicPermissionTarget>> clonedNodes = new ArrayList<Node<BasicPermissionTarget>>();
            for (Node<BasicPermissionTarget> node : nodes)
                clonedNodes.add(cloneNode(node));

            screensTree = new Tree<BasicPermissionTarget>(clonedNodes);
        }
        if (permissionDs != null)
            for (Node<BasicPermissionTarget> node : screensTree.getRootNodes())
                applyPermissions(node);
        // Set permission variants for targets
        return screensTree;
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
