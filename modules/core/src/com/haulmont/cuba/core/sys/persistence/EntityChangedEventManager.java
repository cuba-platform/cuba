/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.app.events.AttributeChanges;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.entity.annotation.PublishEntityChangedEvents;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.Metadata;
import org.eclipse.persistence.descriptors.changetracking.ChangeTracker;
import org.eclipse.persistence.internal.descriptors.changetracking.AttributeChangeListener;
import org.eclipse.persistence.sessions.changesets.AggregateChangeRecord;
import org.eclipse.persistence.sessions.changesets.ChangeRecord;
import org.eclipse.persistence.sessions.changesets.ObjectChangeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component(EntityChangedEventManager.NAME)
public class EntityChangedEventManager {

    public static final String NAME = "cuba_EntityChangedEventManager";

    private static final Logger log = LoggerFactory.getLogger(EntityChangedEventManager.class);

    @Inject
    private Metadata metadata;

    @Inject
    private Events eventPublisher;

    private static class PublishingInfo {
        final boolean publish;
        final boolean onCreated;
        final boolean onUpdated;
        final boolean onDeleted;

        public PublishingInfo() {
            publish = false;
            onCreated = false;
            onUpdated = false;
            onDeleted = false;
        }

        public PublishingInfo(boolean onCreated, boolean onUpdated, boolean onDeleted) {
            this.publish = true;
            this.onCreated = onCreated;
            this.onUpdated = onUpdated;
            this.onDeleted = onDeleted;
        }
    }

    private Map<Class, PublishingInfo> infoCache = new ConcurrentHashMap<>();

    public List<EntityChangedEvent> collect(Collection<Entity> entities) {
        List<EntityChangedEvent> list = new ArrayList<>();
        for (Entity entity : entities) {

            PublishingInfo info = infoCache.computeIfAbsent(entity.getClass(), aClass -> {
                MetaClass metaClass = metadata.getClassNN(entity.getClass());
                Map attrMap = (Map) metaClass.getAnnotations().get(PublishEntityChangedEvents.class.getName());
                if (attrMap != null) {
                    if (!(entity instanceof BaseGenericIdEntity)) {
                        log.warn("Cannot publish EntityChangedEvent for {} because it is not a BaseGenericIdEntity", entity);
                    } else {
                        return new PublishingInfo(
                                Boolean.TRUE.equals(attrMap.get("created")),
                                Boolean.TRUE.equals(attrMap.get("updated")),
                                Boolean.TRUE.equals(attrMap.get("deleted")));
                    }
                }
                return new PublishingInfo();
            });


            if (info.publish) {
                EntityChangedEvent.Type type = null;
                AttributeChanges attributeChanges = null;
                if (info.onCreated && BaseEntityInternalAccess.isNew((BaseGenericIdEntity) entity)) {
                    type = EntityChangedEvent.Type.CREATED;
                    attributeChanges = getEntityAttributeChanges(entity, false);
                } else {
                    if (info.onUpdated || info.onDeleted) {
                        AttributeChangeListener changeListener =
                                (AttributeChangeListener) ((ChangeTracker) entity)._persistence_getPropertyChangeListener();
                        if (changeListener == null) {
                            log.warn("Cannot publish EntityChangedEvent for {} because its AttributeChangeListener is null", entity);
                            continue;
                        }
                        if (info.onDeleted && PersistenceImplSupport.isDeleted((BaseGenericIdEntity) entity, changeListener)) {
                            type = EntityChangedEvent.Type.DELETED;
                            attributeChanges = getEntityAttributeChanges(entity, true);
                        } else if (info.onUpdated && changeListener.hasChanges()) {
                            type = EntityChangedEvent.Type.UPDATED;
                            attributeChanges = getEntityAttributeChanges(entity, changeListener.getObjectChangeSet());
                        }
                    }
                }
                if (type != null) {
                    @SuppressWarnings("unchecked")
                    EntityChangedEvent event = new EntityChangedEvent(this, Id.of(entity), type, attributeChanges);
                    list.add(event);
                }
            }
        }
        return list;
    }

    public void publish(Collection<EntityChangedEvent> events) {
        for (EntityChangedEvent event : events) {
            eventPublisher.publish(event);
        }
    }

    @SuppressWarnings("unchecked")
    private AttributeChanges getEntityAttributeChanges(@Nullable Entity entity, ObjectChangeSet changeSet) {
        if (changeSet == null)
            return null;
        Set<AttributeChanges.Change> changes = new HashSet<>();
        Map<String, AttributeChanges> embeddedChanges = new HashMap<>();

        for (ChangeRecord changeRecord : changeSet.getChanges()) {
            if (changeRecord instanceof AggregateChangeRecord) {
                embeddedChanges.computeIfAbsent(changeRecord.getAttribute(), s ->
                        getEntityAttributeChanges(null, ((AggregateChangeRecord) changeRecord).getChangedObject()));
            } else {
                Object oldValue = changeRecord.getOldValue();
                if (oldValue instanceof Entity) {
                    changes.add(new AttributeChanges.Change(changeRecord.getAttribute(), Id.of((Entity) oldValue)));
                } else if (oldValue instanceof Collection) {
                    Collection<Entity> coll = (Collection<Entity>) oldValue;
                    Collection<Id> idColl = oldValue instanceof List ? new ArrayList<>() : new LinkedHashSet<>();
                    for (Entity item : coll) {
                        idColl.add(Id.of(item));
                    }
                    changes.add(new AttributeChanges.Change(changeRecord.getAttribute(), idColl));
                } else {
                    changes.add(new AttributeChanges.Change(changeRecord.getAttribute(), oldValue));
                }
            }
        }

        addDynamicAttributeChanges(entity, changes, false);

        return new AttributeChanges(changes, embeddedChanges);
    }

    @SuppressWarnings("unchecked")
    private void addDynamicAttributeChanges(@Nullable Entity entity, Set<AttributeChanges.Change> changes, boolean deleted) {
        if (entity instanceof BaseGenericIdEntity && ((BaseGenericIdEntity) entity).getDynamicAttributes() != null) {
            Map<String, CategoryAttributeValue> dynamicAttributes = ((BaseGenericIdEntity) entity).getDynamicAttributes();
            if (dynamicAttributes == null) {
                throw new RuntimeException("Entity dynamicAttributes is null");
            }

            for (CategoryAttributeValue cav : dynamicAttributes.values()) {
                if (BaseEntityInternalAccess.isNew(cav)) {
                    changes.add(new AttributeChanges.Change(DynamicAttributesUtils.encodeAttributeCode(cav.getCode()), null));
                } else {
                    if (deleted) {
                        Object oldValue;
                        switch (cav.getCategoryAttribute().getDataType()) {
                            case STRING:
                            case ENUMERATION:
                                oldValue = cav.getStringValue();
                                break;
                            case INTEGER:
                                oldValue = cav.getIntValue();
                                break;
                            case DOUBLE:
                                oldValue = cav.getDoubleValue();
                                break;
                            case BOOLEAN:
                                oldValue = cav.getBooleanValue();
                                break;
                            case DATE_WITHOUT_TIME:
                                oldValue = cav.getDateWithoutTimeValue();
                                break;
                            case DATE:
                                oldValue = cav.getDateValue();
                                break;
                            case ENTITY:
                                Object entityId = cav.getEntityValue().getObjectEntityId();
                                Class entityClass = cav.getCategoryAttribute().getJavaClassForEntity();
                                oldValue = entityId != null ? Id.of(entityId, entityClass) : null;
                                break;
                            default:
                                log.warn("Unsupported dynamic attribute type: " + cav.getCategoryAttribute().getDataType());
                                oldValue = null;
                        }
                        changes.add(new AttributeChanges.Change(DynamicAttributesUtils.encodeAttributeCode(cav.getCode()), oldValue));
                    } else {
                        AttributeChangeListener changeListener =
                                (AttributeChangeListener) ((ChangeTracker) cav)._persistence_getPropertyChangeListener();
                        if (changeListener != null && changeListener.getObjectChangeSet() != null) {
                            Object oldValue = null;
                            boolean changed = false;
                            for (ChangeRecord changeRecord : changeListener.getObjectChangeSet().getChanges()) {
                                switch (changeRecord.getAttribute()) {
                                    case "stringValue":
                                    case "intValue":
                                    case "doubleValue":
                                    case "booleanValue":
                                    case "dateWithoutTimeValue":
                                    case "dateValue":
                                        oldValue = changeRecord.getOldValue();
                                        changed = true;
                                        break;
                                    case "entityValue":
                                        Object entityId = ((ReferenceToEntity) changeRecord.getOldValue()).getObjectEntityId();
                                        Class entityClass = cav.getCategoryAttribute().getJavaClassForEntity();
                                        oldValue = entityId != null ? Id.of(entityId, entityClass) : null;
                                        changed = true;
                                        break;
                                }
                                if (changed) {
                                    changes.add(new AttributeChanges.Change(DynamicAttributesUtils.encodeAttributeCode(cav.getCode()), oldValue));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private AttributeChanges getEntityAttributeChanges(Entity entity, boolean deleted) {
        Set<AttributeChanges.Change> changes = new HashSet<>();
        Map<String, AttributeChanges> embeddedChanges = new HashMap<>();

        for (MetaProperty property : metadata.getClassNN(entity.getClass()).getProperties()) {
            Object value = entity.getValue(property.getName());
            if (deleted) {
                if (value instanceof EmbeddableEntity) {
                    EmbeddableEntity embedded = (EmbeddableEntity) value;
                    embeddedChanges.computeIfAbsent(property.getName(), s -> getEntityAttributeChanges(embedded, true));
                } else if (value instanceof Entity) {
                    changes.add(new AttributeChanges.Change(property.getName(), Id.of((Entity) value)));
                } else if (value instanceof Collection) {
                    Collection<Entity> coll = (Collection<Entity>) value;
                    Collection<Id> idColl = value instanceof List ? new ArrayList<>() : new LinkedHashSet<>();
                    for (Entity item : coll) {
                        idColl.add(Id.of(item));
                    }
                    changes.add(new AttributeChanges.Change(property.getName(), idColl));
                } else {
                    changes.add(new AttributeChanges.Change(property.getName(), value));
                }

            } else {
                if (value != null) {
                    changes.add(new AttributeChanges.Change(property.getName(), null));
                }
            }
        }

        if (deleted) {
            addDynamicAttributeChanges(entity, changes, true);
        }

        return new AttributeChanges(changes, embeddedChanges);
    }
}