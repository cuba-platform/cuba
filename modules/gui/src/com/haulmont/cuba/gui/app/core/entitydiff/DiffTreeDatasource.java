/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.entitydiff;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.core.app.EntitySnapshotService;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.AbstractTreeDatasource;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class DiffTreeDatasource<T extends EntityPropertyDiff> extends AbstractTreeDatasource<T, UUID> {

    private static final long serialVersionUID = -6298466301673067199L;

    private EntityDiff entityDiff;

    public DiffTreeDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

    protected Tree loadTree(Map params) {
        Tree<EntityPropertyDiff> diffTree = new Tree<EntityPropertyDiff>();
        List<Node<EntityPropertyDiff>> rootNodes = new ArrayList<Node<EntityPropertyDiff>>();
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

    private Node<EntityPropertyDiff> loadPropertyDiff(EntityPropertyDiff propertyDiff) {
        Node<EntityPropertyDiff> diffNode = null;
        if (propertyDiff != null) {
            // check security
            String propName = propertyDiff.getViewProperty().getName();
            MetaClass propMetaClass = MetadataProvider.getSession().getClass(propertyDiff.getMetaClassName());
            UserSession userSession = UserSessionProvider.getUserSession();
            if (!userSession.isEntityOpPermitted(propMetaClass, EntityOp.READ))
                return diffNode;

            if (!userSession.isEntityAttrPermitted(propMetaClass, propName, EntityAttrAccess.VIEW))
                return diffNode;

            diffNode = new Node<EntityPropertyDiff>(propertyDiff);
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
        EntitySnapshotService snapshotService = ServiceLocator.lookup(EntitySnapshotService.NAME);
        entityDiff = snapshotService.getDifference(firstSnap, secondSnap);

        this.refresh();

        return entityDiff;
    }
}