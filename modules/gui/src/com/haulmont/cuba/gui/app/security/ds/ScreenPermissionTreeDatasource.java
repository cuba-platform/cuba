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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ScreenPermissionTreeDatasource extends BasicPermissionTreeDatasource {

    protected PermissionConfig permissionConfig = AppBeans.get(PermissionConfig.class);
    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
    protected Predicate<BasicPermissionTarget> screenFilter;

    @Override
    public Tree<BasicPermissionTarget> getPermissions() {
        Tree<BasicPermissionTarget> allPermissions = permissionConfig.getScreens(userSessionSource.getLocale());
        return new Tree<>(collectSuitableNodes(allPermissions.getRootNodes()));
    }

    protected List<Node<BasicPermissionTarget>> collectSuitableNodes(List<Node<BasicPermissionTarget>> nodes) {
        List<Node<BasicPermissionTarget>> suitableNodes = new ArrayList<>();

        for (Node<BasicPermissionTarget> node : nodes) {
            if (node.getChildren().isEmpty() && suitableNode(node)) {
                suitableNodes.add(node);
            } else {
                List<Node<BasicPermissionTarget>> suitableChildren = collectSuitableNodes(node.getChildren());
                if ((isRoot(node.getData()) || isCategory(node.getData()) || permittedNode(node))
                        && !suitableChildren.isEmpty()) {
                    Node<BasicPermissionTarget> filteredNode = new Node<>(node.getData());
                    filteredNode.setChildren(suitableChildren);
                    suitableNodes.add(filteredNode);
                }
            }
        }

        return suitableNodes;
    }

    protected boolean permittedNode(Node<BasicPermissionTarget> node) {
        return userSession.isScreenPermitted(node.getData().getPermissionValue());
    }

    protected boolean suitableNode(Node<BasicPermissionTarget> node) {
        return permittedNode(node) &&
                (screenFilter == null || screenFilter.test(node.getData()));
    }

    public void setFilter(Predicate<BasicPermissionTarget> filter) {
        this.screenFilter = filter;
    }
}