/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * <p>Class describes how entity fields should be saved during the import performed by
 * {@link EntityImportExportService EntityImportExportService}</p>
 *
 * <p>Only fields that are added as properties to the {@code EntityImportView} will be saved.</p>
 *
 * <p>For simple entity property the rule is simple: if property name was added to the view, then the property will be saved.</p>
 *
 * <p>For references to other entities there are three options:</p>
 * <ul>
 *     <li>Create or update the referenced entity/entities. This is useful when you are importing the master entities
 *     with a collection of its detail entities, like Order and its OrderItems</li>
 *     <li>Try to find the referenced entity in the database and continue the import process if it is not found</li>
 *     <li>Try to find the referenced entity in the database and stop the import process if it is not found</li>
 * </ul>
 *
 * <p>Use the {@link #addProperty(String, EntityImportView)} method when you want to save the referenced entity. It will
 * be saved according to the {@code EntityImportView} passed as parameter.</p>
 *
 * <p>The property should be added with the {@link #addProperty(String, ReferenceImportBehaviour)} method if you want
 * to find the reference in the database. {@link ReferenceImportBehaviour} enum describes the desired behavior:
 * ignore missing reference or throw an exception if it was not found in the database.</p>
 *
 * <p>You can invoke {@code addProperty} methods in fluent interface style. There are also useful methods like
 * {@link #addLocalProperties()}, {@link #addSystemProperties()} or {@link #addProperties(String...)}</p>
 *
 * <p>Example of creating the EntityImportView object:</p>
 * <pre>
 * EntityImportView importView = new EntityImportView(Group.class)
 *           .addLocalProperties()
 *           .addProperty("constraints", new EntityImportView(Constraint.class).addLocalProperties())
 *           .addProperty("parent", ReferenceImportBehaviour.IGNORE_MISSING);
 * </pre>
 *
 * @author gorbunkov
 * @version $Id$
 */
public class EntityImportView implements Serializable {

    private Map<String, EntityImportViewProperty> properties = new HashMap<>();

    private Class<? extends Entity> entityClass;

    public EntityImportView(Class<? extends Entity> entityClass) {
        this.entityClass = entityClass;
    }

    public EntityImportView addProperty(String name, EntityImportView view) {
        EntityImportViewProperty property = new EntityImportViewProperty(name, view);
        properties.put(name, property);
        return this;
    }

    public EntityImportView addProperty(String name, ReferenceImportBehaviour referenceImportBehaviour) {
        EntityImportViewProperty property = new EntityImportViewProperty(name, referenceImportBehaviour);
        properties.put(name, property);
        return this;
    }

    public EntityImportView addProperty(String name) {
        EntityImportViewProperty property = new EntityImportViewProperty(name);
        properties.put(name, property);
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
                .filter(property -> !property.getRange().isClass() && !metadataTools.isSystem(property))
                .forEach(metaProperty -> addProperty(metaProperty.getName()));
        return this;
    }

    public EntityImportView addSystemProperties() {
        Metadata metadata = AppBeans.get(Metadata.class);
        MetaClass metaClass = metadata.getClassNN(entityClass);
        MetadataTools metadataTools = metadata.getTools();
        metaClass.getProperties().stream()
                .filter(metadataTools::isSystem)
                .forEach(metaProperty -> addProperty(metaProperty.getName()));
        return this;
    }

    public EntityImportView addProperties(String... names) {
        for (String name : names) {
            addProperty(name);
        }
        return this;
    }

    public EntityImportView removeProperty(String name) {
        properties.remove(name);
        return this;
    }
}
