/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.SetValueEntity;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.data.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Specific datasource for runtime properties.
 * It will be initialized only when main datasource will be valid.
 *
 * @author devyatkin
 * @version $Id$
 */
public class RuntimePropsDatasourceImpl
        extends AbstractDatasource<RuntimePropertiesEntity>
        implements RuntimePropsDatasource<RuntimePropertiesEntity> {

    protected DsContext dsContext;
    protected DataSupplier dataSupplier;
    protected MetaClass metaClass;
    protected View view;
    protected Datasource mainDs;
    protected boolean inittedBefore = false;
    protected boolean categoryChanged = false;

    protected State state = State.NOT_INITIALIZED;

    protected RuntimePropertiesEntity item;

    public RuntimePropsDatasourceImpl(DsContext dsContext, DataSupplier dataSupplier, String id, String mainDsId) {
        this.id = id;
        this.dsContext = dsContext;
        this.dataSupplier = dataSupplier;
        this.metaClass = new RuntimePropertiesMetaClass();
        this.setMainDs(mainDsId);

        this.setCommitMode(CommitMode.DATASTORE);

        mainDs.addListener(new DsListenerAdapter() {
            public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                if (property.equals("category")) {
                    categoryChanged=true;
                    initMetaClass();
                }
            }
        });
    }

    @Override
    public void setup(DsContext dsContext, DataSupplier dataSupplier, String id,
                      MetaClass metaClass, @Nullable View view) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    protected void initMetaClass() {
        for (MetaProperty property : metaClass.getProperties()) {
            itemToCreate.clear();
            itemToUpdate.clear();
            itemToDelete.add(item.getCategoryValue(property.getName()));
        }
        metaClass.getProperties().clear();

        CategorizedEntity entity = (CategorizedEntity) mainDs.getItem();

        if (!inittedBefore && PersistenceHelper.isNew(entity) && (entity.getCategory() == null)) {
            entity.setCategory(getDefaultCategory(entity));
        }

        LoadContext valuesContext = new LoadContext(CategoryAttributeValue.class);
        LoadContext.Query query = valuesContext.setQueryString("select a from sys$CategoryAttributeValue a" +
                ",sys$CategoryAttribute atr where a.entityId =:e and a.categoryAttribute=atr and atr.category.id=:cat ");
        query.setParameter("e", entity.getUuid());
        query.setParameter("cat", entity.getCategory());
        valuesContext.setView("categoryAttributeValue");
        List<CategoryAttributeValue> entityValues = dataSupplier.loadList(valuesContext);

        LoadContext attributesContext = new LoadContext(CategoryAttribute.class);
        LoadContext.Query attributeQuery = attributesContext.setQueryString("select a from sys$CategoryAttribute a " +
                "where a.category.id=:cat order by a.orderNo");
        attributeQuery.setParameter("cat", entity.getCategory());
        attributesContext.setView(View.LOCAL);
        List<CategoryAttribute> attributes = dataSupplier.loadList(attributesContext);

        Map<String, Object> variables = new HashMap<>();
        Map<String, CategoryAttributeValue> categoryValues = new HashMap<>();

        TimeSource timeSource = AppBeans.get(TimeSource.NAME);
        for (CategoryAttribute attribute : attributes) {
            CategoryAttributeValue attrValue = getValue(attribute, entityValues);
            Object value;
            if (attrValue == null) {
                attrValue = new CategoryAttributeValue();
                attrValue.setCategoryAttribute(attribute);
                attrValue.setEntityId(entity.getId());
                if (PersistenceHelper.isNew(entity) || categoryChanged) {
                    attrValue.setStringValue(StringUtils.trimToNull(attribute.getDefaultString()));
                    attrValue.setIntValue(attribute.getDefaultInt());
                    attrValue.setDoubleValue(attribute.getDefaultDouble());
                    attrValue.setBooleanValue(attribute.getDefaultBoolean());
                    attrValue.setDateValue(BooleanUtils.isTrue(attribute.getDefaultDateIsCurrent()) ?
                            timeSource.currentTimestamp() : attribute.getDefaultDate());
                    attrValue.setEntityValue(attribute.getDefaultEntityId());
                    value = parseValue(attribute, attrValue);
                    itemToUpdate.add(attrValue);
                    modified = true;
                } else {
                    value = null;
                }
            } else {
                value = parseValue(attribute, attrValue);
            }
            categoryValues.put(attribute.getName(), attrValue);
            variables.put(attribute.getName(), value);
            RuntimePropertiesMetaProperty property = new RuntimePropertiesMetaProperty(
                    this.metaClass,
                    attribute.getName(),
                    RuntimePropertiesHelper.getAttributeClass(attribute));
            ((RuntimePropertiesMetaClass) this.metaClass).addProperty(property);
            if (RuntimePropertiesHelper.getAttributeClass(attribute).equals(SetValueEntity.class)) {
                createOptionsDatasource(attribute, (SetValueEntity) value);
            }
        }

        view = new View(RuntimePropertiesEntity.class);
        Collection<MetaProperty> properties = metaClass.getProperties();
        for (MetaProperty property : properties) {
            view.addProperty(property.getName());
        }

        item = new RuntimePropertiesEntity(metaClass, variables, categoryValues);
        item.addListener(new com.haulmont.chile.core.common.ValueListener() {
            public void propertyChanged(Object item, String property, Object prevValue, Object value) {
                modified = true;
                itemToUpdate.add(((RuntimePropertiesEntity) item).getCategoryValue(property));
            }
        });

        this.valid();
        inittedBefore = true;
        if (!itemToDelete.isEmpty()) {
            modified = true;
        }
    }

    protected void createOptionsDatasource(CategoryAttribute attribute, final SetValueEntity attributeValue) {
        final String property = attribute.getName();
        final String id = property;

        final MetaClass metaClass = this.getMetaClass();
        final MetaProperty metaProperty = metaClass.getProperty(property);
        if (metaProperty == null) {
            throw new DevelopmentException(
                    String.format("Can't find property '%s' in datasource '%s'", property, this.getId()));
        }
        DsBuilder builder = new DsBuilder(getDsContext());
        builder.reset().setMetaClass(metadata.getSession().getClass(SetValueEntity.class)).setId(id)
                .setViewName("_minimal").setSoftDeletion(false);

        final CollectionDatasource datasource;

        datasource = builder
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

    protected CategoryAttributeValue getValue(CategoryAttribute attribute, List<CategoryAttributeValue> entityValues) {

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
            final DataSupplier supplier = getDataSupplier();
            item = supplier.commit(item, getView());
            clearCommitLists();
            modified = false;

        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public RuntimePropertiesEntity getItem() {
        if (State.VALID.equals(state))
            return item;
        else
            throw new IllegalStateException("RuntimePropsDataSource state is " + state);
    }

    @Nullable
    @Override
    public RuntimePropertiesEntity getItemIfValid() {
        return getState() == State.VALID ? getItem() : null;
    }

    @Override
    public void setItem(RuntimePropertiesEntity item) {
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
        for (Entity entity : entities) {
            if (entity.equals(item)) {
                detachListener(item);
                item = (RuntimePropertiesEntity) entity;
                attachListener(item);
            }
        }
        modified = false;
        clearCommitLists();
    }

    protected void setMainDs(String name) {
        mainDs = dsContext.get(name);
        if (mainDs == null)
            throw new DevelopmentException("runtimePropsDatasource initialization error: mainDs '" + name + "' does not exists");
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

    public Category getDefaultCategory(Entity entity) {
        LoadContext categoryContext = new LoadContext(Category.class);
        LoadContext.Query query = categoryContext.setQueryString(
                "select c from sys$Category c where c.isDefault = true and c.entityType=:type ");
        query.setParameter("type", metadata.getSession().getClassNN(entity.getClass()).getName());
        categoryContext.setView("_minimal");
        List<Category> categories = dataSupplier.loadList(categoryContext);
        if (!categories.isEmpty())
            return categories.iterator().next();
        else
            return null;
    }
}