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

package com.haulmont.cuba.core.app;

import com.google.common.collect.Sets;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.PersistenceSecurity;
import com.haulmont.cuba.core.app.events.SetupAttributeAccessEvent;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.SecurityTokenManager;
import com.haulmont.cuba.core.sys.persistence.CubaEntityFetchGroup;
import org.eclipse.persistence.queries.FetchGroup;
import org.eclipse.persistence.queries.FetchGroupTracker;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.*;

import static com.haulmont.cuba.core.entity.BaseEntityInternalAccess.*;
import static java.lang.String.format;

/**
 * Supports enforcing entity attribute permissions on Middleware.
 */
@Component(AttributeSecuritySupport.NAME)
public class AttributeSecuritySupport {

    public static final String NAME = "cuba_AttributeSecuritySupport";

    @Inject
    protected Metadata metadata;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected PersistenceSecurity security;

    @Inject
    protected ServerConfig config;

    @Inject
    protected SecurityTokenManager securityTokenManager;

    @Inject
    protected Events events;

    @Inject
    protected EntityStates entityStates;

    /**
     * Removes restricted attributes from a view.
     *
     * @param view source view
     * @return restricted view
     */
    public View createRestrictedView(View view) {
        if (!config.getEntityAttributePermissionChecking()) {
            return view;
        }
        Preconditions.checkNotNullArgument(view, "view is null");

        View restrictedView = new View(view.getEntityClass(),
                StringUtils.isEmpty(view.getName()) ? "" : view.getName() + "_restricted",
                false); // do not include system properties in constructor because they will be copied later if exist
        copyViewConsideringPermissions(view, restrictedView);
        return restrictedView;
    }

    private void copyViewConsideringPermissions(View srcView, View dstView) {
        MetaClass metaClass = metadata.getClassNN(srcView.getEntityClass());
        for (ViewProperty property : srcView.getProperties()) {
            if (security.isEntityAttrReadPermitted(metaClass, property.getName())) {
                View viewCopy = null;
                if (property.getView() != null) {
                    viewCopy = new View(property.getView().getEntityClass(), property.getView().getName() + "(restricted)", false);
                    copyViewConsideringPermissions(property.getView(), viewCopy);
                }
                dstView.addProperty(property.getName(), viewCopy, property.getFetchMode());
            }
        }
    }

    /**
     * Should be called after loading an entity from the database.
     *
     * @param entity just loaded detached entity
     */
    public void afterLoad(Entity entity) {
        if (!config.getEntityAttributePermissionChecking()) {
            return;
        }
        if (entity != null) {
            metadataTools.traverseAttributes(entity, new FillingInaccessibleAttributesVisitor());
        }
    }

    /**
     * Should be called after loading a list of entities from the database.
     *
     * @param entities list of just loaded detached entities
     */
    public void afterLoad(Collection<? extends Entity> entities) {
        Preconditions.checkNotNullArgument(entities, "entities list is null");

        for (Entity entity : entities) {
            afterLoad(entity);
        }
    }

    /**
     * Should be called before persisting a new entity.
     *
     * @param entity new entity
     */
    public void beforePersist(Entity entity) {
        if (!config.getEntityAttributePermissionChecking()) {
            return;
        }
        // check only immediate attributes, otherwise persisted entity can be unusable for calling code
        MetaClass metaClass = metadata.getClassNN(entity.getClass());
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            if (!metadataTools.isSystem(metaProperty)
                    && !metaProperty.isReadOnly()
                    && !security.isEntityAttrUpdatePermitted(metaClass, metaProperty.getName())) {
                entity.setValue(metaProperty.getName(), null);
            }
        }
    }

    /**
     * Should be called after persisting a new entity.
     *
     * @param entity new entity
     * @param view entity view
     */
    public void afterPersist(Entity entity, View view) {
        if (entity instanceof BaseGenericIdEntity) {
            BaseGenericIdEntity genericIdEntity = (BaseGenericIdEntity) entity;
            setupAttributeAccess(genericIdEntity);
            if (view != null) {
                metadataTools.traverseAttributesByView(view, genericIdEntity, new AttributeAccessVisitor(Sets.newHashSet(entity)));
            }
        }
    }

    /**
     * Should be called before merging an entity.
     *
     * @param entity detached entity
     */
    public void beforeMerge(Entity entity) {
        if (!config.getEntityAttributePermissionChecking()) {
            return;
        }
        checkRequiredAttributes(entity);
        applySecurityToFetchGroup(entity);
        //apply fetch group constraints to embedded
        for (MetaProperty metaProperty : entity.getMetaClass().getProperties()) {
            String name = metaProperty.getName();
            if (metadataTools.isEmbedded(metaProperty) && entityStates.isLoaded(entity, name)) {
                Entity embedded = entity.getValue(name);
                checkRequiredAttributes(embedded);
                applySecurityToFetchGroup(embedded);
            }
        }
    }

    /**
     * Should be called after merging an entity and before transaction commit.
     *
     * @param entity detached entity
     */
    public void afterMerge(Entity entity) {
        if (entity instanceof BaseGenericIdEntity) {
            BaseGenericIdEntity genericIdEntity = (BaseGenericIdEntity) entity;
            setupAttributeAccess(genericIdEntity);
            metadataTools.traverseAttributes(genericIdEntity, new AttributeAccessVisitor(Sets.newHashSet(entity)));
        }
    }

    /**
     * Should be called after merging an entity and transaction commit.
     *
     * @param entity detached entity
     */
    public void afterCommit(Entity entity) {
        if (!config.getEntityAttributePermissionChecking()) {
            return;
        }
        if (entity != null) {
            metadataTools.traverseAttributes(entity, new ClearInaccessibleAttributesVisitor());
        }
    }

    public void onLoad(Collection<? extends Entity> entities, View view) {
        Preconditions.checkNotNullArgument(entities, "entities list is null");

        for (Entity entity : entities) {
            onLoad(entity, view);
        }
    }

    public void onLoad(Entity entity, View view) {
        if (entity instanceof BaseGenericIdEntity) {
            BaseGenericIdEntity genericIdEntity = (BaseGenericIdEntity) entity;
            setupAttributeAccess(genericIdEntity);
            metadataTools.traverseLoadedAttributesByView(view, genericIdEntity, new AttributeAccessVisitor(Sets.newHashSet(entity)));
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> void setupAttributeAccess(T entity) {
        if (entity instanceof BaseGenericIdEntity || entity instanceof EmbeddableEntity) {
            SetupAttributeAccessEvent<T> event = new SetupAttributeAccessEvent<>(entity);
            boolean handled = false;
            Map<String, SetupAttributeAccessHandler> handlers = AppBeans.getAll(SetupAttributeAccessHandler.class);
            if (handlers != null) {
                for (SetupAttributeAccessHandler handler : handlers.values()) {
                    MetaClass metaClass = metadata.getExtendedEntities().getOriginalOrThisMetaClass(entity.getMetaClass());
                    if (handler.supports(metaClass.getJavaClass())) {
                        handled = true;
                        handler.setupAccess(event);
                    }
                }
            }
            if (event.getReadonlyAttributes() != null) {
                Set<String> attributes = event.getReadonlyAttributes();
                SecurityState state = getOrCreateSecurityState(entity);
                addReadonlyAttributes(state, attributes.toArray(new String[attributes.size()]));
            }
            if (event.getRequiredAttributes() != null) {
                Set<String> attributes = event.getRequiredAttributes();
                SecurityState state = getOrCreateSecurityState(entity);
                addRequiredAttributes(state, attributes.toArray(new String[attributes.size()]));
            }
            if (event.getHiddenAttributes() != null) {
                Set<String> attributes = event.getHiddenAttributes();
                SecurityState state = getOrCreateSecurityState(entity);
                addHiddenAttributes(state, attributes.toArray(new String[attributes.size()]));
            }
            if (handled) {
                securityTokenManager.writeSecurityToken(entity);
            }
        }
    }

    /**
     * Checks if attribute access enabled for the current entity type.
     * It's based on the existence of SetupAttributeAccessHandler for the metaClass.
     *
     * @param metaClass - entity metaClass
     */
    @SuppressWarnings("unchecked")
    public boolean isAttributeAccessEnabled(MetaClass metaClass) {
        Map<String, SetupAttributeAccessHandler> handlers = AppBeans.getAll(SetupAttributeAccessHandler.class);
        if (handlers != null) {
            metaClass = metadata.getExtendedEntities().getOriginalOrThisMetaClass(metaClass);
            for (SetupAttributeAccessHandler handler : handlers.values()) {
                if (handler.supports(metaClass.getJavaClass())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void checkRequiredAttributes(Entity entity) {
        SecurityState securityState = getSecurityState(entity);
        if (securityState != null && !securityState.getRequiredAttributes().isEmpty()) {
            for (MetaProperty metaProperty : entity.getMetaClass().getProperties()) {
                String propertyName = metaProperty.getName();
                if (BaseEntityInternalAccess.isRequired(securityState, propertyName) && entity.getValue(propertyName) == null) {
                    throw new RowLevelSecurityException(format("Attribute [%s] is required for entity %s", propertyName, entity),
                            entity.getMetaClass().getName());
                }
            }
        }
    }

    protected void applySecurityToFetchGroup(Entity entity) {
        if (entity == null) {
            return;
        }
        MetaClass metaClass = metadata.getClassNN(entity.getClass());
        FetchGroupTracker fetchGroupTracker = (FetchGroupTracker) entity;
        FetchGroup fetchGroup = fetchGroupTracker._persistence_getFetchGroup();
        SecurityState securityState = getSecurityState(entity);
        if (fetchGroup != null) {
            List<String> attributesToRemove = new ArrayList<>();
            for (String attrName : fetchGroup.getAttributeNames()) {
                String[] parts = attrName.split("\\.");
                if (parts.length > 0 && BaseEntityInternalAccess.isHiddenOrReadOnly(securityState, parts[0])) {
                    attributesToRemove.add(attrName);
                } else {
                    MetaClass currentMetaClass = metaClass;
                    for (String part : parts) {
                        if (!security.isEntityAttrUpdatePermitted(currentMetaClass, part)) {
                            attributesToRemove.add(attrName);
                            break;
                        }
                        MetaProperty metaProperty = currentMetaClass.getPropertyNN(part);
                        if (metaProperty.getRange().isClass()) {
                            currentMetaClass = metaProperty.getRange().asClass();
                        }
                    }
                }
            }
            if (!attributesToRemove.isEmpty()) {
                List<String> attributeNames = new ArrayList<>(fetchGroup.getAttributeNames());
                attributeNames.removeAll(attributesToRemove);
                fetchGroupTracker._persistence_setFetchGroup(new CubaEntityFetchGroup(attributeNames));
            }
        } else {
            List<String> attributeNames = new ArrayList<>();
            for (MetaProperty metaProperty : metaClass.getProperties()) {
                String propertyName = metaProperty.getName();
                if (metadataTools.isSystem(metaProperty)) {
                    attributeNames.add(propertyName);
                }
                if (security.isEntityAttrUpdatePermitted(metaClass, propertyName) &&
                        !BaseEntityInternalAccess.isHiddenOrReadOnly(securityState, propertyName)) {
                    attributeNames.add(metaProperty.getName());
                }
            }
            fetchGroupTracker._persistence_setFetchGroup(new CubaEntityFetchGroup(attributeNames));
        }
    }

    private void addInaccessibleAttribute(Entity entity, String property) {
        SecurityState securityState = getOrCreateSecurityState(entity);
        String[] attributes = getInaccessibleAttributes(securityState);
        attributes = attributes == null ? new String[1] : Arrays.copyOf(attributes, attributes.length + 1);
        attributes[attributes.length - 1] = property;
        setInaccessibleAttributes(securityState, attributes);
    }

    protected void setNullPropertyValue(Entity entity, MetaProperty property) {
        // Using reflective access to field because the attribute can be unfetched if loading not partial entities,
        // which is the case when in-memory constraints exist
        Range range = property.getRange();
        if (range.isClass()) {
            Object nullValue = null;
            if (range.getCardinality().isMany()) {
                Class<?> propertyType = property.getJavaType();
                if (List.class.isAssignableFrom(propertyType)) {
                    nullValue = new ArrayList<>();
                } else if (Set.class.isAssignableFrom(propertyType)) {
                    nullValue = new LinkedHashSet<>();
                }
            }
            BaseEntityInternalAccess.setValue(entity, property.getName(), nullValue);
            BaseEntityInternalAccess.setValueForHolder(entity, property.getName(), nullValue);
        } else {
            BaseEntityInternalAccess.setValue(entity, property.getName(), null);
        }
    }

    protected class FillingInaccessibleAttributesVisitor implements EntityAttributeVisitor {
        @Override
        public boolean skip(MetaProperty property) {
            return metadataTools.isNotPersistent(property);
        }

        @Override
        public void visit(Entity entity, MetaProperty property) {
            MetaClass metaClass = metadata.getClassNN(entity.getClass());
            if (!security.isEntityAttrReadPermitted(metaClass, property.getName())) {
                addInaccessibleAttribute(entity, property.getName());
                if (!metadataTools.isSystem(property) && !property.isReadOnly()) {
                    setNullPropertyValue(entity, property);
                }
            }
            SecurityState securityState = BaseEntityInternalAccess.getSecurityState(entity);
            if (securityState != null && securityState.getHiddenAttributes().contains(property.getName())) {
                addInaccessibleAttribute(entity, property.getName());
                if (!metadataTools.isSystem(property)) {
                    setNullPropertyValue(entity, property);
                }
            }
        }
    }

    protected class ClearInaccessibleAttributesVisitor implements EntityAttributeVisitor {
        @Override
        public void visit(Entity entity, MetaProperty property) {
            MetaClass metaClass = metadata.getClassNN(entity.getClass());
            String propertyName = property.getName();
            if (!security.isEntityAttrReadPermitted(metaClass, propertyName)) {
                addInaccessibleAttribute(entity, propertyName);
                if (!metadataTools.isSystem(property) && !property.isReadOnly()) {
                    setNullPropertyValue(entity, property);
                }
            }
            SecurityState securityState = BaseEntityInternalAccess.getSecurityState(entity);
            if (securityState != null && securityState.getHiddenAttributes().contains(property.getName())) {
                addInaccessibleAttribute(entity, property.getName());
                if (!metadataTools.isSystem(property)) {
                    setNullPropertyValue(entity, property);
                }
            }
        }
    }

    protected class AttributeAccessVisitor implements EntityAttributeVisitor {
        protected Set<Entity> visited;

        public AttributeAccessVisitor(Set<Entity> visited) {
            this.visited = visited;
        }

        @Override
        public void visit(Entity entity, MetaProperty property) {
            if (!visited.contains(entity)) {
                visited.add(entity);
                setupAttributeAccess(entity);
            }
        }
    }
}
