/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.EmbeddableEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.entity.diff.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.ViewHelper;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Diff algorithm for Entities.
 *
 * @author artamonov
 * @version $Id$
 */
@ManagedBean("cuba_EntityDiffManager")
public class EntityDiffManager {

    @Inject
    private EntitySnapshotAPI snapshotAPI;

    @Inject
    private MetadataTools metadataTools;

    @Inject
    private ExtendedEntities extendedEntities;

    @Inject
    private Metadata metadata;

    private Log log = LogFactory.getLog(EntityDiffManager.class);

    public EntityDiff getDifference(EntitySnapshot first, EntitySnapshot second) {

        // Sort snapshots by date, first - old, second - new
        long firstTime = 0;
        if (first != null && first.getSnapshotDate() != null)
            firstTime = first.getSnapshotDate().getTime();

        long secondTime = 0;
        if (second != null && second.getSnapshotDate() != null)
            secondTime = second.getSnapshotDate().getTime();

        if (secondTime < firstTime) {
            EntitySnapshot temp = first;
            first = second;
            second = temp;
        }

        checkNotNull(second, "Diff could not be create for null snapshot");

        // Extract views
        View firstView = first != null ? snapshotAPI.extractView(first) : null;
        View secondView = snapshotAPI.extractView(second);

        // Get view for diff
        View diffView;
        if (firstView != null)
            diffView = ViewHelper.intersectViews(firstView, secondView);
        else
            diffView = secondView;

        // Diff
        return getDifferenceByView(first, second, diffView);
    }

    private EntityDiff getDifferenceByView(EntitySnapshot first, EntitySnapshot second, View diffView) {
        EntityDiff result = new EntityDiff(diffView);
        result.setBeforeSnapshot(first);
        result.setAfterSnapshot(second);

        if (!diffView.getProperties().isEmpty()) {
            BaseEntity firstEntity = first != null ? snapshotAPI.extractEntity(first) : null;
            BaseEntity secondEntity = snapshotAPI.extractEntity(second);

            result.setBeforeEntity(first);
            result.setAfterEntity(second);

            Stack<Object> diffBranch = new Stack<>();
            diffBranch.push(second);

            List<EntityPropertyDiff> propertyDiffs = getPropertyDiffs(diffView, firstEntity, secondEntity, diffBranch);
            result.setPropertyDiffs(propertyDiffs);
        }
        return result;
    }

    /**
     * Get diffs for entity properties
     *
     * @param diffView     View
     * @param firstEntity  First entity
     * @param secondEntity Second entity
     * @param diffBranch   Diff branch
     * @return Diff list
     */
    private List<EntityPropertyDiff> getPropertyDiffs(View diffView, Entity firstEntity, Entity secondEntity,
                                                      Stack<Object> diffBranch) {
        List<EntityPropertyDiff> propertyDiffs = new LinkedList<>();

        MetaClass viewMetaClass = metadata.getSession().getClass(diffView.getEntityClass());
        MetaClass metaClass = extendedEntities.getEffectiveMetaClass(viewMetaClass);

        Collection<MetaPropertyPath> metaProperties = metadataTools.getViewPropertyPaths(diffView, metaClass);

        for (MetaPropertyPath metaPropertyPath : metaProperties) {
            MetaProperty metaProperty = metaPropertyPath.getMetaProperty();

            if (!metadataTools.isTransient(metaProperty) && !metadataTools.isSystem(metaProperty)) {
                ViewProperty viewProperty = diffView.getProperty(metaProperty.getName());

                Object firstValue = firstEntity != null ? getPropertyValue(firstEntity, metaPropertyPath) : null;
                Object secondValue = secondEntity != null ? getPropertyValue(secondEntity, metaPropertyPath) : null;

                EntityPropertyDiff diff = getPropertyDifference(firstValue, secondValue, metaProperty, viewProperty, diffBranch);
                if (diff != null)
                    propertyDiffs.add(diff);
            }
        }

        Comparator<EntityPropertyDiff> comparator = new Comparator<EntityPropertyDiff>() {
            @Override
            public int compare(EntityPropertyDiff o1, EntityPropertyDiff o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
        Collections.sort(propertyDiffs, comparator);

        return propertyDiffs;
    }

    /**
     * Return difference between property values
     *
     * @param firstValue   First value
     * @param secondValue  Second value
     * @param metaProperty Meta Property
     * @param viewProperty View property
     * @param diffBranch   Branch with passed diffs
     * @return Diff
     */
    private EntityPropertyDiff getPropertyDifference(Object firstValue, Object secondValue,
                                                     MetaProperty metaProperty, ViewProperty viewProperty,
                                                     Stack<Object> diffBranch) {
        EntityPropertyDiff propertyDiff = null;

        Range range = metaProperty.getRange();
        if (range.isDatatype() || range.isEnum()) {
            // datatype
            if (!ObjectUtils.equals(firstValue, secondValue))
                propertyDiff = new EntityBasicPropertyDiff(viewProperty, metaProperty, firstValue, secondValue);

        } else if (range.getCardinality().isMany()) {
            propertyDiff = getCollectionDiff(firstValue, secondValue, viewProperty, metaProperty, diffBranch);

        } else if (range.isClass()) {
            propertyDiff = getClassDiff(firstValue, secondValue, viewProperty, metaProperty, diffBranch);
        }

        return propertyDiff;
    }

    private EntityPropertyDiff getClassDiff(@Nullable Object firstValue, @Nullable Object secondValue,
                                            ViewProperty viewProperty, MetaProperty metaProperty,
                                            Stack<Object> diffBranch) {
        EntityPropertyDiff propertyDiff = null;
        if (viewProperty.getView() != null) {
            // check exist value in diff branch
            if (!diffBranch.contains(secondValue)) {

                if (secondValue != null) {
                    // added or modified
                    propertyDiff = generateClassDiffFor(secondValue, firstValue, secondValue,
                            viewProperty, metaProperty, diffBranch);
                } else {
                    if (firstValue != null) {
                        // removed or set null
                        propertyDiff = generateClassDiffFor(firstValue, firstValue, secondValue,
                                viewProperty, metaProperty, diffBranch);
                    }
                }
            }
        } else {
            if ((firstValue != null) || (secondValue != null))
                log.debug("Not null values for (null) view ignored, property: " + metaProperty.getName() +
                        "in class" + metaProperty.getDeclaringClass().getCanonicalName());
        }
        return propertyDiff;
    }

    /**
     * Generate class difference for selected not null object
     *
     * @param diffObject   Object
     * @param firstValue   First value
     * @param secondValue  Second value
     * @param viewProperty View property
     * @param metaProperty Meta property
     * @param diffBranch   Diff branch
     * @return Property difference
     */
    private EntityPropertyDiff generateClassDiffFor(Object diffObject,
                                                    @Nullable Object firstValue, @Nullable Object secondValue,
                                                    ViewProperty viewProperty, MetaProperty metaProperty,
                                                    Stack<Object> diffBranch) {
        // link
        boolean isLinkChange = !ObjectUtils.equals(firstValue, secondValue);
        isLinkChange = !(diffObject instanceof EmbeddableEntity) && isLinkChange;

        EntityClassPropertyDiff classPropertyDiff = new EntityClassPropertyDiff(firstValue, secondValue,
                viewProperty, metaProperty, isLinkChange);

        boolean isInternalChange = false;
        diffBranch.push(diffObject);

        List<EntityPropertyDiff> propertyDiffs =
                getPropertyDiffs(viewProperty.getView(), (Entity) firstValue, (Entity) secondValue, diffBranch);

        diffBranch.pop();

        if (!propertyDiffs.isEmpty()) {
            isInternalChange = true;
            classPropertyDiff.setPropertyDiffs(propertyDiffs);
        }

        if (isInternalChange || isLinkChange)
            return classPropertyDiff;
        else
            return null;
    }

    private EntityPropertyDiff getCollectionDiff(Object firstValue, Object secondValue,
                                                 ViewProperty viewProperty, MetaProperty metaProperty,
                                                 Stack<Object> diffBranch) {
        EntityPropertyDiff propertyDiff = null;

        Collection<Entity> addedEntities = new LinkedList<>();
        Collection<Entity> removedEntities = new LinkedList<>();
        Collection<Pair<Entity, Entity>> modifiedEntities = new LinkedList<>();

        // collection
        Collection firstCollection = getCollection(firstValue);
        Collection secondCollection = getCollection(secondValue);

        // added or modified
        for (Object item : secondCollection) {
            Entity secondEntity = (Entity) item;
            Entity firstEntity = getRelatedItem(firstCollection, secondEntity);
            if (firstEntity == null)
                addedEntities.add(secondEntity);
            else
                modifiedEntities.add(new Pair<Entity, Entity>(firstEntity, secondEntity));
        }

        // removed
        for (Object item : firstCollection) {
            Entity firstEntity = (Entity) item;
            Entity secondEntity = getRelatedItem(secondCollection, firstEntity);
            if (secondEntity == null)
                removedEntities.add(firstEntity);
        }

        boolean changed = !(addedEntities.isEmpty() && removedEntities.isEmpty() && modifiedEntities.isEmpty());
        if (changed) {
            EntityCollectionPropertyDiff diff = new EntityCollectionPropertyDiff(viewProperty, metaProperty);

            for (Entity entity : addedEntities) {
                EntityPropertyDiff addedDiff = getClassDiff(null, entity, viewProperty, metaProperty, diffBranch);
                if (addedDiff != null) {
                    addedDiff.setName(InstanceUtils.getInstanceName(entity));
                    addedDiff.setItemState(EntityPropertyDiff.ItemState.Added);
                    diff.getAddedEntities().add(addedDiff);
                }
            }
            // check modified
            for (Pair<Entity, Entity> entityPair : modifiedEntities) {
                EntityPropertyDiff modifiedDiff = getClassDiff(entityPair.getFirst(), entityPair.getSecond(),
                        viewProperty, metaProperty, diffBranch);
                if (modifiedDiff != null) {
                    modifiedDiff.setName(InstanceUtils.getInstanceName(entityPair.getSecond()));
                    modifiedDiff.setItemState(EntityPropertyDiff.ItemState.Modified);
                    diff.getModifiedEntities().add(modifiedDiff);
                }
            }
            // check removed
            for (Entity entity : removedEntities) {
                EntityPropertyDiff removedDiff = getClassDiff(entity, null, viewProperty, metaProperty, diffBranch);
                if (removedDiff != null) {
                    removedDiff.setName(InstanceUtils.getInstanceName(entity));
                    removedDiff.setItemState(EntityPropertyDiff.ItemState.Removed);
                    diff.getRemovedEntities().add(removedDiff);
                }
            }

            boolean empty = diff.getAddedEntities().isEmpty()
                    && diff.getModifiedEntities().isEmpty()
                    && diff.getRemovedEntities().isEmpty();
            if (!empty)
                propertyDiff = diff;
        }
        return propertyDiff;
    }

    private Entity getRelatedItem(Collection collection, Entity entity) {
        for (Object item : collection) {
            Entity itemEntity = (Entity) item;
            if (entity.getId().equals(itemEntity.getId()))
                return itemEntity;
        }
        return null;
    }

    private Collection getCollection(Object value) {
        Collection collection;
        if (value == null)
            collection = Collections.emptyList();
        else
            collection = (Collection) value;
        return collection;
    }

    private Object getPropertyValue(Entity entity, MetaPropertyPath propertyPath) {
        return entity.getValue(propertyPath.toString());
    }
}