/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesMetaClass;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.data.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Specific datasource for dynamic attributes.
 * It will be initialized only when main datasource will be valid.
 *
 * @author devyatkin
 * @version $Id$
 */
public class RuntimePropsDatasourceImpl
        extends AbstractDatasource<DynamicAttributesEntity>
        implements RuntimePropsDatasource<DynamicAttributesEntity> {

    protected DsContext dsContext;
    protected DataSupplier dataSupplier;
    protected DynamicAttributesMetaClass metaClass;
    protected View view;
    protected Datasource mainDs;
    protected MetaClass categorizedEntityClass;
    protected boolean initializedBefore = false;
    protected boolean categoryChanged = false;

    protected State state = State.NOT_INITIALIZED;

    protected DynamicAttributesEntity item;
    protected Category category;

    protected final Collection<CategoryAttribute> attributes;
    protected final View attributeValueView;

    public RuntimePropsDatasourceImpl(DsContext dsContext, DataSupplier dataSupplier, String id, String mainDsId, @Nullable MetaClass categorizedEntityClass) {
        this.categorizedEntityClass = categorizedEntityClass;
        this.id = id;
        this.dsContext = dsContext;
        this.dataSupplier = dataSupplier;
        this.metaClass = new DynamicAttributesMetaClass();
        this.setMainDs(mainDsId);
        this.setCommitMode(CommitMode.DATASTORE);

        attributes = AppBeans.get(DynamicAttributes.class).getAttributesForMetaClass(resolveCategorizedEntityClass());
        for (CategoryAttribute attribute : attributes) {
            MetaProperty metaProperty = DynamicAttributesUtils.getMetaPropertyPath(mainDs.getMetaClass(), attribute).getMetaProperty();
            this.metaClass.addProperty(metaProperty, attribute);
        }

        //noinspection unchecked
        mainDs.addItemPropertyChangeListener(e -> {
            if (e.getProperty().equals("category")) {
                categoryChanged = true;
                initMetaClass();
            }
        });
        mainDs.setLoadDynamicAttributes(true);

        ViewRepository viewRepository = AppBeans.get(ViewRepository.NAME);

        View baseAttributeValueView = viewRepository.getView(CategoryAttributeValue.class, View.LOCAL);
        View baseAttributeView = viewRepository.getView(CategoryAttribute.class, View.LOCAL);

        attributeValueView = new View(baseAttributeValueView, null, false)
                .addProperty("categoryAttribute", new View(baseAttributeView, null, false).addProperty("category"));
    }

    @Override
    public void setup(DsContext dsContext, DataSupplier dataSupplier, String id,
                      MetaClass metaClass, @Nullable View view) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    protected void initMetaClass() {
        Entity entity = mainDs.getItem();
        if (!(entity instanceof BaseGenericIdEntity)) {
            throw new IllegalStateException("This datasource can contain only entity with subclass of BaseGenericIdEntity");
        }
        BaseGenericIdEntity baseGenericIdEntity = (BaseGenericIdEntity) entity;
        if (PersistenceHelper.isNew(baseGenericIdEntity)) {
            baseGenericIdEntity.setDynamicAttributes(new HashMap<>());
        }
        @SuppressWarnings("unchecked")
        Map<String, CategoryAttributeValue> dynamicAttributes = baseGenericIdEntity.getDynamicAttributes();
        Preconditions.checkNotNullArgument(dynamicAttributes, "Dynamic attributes should be loaded explicitly");

        if (entity instanceof Categorized) {
            category = ((Categorized) entity).getCategory();
        }
        if (!initializedBefore && category == null) {
            category = getDefaultCategory();
            if (entity.getMetaClass().getProperty("category") != null) {
                entity.setValue("category", category);
            }
        }

        item = new DynamicAttributesEntity(metaClass);
        Collection<CategoryAttributeValue> entityValues = dynamicAttributes.values();
        TimeSource timeSource = AppBeans.get(TimeSource.NAME);
        for (CategoryAttribute attribute : attributes) {
            CategoryAttributeValue attributeValue = getValue(attribute, entityValues);
            Object value;
            if (attributeValue == null) {
                attributeValue = new CategoryAttributeValue();
                dynamicAttributes.put(attribute.getCode(), attributeValue);

                attributeValue.setCode(DynamicAttributesUtils.decodeAttributeCode(attribute.getCode()));
                attributeValue.setCategoryAttribute(attribute);
                attributeValue.setEntityId(entity.getUuid());

                if (PersistenceHelper.isNew(entity) || categoryChanged) {
                    attributeValue.setStringValue(StringUtils.trimToNull(attribute.getDefaultString()));
                    attributeValue.setIntValue(attribute.getDefaultInt());
                    attributeValue.setDoubleValue(attribute.getDefaultDouble());
                    attributeValue.setBooleanValue(attribute.getDefaultBoolean());
                    attributeValue.setDateValue(BooleanUtils.isTrue(attribute.getDefaultDateIsCurrent()) ?
                            timeSource.currentTimestamp() : attribute.getDefaultDate());
                    attributeValue.setEntityValue(attribute.getDefaultEntityId());
                    value = parseValue(attribute, attributeValue);
                } else {
                    value = null;
                }
            } else {
                value = parseValue(attribute, attributeValue);
            }

            item.addAttributeValue(attribute, attributeValue, value);
        }

        view = new View(DynamicAttributesEntity.class);
        Collection<MetaProperty> properties = metaClass.getProperties();
        for (MetaProperty property : properties) {
            view.addProperty(property.getName());
        }

        item.addPropertyChangeListener(listener);
        item.addPropertyChangeListener(e -> {
            modified = true;
            //noinspection unchecked
            itemsToUpdate.add(item.getCategoryValue(e.getProperty()));
        });

        this.valid();
        initializedBefore = true;
        if (!itemsToDelete.isEmpty()) {
            modified = true;
        }
        fireItemChanged(null);
    }

    @Override
    public MetaClass resolveCategorizedEntityClass() {
        if (categorizedEntityClass == null) {
            return mainDs.getMetaClass();
        } else {
            return categorizedEntityClass;
        }
    }

    protected CategoryAttributeValue getValue(CategoryAttribute attribute, Collection<CategoryAttributeValue> entityValues) {
        for (CategoryAttributeValue attrValue : entityValues) {
            if (attrValue.getCategoryAttribute().equals(attribute))
                return attrValue;
        }
        return null;
    }

    protected Object parseValue(CategoryAttribute attribute, CategoryAttributeValue attrValue) {
        PropertyType dataType = attribute.getDataType();

        if (BooleanUtils.isTrue(attribute.getIsEntity())) {
            if (attrValue != null) {
                UUID entityId = attrValue.getEntityValue();
                return parseEntity(attribute.getEntityClass(), entityId);
            } else {
                UUID entityId = attribute.getDefaultEntityId();
                return parseEntity(attribute.getEntityClass(), entityId);
            }
        } else {
            switch (dataType) {
                case STRING:
                case ENUMERATION:
                    return attrValue != null ? attrValue.getStringValue() : attribute.getDefaultString();
                case INTEGER:
                    return attrValue != null ? attrValue.getIntValue() : attribute.getDefaultInt();
                case DOUBLE:
                    return attrValue != null ? attrValue.getDoubleValue() : attribute.getDefaultDouble();
                case BOOLEAN:
                    return attrValue != null ? attrValue.getBooleanValue() : attribute.getDefaultBoolean();
                case DATE:
                    return attrValue != null ? attrValue.getDateValue() : attribute.getDefaultDate();
            }

        }
        return attrValue.getStringValue();
    }

    protected Entity parseEntity(String entityType, UUID uuid) {
        try {
            Class clazz = Class.forName(entityType);
            String entityClassName = metadata.getSession().getClassNN(clazz).getName();
            LoadContext entitiesContext = LoadContext.create(clazz)
                    .setQuery(LoadContext.createQuery("select a from " + entityClassName + " a where a.id =:e")
                            .setParameter("e", uuid))
                    .setView(View.LOCAL)
                    .setSoftDeletion(false);
            Entity entity = dataSupplier.load(entitiesContext);
            return entity;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("can't parse entity " + entityType + " " + uuid, e);
        }
    }

    @Override
    public DsContext getDsContext() {
        return dsContext;
    }

    @Override
    public DataSupplier getDataSupplier() {
        return dataSupplier;
    }

    @Override
    public void commit() {
        if (!allowCommit)
            return;

        if (Datasource.CommitMode.DATASTORE.equals(getCommitMode())) {
            Set<Entity> commitInstances = new HashSet<>();
            Set<Entity> deleteInstances = new HashSet<>();

            commitInstances.addAll(itemsToCreate);
            commitInstances.addAll(itemsToUpdate);
            deleteInstances.addAll(itemsToDelete);

            CommitContext cc = new CommitContext(commitInstances, deleteInstances);

            Set<Entity> entities = getDataSupplier().commit(cc);
            committed(entities);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public DynamicAttributesEntity getItem() {
        if (State.VALID.equals(state))
            return item;
        else
            throw new IllegalStateException("RuntimePropsDataSource state is " + state);
    }

    @Nullable
    @Override
    public DynamicAttributesEntity getItemIfValid() {
        return getState() == State.VALID ? getItem() : null;
    }

    @Override
    public void setItem(DynamicAttributesEntity item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void invalidate() {
        if (State.NOT_INITIALIZED != this.state) {
            final State prevStatus = this.state;
            this.state = State.INVALID;
            fireStateChanged(prevStatus);
        }
        modified = false;
        clearCommitLists();
    }

    @Override
    public void refresh() {
        initMetaClass();
    }

    @Override
    public MetaClass getMetaClass() {
        return metaClass;
    }

    @Override
    public View getView() {
        return null; // null is correct
    }

    @Override
    public View getAttributeValueView() {
        return attributeValueView;
    }

    @Override
    public void initialized() {
        final State prev = state;
        state = State.INVALID;
        fireStateChanged(prev);
    }

    @Override
    public void valid() {
        final State prev = state;
        state = State.VALID;
        fireStateChanged(prev);
    }

    @Override
    public void committed(Set<Entity> entities) {
        if (state != State.VALID) {
            return;
        }

        for (Entity entity : entities) {
            if (entity instanceof CategoryAttributeValue) {
                CategoryAttributeValue attributeValue = (CategoryAttributeValue) entity;
                item.updateAttributeValue(attributeValue);
            }
        }
        modified = false;
        clearCommitLists();
    }

    protected void setMainDs(String name) {
        mainDs = dsContext.get(name);
        if (mainDs == null) {
            throw new DevelopmentException("runtimePropsDatasource initialization error: mainDs '" + name + "' does not exists");
        }

        //noinspection unchecked
        mainDs.addStateChangeListener(e -> {
            if (state == State.VALID) {
                if (e.getPrevState() != State.VALID) {
                    initMetaClass();
                } else {
                    valid();
                }
            }
        });
    }

    @Override
    public Datasource getMainDs() {
        return mainDs;
    }

    @Override
    public Collection<MetaProperty> getPropertiesFilteredByCategory() {
        return metaClass.getPropertiesFilteredByCategory(category);
    }

    @Nullable
    public Category getDefaultCategory() {
        MetaClass metaClass = resolveCategorizedEntityClass();
        Collection<Category> categoriesForMetaClass = AppBeans.get(DynamicAttributes.class).getCategoriesForMetaClass(metaClass);
        for (Category category : categoriesForMetaClass) {
            if (Boolean.TRUE.equals(category.getIsDefault())) {
                return category;
            }
        }

        return null;
    }
}