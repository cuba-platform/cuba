/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.*;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.SetValueEntity;
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
    protected boolean initializedBefore = false;
    protected boolean categoryChanged = false;

    protected State state = State.NOT_INITIALIZED;

    protected DynamicAttributesEntity item;
    protected Category category;

    protected final Collection<CategoryAttribute> attributes;
    protected final View attributeValueView;

    public RuntimePropsDatasourceImpl(DsContext dsContext, DataSupplier dataSupplier, String id, String mainDsId) {
        this.id = id;
        this.dsContext = dsContext;
        this.dataSupplier = dataSupplier;
        this.metaClass = new DynamicAttributesMetaClass();
        this.setMainDs(mainDsId);
        this.setCommitMode(CommitMode.DATASTORE);

        attributes = AppBeans.get(DynamicAttributes.class).getAttributesForMetaClass(mainDs.getMetaClass());
        for (CategoryAttribute attribute : attributes) {
            MetaProperty metaProperty = DynamicAttributesUtils.getMetaPropertyPath(mainDs.getMetaClass(), attribute).getMetaProperty();
            this.metaClass.addProperty(metaProperty, attribute);
        }

        mainDs.addListener(new DsListenerAdapter() {
            @Override
            public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                if (property.equals("category")) {
                    categoryChanged = true;
                    initMetaClass();
                }
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
            baseGenericIdEntity.setDynamicAttributes(new HashMap<String, CategoryAttributeValue>());
        }
        Map<String, CategoryAttributeValue> dynamicAttributes = baseGenericIdEntity.getDynamicAttributes();
        Preconditions.checkNotNullArgument(dynamicAttributes, "Dynamic attributes should be loaded explicitly");

        if (entity instanceof Categorized) {
            category = ((Categorized) entity).getCategory();
        }
        if (!initializedBefore && category == null) {
            category = getDefaultCategory(entity);
            if (mainDs.getMetaClass().getProperty("category") != null) {
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

            if (DynamicAttributesUtils.getAttributeClass(attribute).equals(SetValueEntity.class)) {
                createOptionsDatasource(attribute, (SetValueEntity) value);
            }
        }

        view = new View(DynamicAttributesEntity.class);
        Collection<MetaProperty> properties = metaClass.getProperties();
        for (MetaProperty property : properties) {
            view.addProperty(property.getName());
        }

        item.addListener(listener);
        item.addListener(new com.haulmont.chile.core.common.ValueListener() {
            @Override
            public void propertyChanged(Object item, String property, Object prevValue, Object value) {
                modified = true;
                //noinspection unchecked
                itemToUpdate.add(((DynamicAttributesEntity) item).getCategoryValue(property));
            }
        });
        this.valid();
        initializedBefore = true;
        if (!itemToDelete.isEmpty()) {
            modified = true;
        }
        fireItemChanged(null);
    }

    protected void createOptionsDatasource(CategoryAttribute attribute, final SetValueEntity attributeValue) {
        final String property = attribute.getName();
        final MetaClass metaClass = this.getMetaClass();
        final MetaProperty metaProperty = metaClass.getProperty(property);
        if (metaProperty == null) {
            throw new DevelopmentException(
                    String.format("Can't find property '%s' in datasource '%s'", property, this.getId()));
        }
        DsBuilder builder = new DsBuilder(getDsContext());
        builder.reset().setMetaClass(metadata.getSession().getClass(SetValueEntity.class)).setId(id)
                .setViewName(View.MINIMAL).setSoftDeletion(false);

        CollectionDatasource datasource = builder
                .setRefreshMode(CollectionDatasource.RefreshMode.NEVER)
                .buildCollectionDatasource();
        List<SetValueEntity> options = getOptions(attribute, attributeValue);
        for (SetValueEntity option : options) {
            datasource.includeItem(option);
        }

        ((DatasourceImpl) datasource).valid();
        //datasource.setItem(attributeValue);
    }

    protected List<SetValueEntity> getOptions(CategoryAttribute attribute, SetValueEntity attributeValue) {
        String enumeration = attribute.getEnumeration();
        String[] values = StringUtils.split(enumeration, ',');
        List<SetValueEntity> options = new LinkedList<>();
        for (String value : values) {
            String trimmedValue = StringUtils.trimToNull(value);
            if (trimmedValue != null) {
                if (attributeValue != null && trimmedValue.equals(attributeValue.getValue()))
                    options.add(attributeValue);
                else
                    options.add(new SetValueEntity(trimmedValue));
            }
        }
        return options;
    }

    protected CategoryAttributeValue getValue(CategoryAttribute attribute, Collection<CategoryAttributeValue> entityValues) {
        for (CategoryAttributeValue attrValue : entityValues) {
            if (attrValue.getCategoryAttribute().equals(attribute))
                return attrValue;
        }
        return null;
    }

    protected Object parseValue(CategoryAttribute attribute, CategoryAttributeValue attrValue) {
        String dataType = attribute.getDataType();

        if (BooleanUtils.isTrue(attribute.getIsEntity())) {
            if (attrValue != null) {
                UUID entityId = attrValue.getEntityValue();
                return parseEntity(dataType, entityId);
            } else {
                UUID entityId = attribute.getDefaultEntityId();
                return parseEntity(dataType, entityId);
            }
        } else {

            switch (PropertyType.valueOf(dataType)) {
                case STRING:
                    if (attrValue != null)
                        return attrValue.getStringValue();
                    else return attribute.getDefaultString();
                case INTEGER:
                    if (attrValue != null)
                        return attrValue.getIntValue();
                    else return attribute.getDefaultInt();
                case DOUBLE:
                    if (attrValue != null)
                        return attrValue.getDoubleValue();
                    else
                        return attribute.getDefaultDouble();
                case BOOLEAN:
                    if (attrValue != null)
                        return attrValue.getBooleanValue();
                    else
                        return attribute.getDefaultBoolean();
                case DATE:
                    if (attrValue != null)
                        return attrValue.getDateValue();
                    else
                        return attribute.getDefaultDate();
                case ENUMERATION:

                    if (attrValue != null) {
                        String stringValue = attrValue.getStringValue();
                        if (stringValue != null && !StringUtils.isBlank(stringValue))
                            return new SetValueEntity(stringValue);
                        else
                            return null;
                    } else {
                        String eValue = attribute.getDefaultString();
                        if (eValue != null)
                            return new SetValueEntity(eValue);
                        else
                            return null;
                    }
            }

        }
        return attrValue.getStringValue();
    }

    protected Entity parseEntity(String entityType, UUID uuid) {
        Entity entity;
        try {
            Class clazz = Class.forName(entityType);
            LoadContext entitiesContext = new LoadContext(clazz);
            String entityClassName = metadata.getSession().getClassNN(clazz).getName();
            LoadContext.Query query = entitiesContext.setQueryString("select a from " + entityClassName + " a where a.id =:e");
            query.setParameter("e", uuid);
            entitiesContext.setView("_local");
            entity = dataSupplier.load(entitiesContext);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("can't parse entity " + entityType + " " + uuid, e);
        }
        return entity;
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

            commitInstances.addAll(itemToCreate);
            commitInstances.addAll(itemToUpdate);
            deleteInstances.addAll(itemToDelete);

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
        if (!State.VALID.equals(state)) {
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
        mainDs.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void stateChanged(Datasource ds, State prevState, State state) {
                        if (State.VALID.equals(state)) {
                            if (!State.VALID.equals(prevState))
                                initMetaClass();
                            else
                                valid();
                        }
                    }
                }
        );
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
    public Category getDefaultCategory(Entity entity) {
        MetaClass metaClass = metadata.getSession().getClassNN(entity.getClass());
        Collection<Category> categoriesForMetaClass = AppBeans.get(DynamicAttributes.class).getCategoriesForMetaClass(metaClass);
        for (Category category : categoriesForMetaClass) {
            if (Boolean.TRUE.equals(category.getIsDefault())) {
                return category;
            }
        }

        return null;
    }
}