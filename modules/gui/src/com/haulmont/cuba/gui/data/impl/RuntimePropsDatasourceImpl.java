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

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesMetaClass;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesMetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Specific datasource for dynamic attributes.
 * It will be initialized only when main datasource will be valid.
 *
 */
public class RuntimePropsDatasourceImpl
        extends AbstractDatasource<DynamicAttributesEntity>
        implements RuntimePropsDatasource<DynamicAttributesEntity> {

    protected DsContext dsContext;
    protected DataSupplier dataSupplier;
    protected DynamicAttributesGuiTools dynamicAttributesGuiTools;
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

    public RuntimePropsDatasourceImpl(DsContext dsContext, DataSupplier dataSupplier, String id, String mainDsId, @Nullable MetaClass categorizedEntityClass) {
        this.categorizedEntityClass = categorizedEntityClass;
        this.id = id;
        this.dsContext = dsContext;
        this.dataSupplier = dataSupplier;
        this.dynamicAttributesGuiTools = AppBeans.get(DynamicAttributesGuiTools.NAME);

        this.metaClass = new DynamicAttributesMetaClass();
        this.setMainDs(mainDsId);
        this.setCommitMode(CommitMode.DATASTORE);

        this.attributes = AppBeans.get(DynamicAttributes.NAME, DynamicAttributes.class).getAttributesForMetaClass(resolveCategorizedEntityClass());
        for (CategoryAttribute attribute : attributes) {
            MetaProperty metaProperty = DynamicAttributesUtils.getMetaPropertyPath(mainDs.getMetaClass(), attribute).getMetaProperty();
            this.metaClass.addProperty(metaProperty, attribute);
        }
    }

    @Override
    public void setup(DsContext dsContext, DataSupplier dataSupplier, String id,
                      MetaClass metaClass, @Nullable View view) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public MetaClass resolveCategorizedEntityClass() {
        if (categorizedEntityClass == null) {
            return mainDs.getMetaClass();
        } else {
            return categorizedEntityClass;
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
        //just do nothing
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
        initMetaClass(mainDs.getItem());
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
        if (!State.VALID.equals(state)) {
            return;
        }

        for (Entity entity : entities) {
            if (entity.equals(mainDs.getItem())) {
                initMetaClass(entity);
            }
        }
        modified = false;
        clearCommitLists();
    }

    @Override
    public Datasource getMainDs() {
        return mainDs;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<DynamicAttributesMetaProperty> getPropertiesFilteredByCategory() {
        Collection propertiesFilteredByCategory = metaClass.getPropertiesFilteredByCategory(category);
        return (Collection<DynamicAttributesMetaProperty>) propertiesFilteredByCategory;
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

    protected void setMainDs(String name) {
        mainDs = dsContext.get(name);
        if (mainDs == null) {
            throw new DevelopmentException("runtimePropsDatasource initialization error: mainDs '" + name + "' does not exists");
        }
        mainDs.setLoadDynamicAttributes(true);

        dynamicAttributesGuiTools.listenDynamicAttributesChanges(mainDs);

        //noinspection unchecked
        mainDs.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void stateChanged(Datasource ds, State prevState, State state) {
                        if (State.VALID.equals(state)) {
                            if (!State.VALID.equals(prevState))
                                initMetaClass(mainDs.getItem());
                            else
                                valid();
                        }
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        if (property.equals("category")) {
                            categoryChanged = true;
                            initMetaClass(mainDs.getItem());
                        }
                    }

                    @Override
                    public void itemChanged(Datasource ds, @Nullable Entity prevItem, @Nullable Entity item) {
                        initMetaClass(item);
                    }
                }
        );
    }

    protected void initMetaClass(Entity entity) {
        if (!(entity instanceof BaseGenericIdEntity)) {
            throw new IllegalStateException("This datasource can contain only entity with subclass of BaseGenericIdEntity");
        }

        BaseGenericIdEntity baseGenericIdEntity = (BaseGenericIdEntity) entity;
        if (PersistenceHelper.isNew(baseGenericIdEntity) && baseGenericIdEntity.getDynamicAttributes() == null) {
            baseGenericIdEntity.setDynamicAttributes(new HashMap<>());
        }

        @SuppressWarnings("unchecked")
        Map<String, CategoryAttributeValue> dynamicAttributes = baseGenericIdEntity.getDynamicAttributes();
        Preconditions.checkNotNullArgument(dynamicAttributes, "Dynamic attributes should be loaded explicitly");

        if (baseGenericIdEntity instanceof Categorized) {
            category = ((Categorized) baseGenericIdEntity).getCategory();
        }
        if (!initializedBefore && category == null) {
            category = getDefaultCategory();
            if (baseGenericIdEntity.getMetaClass().getProperty("category") != null) {
                baseGenericIdEntity.setValue("category", category);
            }
        }

        item = new DynamicAttributesEntity(baseGenericIdEntity, attributes);
        dynamicAttributesGuiTools.initDefaultAttributeValues(baseGenericIdEntity);

        view = new View(DynamicAttributesEntity.class, false);
        Collection<MetaProperty> properties = metaClass.getProperties();
        for (MetaProperty property : properties) {
            view.addProperty(property.getName());
        }

        valid();
        initializedBefore = true;
        fireItemChanged(null);
    }
}