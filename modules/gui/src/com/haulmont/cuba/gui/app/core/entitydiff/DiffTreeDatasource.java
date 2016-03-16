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

package com.haulmont.cuba.gui.app.core.entitydiff;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.EntitySnapshotService;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.entity.diff.EntityClassPropertyDiff;
import com.haulmont.cuba.core.entity.diff.EntityCollectionPropertyDiff;
import com.haulmont.cuba.core.entity.diff.EntityDiff;
import com.haulmont.cuba.core.entity.diff.EntityPropertyDiff;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.data.impl.AbstractTreeDatasource;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 */
public class DiffTreeDatasource extends AbstractTreeDatasource<EntityPropertyDiff, UUID> {

    protected EntityDiff entityDiff;

    @Override
    protected Tree<EntityPropertyDiff> loadTree(Map params) {
        Tree<EntityPropertyDiff> diffTree = new Tree<>();
        List<Node<EntityPropertyDiff>> rootNodes = new ArrayList<>();
        if (entityDiff != null) {
            for (EntityPropertyDiff childPropertyDiff : entityDiff.getPropertyDiffs()) {
                Node<EntityPropertyDiff> childPropDiffNode = loadPropertyDiff(childPropertyDiff);

                if (childPropDiffNode != null)
                    rootNodes.add(childPropDiffNode);
            }
        }
        diffTree.setRootNodes(rootNodes);
        return diffTree;
    }

    protected Node<EntityPropertyDiff> loadPropertyDiff(EntityPropertyDiff propertyDiff) {
        Node<EntityPropertyDiff> diffNode = null;
        if (propertyDiff != null) {
            // check security
            String propName = propertyDiff.getViewProperty().getName();
            MetaClass propMetaClass = metadata.getSession().getClass(propertyDiff.getMetaClassName());

            Security security = AppBeans.get(Security.NAME);
            if (!security.isEntityOpPermitted(propMetaClass, EntityOp.READ)
                || !security.isEntityAttrPermitted(propMetaClass, propName, EntityAttrAccess.VIEW)) {
                return null;
            }

            diffNode = new Node<>(propertyDiff);
            if (propertyDiff instanceof EntityClassPropertyDiff) {

                EntityClassPropertyDiff classPropertyDiff = (EntityClassPropertyDiff) propertyDiff;
                for (EntityPropertyDiff childPropertyDiff : classPropertyDiff.getPropertyDiffs()) {
                    Node<EntityPropertyDiff> childPropDiffNode = loadPropertyDiff(childPropertyDiff);
                    if (childPropDiffNode != null)
                        diffNode.addChild(childPropDiffNode);
                }
            } else if (propertyDiff instanceof EntityCollectionPropertyDiff) {
                EntityCollectionPropertyDiff collectionPropertyDiff = (EntityCollectionPropertyDiff) propertyDiff;
                for (EntityPropertyDiff childPropertyDiff : collectionPropertyDiff.getAddedEntities()) {
                    Node<EntityPropertyDiff> childPropDiffNode = loadPropertyDiff(childPropertyDiff);
                    if (childPropDiffNode != null)
                        diffNode.addChild(childPropDiffNode);
                }

                for (EntityPropertyDiff childPropertyDiff : collectionPropertyDiff.getModifiedEntities()) {
                    Node<EntityPropertyDiff> childPropDiffNode = loadPropertyDiff(childPropertyDiff);
                    if (childPropDiffNode != null)
                        diffNode.addChild(childPropDiffNode);
                }

                for (EntityPropertyDiff childPropertyDiff : collectionPropertyDiff.getRemovedEntities()) {
                    Node<EntityPropertyDiff> childPropDiffNode = loadPropertyDiff(childPropertyDiff);
                    if (childPropDiffNode != null)
                        diffNode.addChild(childPropDiffNode);
                }
            }
        }
        return diffNode;
    }

    public EntityDiff loadDiff(EntitySnapshot firstSnap, EntitySnapshot secondSnap) {
        EntitySnapshotService snapshotService = AppBeans.get(EntitySnapshotService.NAME);
        entityDiff = snapshotService.getDifference(firstSnap, secondSnap);

        this.refresh();

        return entityDiff;
    }
}