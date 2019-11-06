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

package com.haulmont.cuba.gui.components.data.value;

import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.sys.BeanLocatorAware;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.RuntimePropsDatasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;

import java.util.Objects;
import java.util.function.Consumer;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class DatasourceValueSource<E extends Entity, V> implements EntityValueSource<E, V>, BeanLocatorAware {
    protected final Datasource<E> datasource;

    protected MetaPropertyPath metaPropertyPath;
    protected String property;

    protected BindingState state = BindingState.INACTIVE;

    protected EventHub events = new EventHub();

    protected boolean dataModelSecurityEnabled = true;

    public DatasourceValueSource(Datasource<E> datasource, String property) {
        checkNotNullArgument(datasource);
        checkNotNullArgument(property);

        this.datasource = datasource;
        this.property = property;
    }

    /**
     * Sets data model security enabled for data binding.
     * <br>
     * Caller may set false in order to disable built-in security check on data binding.
     *
     * @param enabled enabled flag
     */
    public void setDataModelSecurityEnabled(boolean enabled) {
        this.dataModelSecurityEnabled = enabled;
    }

    @Override
    public void setBeanLocator(BeanLocator beanLocator) {
        MetaClass metaClass = datasource instanceof RuntimePropsDatasource ?
                ((RuntimePropsDatasource<E>) datasource).resolveCategorizedEntityClass() :
                datasource.getMetaClass();

        MetadataTools metadataTools = beanLocator.get(MetadataTools.NAME);
        MetaPropertyPath metaPropertyPath = metadataTools.resolveMetaPropertyPath(metaClass, property);

        checkNotNullArgument(metaPropertyPath, "Could not resolve property path '%s' in '%s'", property, metaClass);

        this.metaPropertyPath = metaPropertyPath;

        this.datasource.addStateChangeListener(this::datasourceStateChanged);
        this.datasource.addItemChangeListener(this::datasourceItemChanged);
        this.datasource.addItemPropertyChangeListener(this::datasourceItemPropertyChanged);

        if (datasource.getState() == Datasource.State.VALID) {
            setState(BindingState.ACTIVE);
        }
    }

    public void setState(BindingState state) {
        if (this.state != state) {
            this.state = state;

            events.publish(StateChangeEvent.class, new StateChangeEvent(this,  state));
        }
    }

    public Datasource getDatasource() {
        return datasource;
    }

    @Override
    public MetaClass getEntityMetaClass() {
        return datasource.getMetaClass();
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return metaPropertyPath;
    }

    @Override
    public E getItem() {
        return datasource.getItem();
    }

    @Override
    public boolean isDataModelSecurityEnabled() {
        return dataModelSecurityEnabled;
    }

    @Override
    public V getValue() {
        E item = datasource.getItem();
        if (item != null) {
            return item.getValueEx(metaPropertyPath);
        }
        return null;
    }

    @Override
    public void setValue(Object value) {
        E item = datasource.getItem();
        if (item != null) {
            item.setValueEx(metaPropertyPath, value);
        }
    }

    @Override
    public boolean isReadOnly() {
        // todo what about security ?
        return metaPropertyPath.getMetaProperty().isReadOnly();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<V> getType() {
        return (Class<V>) metaPropertyPath.getMetaProperty().getJavaType();
    }

    @Override
    public BindingState getState() {
        if (datasource.getState() == Datasource.State.VALID
                && datasource.getItem() != null) {
            return BindingState.ACTIVE;
        }

        return BindingState.INACTIVE;
    }

    public boolean isModified() {
        return datasource.isModified();
    }

    public void setModified(boolean modified) {
        ((DatasourceImplementation) datasource).setModified(modified);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addInstanceChangeListener(Consumer<InstanceChangeEvent<E>> listener) {
        return events.subscribe(InstanceChangeEvent.class, (Consumer) listener);
    }

    @Override
    public Subscription addStateChangeListener(Consumer<StateChangeEvent> listener) {
        return events.subscribe(StateChangeEvent.class, listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<V>> listener) {
        return events.subscribe(ValueChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    protected void datasourceItemChanged(Datasource.ItemChangeEvent e) {
        if (e.getItem() != null && datasource.getState() == Datasource.State.VALID) {
            setState(BindingState.ACTIVE);
        } else {
            setState(BindingState.INACTIVE);
        }

        events.publish(InstanceChangeEvent.class, new InstanceChangeEvent(this, e.getPrevItem(), e.getItem()));
    }

    protected void datasourceStateChanged(Datasource.StateChangeEvent<E> e) {
        if (e.getState() == Datasource.State.VALID) {
            setState(BindingState.ACTIVE);
        } else {
            setState(BindingState.INACTIVE);
        }
    }

    @SuppressWarnings("unchecked")
    protected void datasourceItemPropertyChanged(Datasource.ItemPropertyChangeEvent e) {
        if (Objects.equals(e.getProperty(), metaPropertyPath.toPathString())) {
            events.publish(ValueChangeEvent.class, new ValueChangeEvent<>(this, (V)e.getPrevValue(), (V)e.getValue()));
        }
    }
}