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
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.app.security.entity.BasicPermissionTarget;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.security.global.UserSession;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SpecificPermissionTreeDatasource extends BasicPermissionTreeDatasource {

    protected PermissionConfig permissionConfig = AppBeans.get(PermissionConfig.class);
    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
    protected UserSessionSource uss = AppBeans.get(UserSessionSource.class);

    @Override
    public Tree<BasicPermissionTarget> getPermissions() {
        Tree<BasicPermissionTarget> allPermissions = permissionConfig.getSpecific(userSessionSource.getLocale());
        return filterPermitted(allPermissions);
    }

    private Tree<BasicPermissionTarget> filterPermitted(Tree<BasicPermissionTarget> permissions) {
        UserSession session = uss.getUserSession();
        List<Node<BasicPermissionTarget>> newRootNodes = permissions.getRootNodes().stream()
                .map(root -> filterNode(session, root))
                .filter(root -> root.getNumberOfChildren() > 0) //empty nodes
                .collect(Collectors.toCollection(LinkedList::new));
        return new Tree<>(newRootNodes);
    }

    private Node<BasicPermissionTarget> filterNode(UserSession session, Node<BasicPermissionTarget> rootNode) {
        Node<BasicPermissionTarget> filteredRootNode = new Node<>(rootNode.getData());
        rootNode.getChildren().stream()
                .filter(child -> isCategory(child.getData())
                        || session.isSpecificPermitted(child.getData().getPermissionValue()))
                .map(child -> filterNode(session, child))
                .filter(child -> child.getNumberOfChildren() > 0 || !isCategory(child.getData())) //filtering out empty categories
                .forEach(filteredRootNode::addChild);
        return filteredRootNode;
    }
}
