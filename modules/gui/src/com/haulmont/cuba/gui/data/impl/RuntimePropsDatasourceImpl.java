/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.SetValueEntity;
import com.haulmont.cuba.gui.data.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */

public class RuntimePropsDatasourceImpl extends AbstractDatasource<RuntimePropertiesEntity> implements RuntimePropsDatasource<RuntimePropertiesEntity> {

    private DsContext dsContext;
    private DataService dataService;
    private MetaClass metaClass;
    private String viewName;
    private View view;
    private Datasource mainDs;
    private boolean inittedBefore = false;


    protected State state = State.NOT_INITIALIZED;

    private RuntimePropertiesEntity item;


    public RuntimePropsDatasourceImpl(DsContext dsContext, DataService dataService,
                                      String id, String viewName, String mainDsId) {
        super(id);
        this.dsContext = dsContext;
        this.dataService = dataService;
        this.viewName = viewName;
        this.metaClass = new RuntimePropertiesMetaClass();
        this.setMainDs(mainDsId);

        this.setCommitMode(CommitMode.DATASTORE);

        mainDs.addListener(new DsListenerAdapter() {
            public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                if (property.equals("category")) {
                    initMetaClass();
                }
            }
        });
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
        LoadContext.Query query = valuesContext.setQueryString("select a from sys$CategoryAttributeValue a,sys$CategoryAttribute atr where a.entityId =:e and a.categoryAttribute=atr and atr.category.id=:cat ");
        query.addParameter("e", entity.getUuid());
        query.addParameter("cat", entity.getCategory());
        valuesContext.setView("categoryAttributeValue");
        List<CategoryAttributeValue> entityValues = dataService.loadList(valuesContext);

        LoadContext attributesContext = new LoadContext(CategoryAttribute.class);
        LoadContext.Query attributeQuery = attributesContext.setQueryString("select a from sys$CategoryAttribute a where a.category.id=:cat order by a.orderNo");
        attributeQuery.addParameter("cat", entity.getCategory());
        valuesContext.setView("_local");
        List<CategoryAttribute> attributes = dataService.loadList(attributesContext);

        Map<String, Object> variables = new HashMap<String, Object>();
        Map<String, CategoryAttributeValue> categoryValues = new HashMap<String, CategoryAttributeValue>();

        for (CategoryAttribute attribute : attributes) {
            CategoryAttributeValue attrValue = getValue(attribute, entityValues);
            if (attrValue == null) {
                attrValue = new CategoryAttributeValue();
                attrValue.setCategoryAttribute(attribute);
                attrValue.setEntityId(entity.getId());
                attrValue.setStringValue(attribute.getDefaultString());
                attrValue.setIntValue(attribute.getDefaultInt());
                attrValue.setDoubleValue(attribute.getDefaultDouble());
                attrValue.setBooleanValue(attribute.getDefaultBoolean());
                attrValue.setDateValue(attribute.getDefaultDate());
                attrValue.setEntityValue(attribute.getDefaultEntityId());
            }
            categoryValues.put(attribute.getName(), attrValue);
            Object value = parseValue(attribute, attrValue);
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
    }

    private void createOptionsDatasource(CategoryAttribute attribute, SetValueEntity attributeValue) {
        String property = attribute.getName();
        final String id = property;

        final MetaClass metaClass = this.getMetaClass();
        final MetaProperty metaProperty = metaClass.getProperty(property);
        if (metaProperty == null) {
            throw new IllegalStateException(
                    String.format("Can't find property '%s' in datasource '%s'", property, this.getId()));
        }
        DsBuilder builder = new DsBuilder(getDsContext());
        builder.reset().setMetaClass(MetadataProvider.getSession().getClass(SetValueEntity.class)).setId(id).setViewName("_minimal").setSoftDeletion(false);

        final CollectionDatasource datasource;

        datasource = builder
                .setFetchMode(CollectionDatasource.FetchMode.ALL)
                .setRefreshMode(CollectionDatasource.RefreshMode.NEVER)
                .buildCollectionDatasource();
        List<SetValueEntity> options = getOptions(attribute, attributeValue);
        for (SetValueEntity option : options) {
            datasource.includeItem(option);
        }

        ((DatasourceImpl) datasource).valid();
        ((DsContextImplementation) getDsContext()).register(datasource);
    }

    private List<SetValueEntity> getOptions(CategoryAttribute attribute, SetValueEntity attributeValue) {
        String enumeration = attribute.getEnumeration();
        String[] values = StringUtils.split(enumeration, ',');
        List<SetValueEntity> options = new LinkedList<SetValueEntity>();
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

    private CategoryAttributeValue getValue(CategoryAttribute attribute, List<CategoryAttributeValue> entityValues) {

        for (CategoryAttributeValue attrValue : entityValues) {
            if (attrValue.getCategoryAttribute().equals(attribute))
                return attrValue;
        }
        return null;
    }

    private Object parseValue(CategoryAttribute attribute, CategoryAttributeValue attrValue) {
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

                    if (attrValue != null)
                        return new SetValueEntity(attrValue.getStringValue());
                    else {
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

    private Entity parseEntity(String entityType, UUID uuid) {

        Entity entity;
        try {
            Class clazz = Class.forName(entityType);
            LoadContext entitiesContext = new LoadContext(clazz);
            String entityClassName = MetadataProvider.getSession().getClass(clazz).getName();
            LoadContext.Query query = entitiesContext.setQueryString("select a from " + entityClassName + " a where a.id =:e");
            query.addParameter("e", uuid);
            entitiesContext.setView("_local");
            entity = dataService.load(entitiesContext);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("can't parse entity " + entityType + " " + uuid, e);
        }
        return entity;
    }


    public DsContext getDsContext() {
        return dsContext;
    }

    public DataService getDataService() {
        return dataService;
    }

    public void commit() {
        if (Datasource.CommitMode.DATASTORE.equals(getCommitMode())) {
            final DataService service = getDataService();
            item = service.commit(item, getView());
            clearCommitLists();
            modified = false;

        } else {
            throw new UnsupportedOperationException();
        }
    }


    public State getState() {
        return state;
    }

    public RuntimePropertiesEntity getItem() {
        if (State.VALID.equals(state))
            return item;
        else
            throw new IllegalStateException("RuntimePropsDataSource state is " + state);
    }

    public void setItem(RuntimePropertiesEntity item) {
        throw new UnsupportedOperationException();
    }

    public void invalidate() {
        if (State.NOT_INITIALIZED != this.state) {
            final State prevStatus = this.state;
            this.state = State.INVALID;
            fireStateChanged(prevStatus);
        }
        modified = false;
        clearCommitLists();
    }

    public void refresh() {
        initMetaClass();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

    public View getView() {
        return null;
    }

    public void initialized() {
        final State prev = state;
        state = State.INVALID;
        fireStateChanged(prev);
    }

    public void valid() {
        final State prev = state;
        state = State.VALID;
        fireStateChanged(prev);
    }

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

    private void setMainDs(String name) {
        mainDs = dsContext.get(name);
        mainDs.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void stateChanged(Datasource ds, State prevState, State state) {
                        if (State.VALID.equals(state)) {
                            initMetaClass();
                        }
                    }
                }
        );
    }

    public Datasource getMainDs() {
        return mainDs;
    }

    public Category getDefaultCategory(Entity entity) {
        LoadContext categoryContext = new LoadContext(Category.class);
        LoadContext.Query query = categoryContext.setQueryString(
                "select c from sys$Category c where c.isDefault = true and c.entityType=:type ");
        query.addParameter("type", MetadataProvider.getSession().getClass(entity.getClass()).getName());
        categoryContext.setView("_minimal");
        List<Category> categories = dataService.loadList(categoryContext);
        if (!categories.isEmpty())
            return categories.iterator().next();
        else
            return null;
    }
}
