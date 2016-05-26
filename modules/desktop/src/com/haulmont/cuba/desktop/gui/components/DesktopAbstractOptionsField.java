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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Enumeration;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.gui.executors.impl.DesktopBackgroundWorker;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.OptionsField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.haulmont.cuba.gui.ComponentsHelper.handleFilteredAttributes;

public abstract class DesktopAbstractOptionsField<C extends JComponent>
        extends
            DesktopAbstractField<C>
        implements
            OptionsField {

    protected CaptionMode captionMode = CaptionMode.ITEM;
    protected String captionProperty;
    protected String descProperty;

    protected CollectionDatasource<Entity<Object>, Object> optionsDatasource;
    protected List optionsList;
    protected Map<String, Object> optionsMap;
    protected Class<? extends EnumClass> optionsEnum;

    protected Datasource datasource;
    protected boolean updatingInstance;

    protected Object prevValue;

    protected Messages messages = AppBeans.get(Messages.NAME);

    protected CaptionFormatter captionFormatter;

    public interface CaptionFormatter<T> {
        String formatValue(T value);
    }

    @Override
    public CollectionDatasource getOptionsDatasource() {
        return optionsDatasource;
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        this.optionsDatasource = datasource;

        assignAutoDebugId();
    }

    @Override
    public List getOptionsList() {
        return optionsList;
    }

    @Override
    public void setOptionsList(List optionsList) {
        this.optionsList = optionsList;
        this.captionMode = CaptionMode.ITEM; // works as web version
    }

    @Override
    public Map<String, Object> getOptionsMap() {
        return optionsMap;
    }

    @Override
    public void setOptionsMap(Map<String, Object> map) {
        optionsMap = map;
    }

    @Override
    public Class<? extends EnumClass> getOptionsEnum() {
        return optionsEnum;
    }

    @Override
    public void setOptionsEnum(Class<? extends EnumClass> optionsEnum) {
        this.optionsEnum = optionsEnum;
        this.captionMode = CaptionMode.ITEM; // works as web version
    }

    @Override
    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        this.captionMode = captionMode;
    }

    @Override
    public String getCaptionProperty() {
        return captionProperty;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
    }

    @Override
    public String getDescriptionProperty() {
        return descProperty;
    }

    @Override
    public void setDescriptionProperty(String descProperty) {
        this.descProperty = descProperty;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        if (datasource == null) {
            setValue(null);
            return;
        }

        MetaClass metaClass = datasource.getMetaClass();
        resolveMetaPropertyPath(metaClass, property);

        datasource.addItemChangeListener(e -> {
            if (updatingInstance)
                return;
            Object value = InstanceUtils.getValueEx(e.getItem(), metaPropertyPath.getPath());
            updateComponent(value);
            fireChangeListeners(value);
        });

        datasource.addItemPropertyChangeListener(e -> {
            if (updatingInstance)
                return;
            if (e.getProperty().equals(metaPropertyPath.toString())) {
                updateComponent(e.getValue());
                fireChangeListeners(e.getValue());
            }
        });

        setRequired(metaProperty.isMandatory());
        if (StringUtils.isEmpty(getRequiredMessage())) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            setRequiredMessage(messageTools.getDefaultRequiredMessage(metaClass, property));
        }

        if (metaProperty.getRange().isEnum()) {
            final Enumeration enumeration = metaProperty.getRange().asEnumeration();
            final Class<Enum> javaClass = enumeration.getJavaClass();

            setOptionsList(Arrays.asList(javaClass.getEnumConstants()));
            setCaptionMode(CaptionMode.ITEM);
        }

        if (DynamicAttributesUtils.isDynamicAttribute(metaProperty)) {
            CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(metaProperty);
            if (categoryAttribute != null && categoryAttribute.getDataType() == PropertyType.ENUMERATION) {
                setOptionsList(categoryAttribute.getEnumerationOptions());
            }
        }

        if ((datasource.getState() == Datasource.State.VALID) && (datasource.getItem() != null)) {
            Object newValue = InstanceUtils.getValueEx(datasource.getItem(), metaPropertyPath.getPath());
            updateComponent(newValue);
            fireChangeListeners(newValue);
        }

        if (metaProperty.isReadOnly()) {
            setEditable(false);
        }

        handleFilteredAttributes(this.datasource, metaProperty, this);
        this.datasource.addItemChangeListener(e -> handleFilteredAttributes(this.datasource, metaProperty, this));
    }

    protected void fireChangeListeners(Object newValue) {
        Object oldValue = prevValue;
        prevValue = newValue;
        if (!ObjectUtils.equals(oldValue, newValue)) {
            fireValueChanged(oldValue, newValue);
        }
    }

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

    @Override
    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return metaPropertyPath;
    }

    @Override
    public <T> T getValue() {
        Object selectedItem = getSelectedItem();
        return selectedItem != null && selectedItem instanceof ValueWrapper ?
                (T) ((ValueWrapper) selectedItem).getValue() : null;
    }

    protected void updateComponent(Object value) {
        if (value == null) {
            setSelectedItem(null);
            return;
        }

        Object selectedItem;
        if (optionsMap != null) {
            for (Map.Entry<String, Object> entry : optionsMap.entrySet()) {
                if (value.equals(entry.getValue())) {
                    setSelectedItem(new MapKeyWrapper(entry.getKey()));
                    return;
                }
            }
            setSelectedItem(null);
            return;
        }

        if (!(value instanceof ValueWrapper)) {
            if (value instanceof Entity) {
                selectedItem = new EntityWrapper((Entity) value);
            } else {
                selectedItem = new ObjectWrapper(value);
            }
        } else {
            selectedItem = value;
        }
        setSelectedItem(selectedItem);
    }

    protected void updateInstance(Object value) {
        updatingInstance = true;
        try {
            if (datasource != null && metaProperty != null && datasource.getItem() != null) {
                InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), value);
            }
        } finally {
            updatingInstance = false;
        }
    }

    @Override
    public void setValue(Object value) {
        DesktopBackgroundWorker.checkSwingUIAccess();

        if (!ObjectUtils.equals(prevValue, value)) {
            updateInstance(value);
            updateComponent(value);
            fireChangeListeners(value);
        } else {
            updateComponent(prevValue);
        }
    }

    /**
     * Set custom caption formatter. This formatter is only used when options are explicitly
     * set with {@link #setOptionsMap(java.util.Map)} or {@link #setOptionsList(java.util.List)}
     * @param captionFormatter
     */
    public void setCaptionFormatter(CaptionFormatter captionFormatter) {
        this.captionFormatter = captionFormatter;
    }

    protected String getDisplayString(Entity entity) {
        if (entity == null)
            return "";

        String captionValue;
        if (captionMode.equals(CaptionMode.PROPERTY) && !StringUtils.isBlank(captionProperty)) {
            captionValue = entity.getValueEx(captionProperty);
        } else {
            captionValue = entity.getInstanceName();
        }

        if (captionValue == null)
            captionValue = "";

        return captionValue;
    }

    protected abstract Object getSelectedItem();
    protected abstract void setSelectedItem(Object item);

    public interface ValueWrapper<T> {
        T getValue();
    }

    public class EntityWrapper implements ValueWrapper<Entity> {

        private Entity entity;

        public EntityWrapper(Entity entity) {
            this.entity = entity;
        }

        @Override
        public Entity getValue() {
            return entity;
        }

        @Override
        public String toString() {
            if (captionFormatter != null) {
                return captionFormatter.formatValue(entity);
            }
            return getDisplayString(entity);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof DesktopAbstractOptionsField.EntityWrapper) {
                DesktopAbstractOptionsField.EntityWrapper that = (EntityWrapper) obj;
                return ObjectUtils.equals(this.entity, that.entity);
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            if (entity != null) {
                return entity.hashCode();
            }
            return 0;
        }
    }

    public class MapKeyWrapper implements ValueWrapper<Object> {

        private String key;

        public MapKeyWrapper(String key) {
            this.key = key;
        }

        @Override
        public Object getValue() {
            return optionsMap.get(key);
        }

        @Override
        public String toString() {
            return key;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof DesktopAbstractOptionsField.MapKeyWrapper) {
                DesktopAbstractOptionsField.MapKeyWrapper other = (DesktopAbstractOptionsField.MapKeyWrapper) obj;
                return StringUtils.equals(this.key, other.key);
            }
            return false;
        }

        @Override
        public int hashCode() {
            if (key != null) {
                return key.hashCode();
            }
            return 0;
        }
    }

    public class ObjectWrapper implements ValueWrapper<Object> {

        private Object obj;

        public ObjectWrapper(Object obj) {
            this.obj = obj;
        }

        @Override
        public Object getValue() {
            return obj;
        }

        @Override
        public String toString() {
            if (captionFormatter != null) {
                return captionFormatter.formatValue(obj);
            }

            if (obj == null)
                return "";

            if (obj instanceof Instance)
                return InstanceUtils.getInstanceName((Instance) obj);

            if (obj instanceof Enum)
                return messages.getMessage((Enum) obj);

            return obj.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof DesktopAbstractOptionsField.ObjectWrapper) {
                DesktopAbstractOptionsField.ObjectWrapper anotherWrapper = (DesktopAbstractOptionsField.ObjectWrapper) obj;
                return ObjectUtils.equals(this.obj, anotherWrapper.obj);
            }
            return false;
        }

        @Override
        public int hashCode() {
            if (obj != null) {
                return obj.hashCode();
            }
            return 0;
        }
    }
}