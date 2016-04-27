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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.PersistenceSecurity;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.persistence.CubaEntityFetchGroup;
import org.eclipse.persistence.queries.FetchGroup;
import org.eclipse.persistence.queries.FetchGroupTracker;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

    /**
     * Removes restricted attributes from a view.
     *
     * @param view  source view
     * @return      restricted view
     */
    public View createRestrictedView(View view) {
        if (!config.getEntityAttributePermissionChecking()) {
            return view;
        }
        Preconditions.checkNotNullArgument(view, "view is null");

        View restrictedView = new View(view.getEntityClass(), StringUtils.isEmpty(view.getName()) ? "" : view.getName() + "_restricted");
        copyViewConsideringPermissions(view, restrictedView);
        return restrictedView;
    }

    private void copyViewConsideringPermissions(View srcView, View dstView) {
        MetaClass metaClass = metadata.getClassNN(srcView.getEntityClass());
        for (ViewProperty property : srcView.getProperties()) {
            if (security.isEntityAttrReadPermitted(metaClass, property.getName())) {
                View viewCopy = null;
                if (property.getView() != null) {
                    viewCopy = new View(property.getView().getEntityClass(), property.getView().getName() + "(restricted)");
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
        if (!config.getEntityAttributePermissionChecking()) {
            return;
        }
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
     * Should be called before merging an entity.
     *
     * @param entity detached entity
     */
    public void beforeMerge(Entity entity) {
        if (!config.getEntityAttributePermissionChecking()) {
            return;
        }
        MetaClass metaClass = metadata.getClassNN(entity.getClass());
        FetchGroup fetchGroup = ((FetchGroupTracker) entity)._persistence_getFetchGroup();
        if (fetchGroup != null) {
            List<String> attributesToRemove = new ArrayList<>();
            for (String attrName : fetchGroup.getAttributeNames()) {
                String[] parts = attrName.split("\\.");
                MetaClass tmpMetaClass = metaClass;
                for (String part : parts) {
                    if (!security.isEntityAttrUpdatePermitted(tmpMetaClass, part)) {
                        attributesToRemove.add(attrName);
                        break;
                    }
                    MetaProperty metaProperty = tmpMetaClass.getPropertyNN(part);
                    if (metaProperty.getRange().isClass()) {
                        tmpMetaClass = metaProperty.getRange().asClass();
                    }
                }
            }
            if (!attributesToRemove.isEmpty()) {
                List<String> attributeNames = new ArrayList<>(fetchGroup.getAttributeNames());
                attributeNames.removeAll(attributesToRemove);
                ((FetchGroupTracker) entity)._persistence_setFetchGroup(new CubaEntityFetchGroup(attributeNames));
            }
        } else {
            List<String> attributeNames = new ArrayList<>();
            for (MetaProperty metaProperty : metaClass.getProperties()) {
                if (security.isEntityAttrUpdatePermitted(metaClass, metaProperty.getName())) {
                    attributeNames.add(metaProperty.getName());
                }
            }
            ((FetchGroupTracker) entity)._persistence_setFetchGroup(new CubaEntityFetchGroup(attributeNames));
        }
    }

    /**
     * Should be called after merging an entity and transaction commit.
     *
     * @param entity detached entity
     */
    public void afterMerge(Entity entity, View view) {
        if (!config.getEntityAttributePermissionChecking()) {
            return;
        }
        if (entity != null) {
            metadataTools.traverseAttributesByView(view, entity, new ClearInaccessibleAttributesVisitor());
        }
    }

    private void addInaccessibleAttribute(BaseGenericIdEntity entity, String property) {
        String[] attributes = BaseEntityInternalAccess.getInaccessibleAttributes(entity);
        attributes = attributes == null ? new String[1] : Arrays.copyOf(attributes, attributes.length + 1);
        attributes[attributes.length - 1] = property;
        BaseEntityInternalAccess.setInaccessibleAttributes(entity, attributes);
    }

    private class FillingInaccessibleAttributesVisitor implements EntityAttributeVisitor {

        @Override
        public boolean skip(MetaProperty property) {
            return metadataTools.isTransient(property);
        }

        @Override
        public void visit(Entity entity, MetaProperty property) {
            MetaClass metaClass = metadata.getClassNN(entity.getClass());
            if (!security.isEntityAttrReadPermitted(metaClass, property.getName())) {
                addInaccessibleAttribute((BaseGenericIdEntity) entity, property.getName());
                if (!metadataTools.isSystem(property) && !property.isReadOnly()) {
                    entity.setValue(property.getName(), null);
                }
            }
        }
    }

    private class ClearInaccessibleAttributesVisitor implements EntityAttributeVisitor {
        @Override
        public void visit(Entity entity, MetaProperty property) {
            MetaClass metaClass = metadata.getClassNN(entity.getClass());
            if (!security.isEntityAttrReadPermitted(metaClass, property.getName())) {
                addInaccessibleAttribute((BaseGenericIdEntity) entity, property.getName());
                if (!metadataTools.isSystem(property) && !property.isReadOnly()) {
                    entity.setValue(property.getName(), null);
                }
            }
        }
    }
}
