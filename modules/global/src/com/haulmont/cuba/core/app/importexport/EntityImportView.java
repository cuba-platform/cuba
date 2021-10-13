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

package com.haulmont.cuba.core.app.importexport;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class describing how entity fields should be saved during the import performed by {@link EntityImportExportService
 * EntityImportExportService} <p> Only fields that are added as properties to the {@code EntityImportView} will be
 * saved.</p> <p> For local entity property the rule is simple: if property name is added to the view, then the property
 * will be saved. Use {@link #addLocalProperty(String)} method for adding local property to the view. <p> For
 * <b>many-to-one</b> references there are two possible options: <ul> <li>Create or update the referenced entity. Use
 * the {@link #addManyToOneProperty(String, EntityImportView)} method. The referenced entity will be saved according to
 * the {@code EntityImportView} passed as parameter</li> <li>Try to find the reference in the database and put it to the
 * property value. {@link #addManyToOneProperty(String, ReferenceImportBehaviour)} must be used for this. {@link
 * ReferenceImportBehaviour} parameter specifies the behaviour in case when referenced entity is missed in the database:
 * missing entity can be ignored or import may fail with an error.</li> </ul>
 * <p>
 * For <b>one-to-one</b> references behavior is the same as for the many-to-one references. Just use the corresponding
 * methods for adding properties to the view: {@link #addOneToOneProperty(String, EntityImportView)} or {@link
 * #addOneToOneProperty(String, ReferenceImportBehaviour)}.
 * <p>
 * For <b>one-to-many</b> references you must specify the {@link EntityImportView} which defines how entities from the
 * collection must be saved. The second parameter is the {@link CollectionImportPolicy} which specifies what to do with
 * collection items that weren't passed to the import: they can be removed or remained.
 * <p>
 * For <b>many-to-many</b> references the following things must be defined: <ul> <li>Whether the passed collection
 * members must be created/updated or just searched in the database</li> <li>Whether the collection items not passed to
 * the import must be removed or remain. Keep in mind that for many-to-many properties missing collection members will be
 * removed from the collection only, not from the database</li> </ul>
 * <p>
 * You can invoke methods for adding view properties in fluent interface style. There are also useful methods like
 * {@link #addLocalProperties()}, {@link #addSystemProperties()} or {@link #addProperties(String...)}
 * <p>
 * Example of creating the EntityImportView object:
 * <pre>
 * EntityImportView importView = new EntityImportView(Group.class)
 *           .addLocalProperties()
 *           .addOneToManyProperty("constraints",
 *                  new EntityImportView(Constraint.class).addLocalProperties(),
 *                  CollectionImportPolicy.KEEP_ABSENT_ITEMS)
 *           .addManyToOneProperty("parent", ReferenceImportBehaviour.ERROR_ON_MISSING);
 * </pre>
 */
public class EntityImportView implements Serializable {

    private Map<String, EntityImportViewProperty> properties = new HashMap<>();

    private Class<? extends Entity> entityClass;

    public EntityImportView(Class<? extends Entity> entityClass) {
        this.entityClass = entityClass;
    }

    public EntityImportView addLocalProperty(String name) {
        EntityImportViewProperty property = new EntityImportViewProperty(name);
        properties.put(name, property);
        return this;
    }

    public EntityImportView addManyToOneProperty(String name, EntityImportView view) {
        EntityImportViewProperty property = new EntityImportViewProperty(name, view);
        properties.put(name, property);
        return this;
    }

    public EntityImportView addManyToOneProperty(String name, ReferenceImportBehaviour referenceImportBehaviour) {
        EntityImportViewProperty property = new EntityImportViewProperty(name, referenceImportBehaviour);
        properties.put(name, property);
        return this;
    }

    public EntityImportView addOneToOneProperty(String name, EntityImportView view) {
        EntityImportViewProperty property = new EntityImportViewProperty(name, view);
        properties.put(name, property);
        return this;
    }

    public EntityImportView addOneToOneProperty(String name, ReferenceImportBehaviour referenceImportBehaviour) {
        EntityImportViewProperty property = new EntityImportViewProperty(name, referenceImportBehaviour);
        properties.put(name, property);
        return this;
    }

    public EntityImportView addOneToManyProperty(String name, EntityImportView view, CollectionImportPolicy collectionImportPolicy) {
        EntityImportViewProperty property = new EntityImportViewProperty(name, view, collectionImportPolicy);
        properties.put(name, property);
        return this;
    }

    public EntityImportView addManyToManyProperty(String name, EntityImportView view, CollectionImportPolicy collectionImportPolicy) {
        EntityImportViewProperty property = new EntityImportViewProperty(name, view, collectionImportPolicy);
        properties.put(name, property);
        return this;
    }

    public EntityImportView addManyToManyProperty(String name, ReferenceImportBehaviour referenceImportBehaviour, CollectionImportPolicy collectionImportPolicy) {
        EntityImportViewProperty property = new EntityImportViewProperty(name, referenceImportBehaviour, collectionImportPolicy);
        properties.put(name, property);
        return this;
    }

    public EntityImportView addEmbeddedProperty(String name, EntityImportView view) {
        EntityImportViewProperty property = new EntityImportViewProperty(name, view);
        properties.put(name, property);
        return this;
    }

    public EntityImportView addProperty(EntityImportViewProperty property) {
        properties.put(property.getName(), property);
        return this;
    }

    public EntityImportViewProperty getProperty(String name) {
        return properties.get(name);
    }

    public Collection<EntityImportViewProperty> getProperties() {
        return properties.values();
    }

    public Class<? extends Entity> getEntityClass() {
        return entityClass;
    }

    public EntityImportView addLocalProperties() {
        Metadata metadata = AppBeans.get(Metadata.class);
        MetaClass metaClass = metadata.getClassNN(entityClass);
        MetadataTools metadataTools = metadata.getTools();
        metaClass.getProperties().stream()
                .filter(property -> !property.getRange().isClass() &&
                        !metadataTools.isSystem(property) &&
                        metadataTools.isPersistent(property))
                .forEach(metaProperty -> addLocalProperty(metaProperty.getName()));
        return this;
    }

    public EntityImportView addSystemProperties() {
        Metadata metadata = AppBeans.get(Metadata.class);
        MetaClass metaClass = metadata.getClassNN(entityClass);
        MetadataTools metadataTools = metadata.getTools();
        metaClass.getProperties().stream()
                .filter(metadataTools::isSystem)
                .forEach(metaProperty -> addLocalProperty(metaProperty.getName()));
        return this;
    }

    public EntityImportView addProperties(String... names) {
        for (String name : names) {
            addLocalProperty(name);
        }
        return this;
    }

    public EntityImportView removeProperty(String name) {
        properties.remove(name);
        return this;
    }
}
