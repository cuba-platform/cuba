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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Enumeration;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.OptionsField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.EnumerationContainer;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.ObjectContainer;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;
import org.apache.commons.lang.StringUtils;

import java.util.*;

import static com.haulmont.cuba.gui.ComponentsHelper.handleFilteredAttributes;

public abstract class WebAbstractOptionsField<T extends com.vaadin.ui.AbstractSelect>
        extends WebAbstractField<T>
        implements OptionsField {

    protected List optionsList;
    protected Map<String, Object> optionsMap;
    protected Class<? extends EnumClass> optionsEnum;
    protected CollectionDatasource optionsDatasource;

    protected CaptionMode captionMode = CaptionMode.ITEM;
    protected String captionProperty;
    protected String descriptionProperty;

    /**
     * In the initialization list of options updating the data source is prohibited
     */
    protected boolean optionsInitialization = false;

    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        final MetaClass metaClass = datasource.getMetaClass();
        resolveMetaPropertyPath(metaClass, property);

        if (metaProperty.getRange().getCardinality() != null) {
            setMultiSelect(metaProperty.getRange().getCardinality().isMany());
        }

        final ItemWrapper wrapper = createDatasourceWrapper(datasource, Collections.singleton(metaPropertyPath));
        final Property itemProperty = wrapper.getItemProperty(metaPropertyPath);

        setRequired(metaProperty.isMandatory());
        if (StringUtils.isEmpty(getRequiredMessage())) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            setRequiredMessage(messageTools.getDefaultRequiredMessage(metaClass, property));
        }

        if (metaProperty.getRange().isEnum()) {
            Enumeration enumeration = metaProperty.getRange().asEnumeration();
            List options = Arrays.asList(enumeration.getJavaClass().getEnumConstants());
            setComponentContainerDs(createEnumContainer(options));

            setCaptionMode(CaptionMode.ITEM);
        }

        if (DynamicAttributesUtils.isDynamicAttribute(metaProperty)) {
            CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(metaProperty);
            if (categoryAttribute != null && categoryAttribute.getDataType() == PropertyType.ENUMERATION) {
                setOptionsList(categoryAttribute.getEnumerationOptions());
            }
        }

        component.setPropertyDataSource(itemProperty);

        if (metaProperty.isReadOnly()) {
            setEditable(false);
        }

        handleFilteredAttributes(this, this.datasource, metaPropertyPath);
        this.datasource.addItemChangeListener(e -> handleFilteredAttributes(this, this.datasource, metaPropertyPath));
    }

    protected EnumerationContainer createEnumContainer(List options) {
        return new EnumerationContainer(options);
    }

    protected ObjectContainer createObjectContainer(List opts) {
        return new ObjectContainer(opts);
    }

    @Override
    public void setOptionsMap(Map<String, Object> options) {
        if (metaProperty != null && metaProperty.getRange().isEnum()) {
            List constants = Arrays.asList(metaProperty.getRange().asEnumeration().getJavaClass().getEnumConstants());
            List opts = new ArrayList();

            for (Map.Entry<String, Object> entry : options.entrySet()) {
                String key = entry.getKey();
                Object itemId = entry.getValue();

                component.setItemCaption(itemId, key);
                if (!constants.contains(itemId)) {
                    throw new UnsupportedOperationException(itemId + " is not of class of meta property" + metaProperty);
                }
                opts.add(itemId);
            }
            this.optionsList = opts;

            setComponentContainerDs(createEnumContainer(opts));
            setCaptionMode(CaptionMode.ITEM);
        } else {
            List opts = new ArrayList();
            for (Map.Entry<String, Object> entry : options.entrySet()) {
                String key = entry.getKey();
                Object itemId = entry.getValue();

                component.setItemCaption(itemId, key);
                opts.add(itemId);
            }
            setComponentContainerDs(createObjectContainer(opts));
            component.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
            this.optionsMap = options;
        }
    }

    protected void setComponentContainerDs(com.vaadin.data.Container newDataSource) {
        optionsInitialization = true;
        component.setContainerDataSource(newDataSource);
        optionsInitialization = false;
    }

    @Override
    public void setOptionsList(List optionsList) {
        if (metaProperty != null) {
            Object currentValue = component.getValue();
            if (metaProperty.getRange().isEnum()) {
                setComponentContainerDs(createEnumContainer(optionsList));
                setCaptionMode(CaptionMode.ITEM);
            } else {
                setComponentContainerDs(createObjectContainer(optionsList));
                component.setItemCaptionMode(AbstractSelect.ItemCaptionMode.ITEM);
            }

            component.setValue(currentValue);
            this.optionsList = optionsList;
        } else if (!optionsList.isEmpty()) {
            final Object o = optionsList.iterator().next();
            if (o instanceof Enum) {
                setComponentContainerDs(createEnumContainer(optionsList));
            } else {
                setComponentContainerDs(createObjectContainer(optionsList));
            }
            setCaptionMode(CaptionMode.ITEM);
            this.optionsList = optionsList;
        } else {
            setComponentContainerDs(createObjectContainer(optionsList));
            setCaptionMode(CaptionMode.ITEM);
            this.optionsList = optionsList;
        }
    }

    @Override
    public Class<? extends EnumClass> getOptionsEnum() {
        return optionsEnum;
    }

    @Override
    public void setOptionsEnum(Class<? extends EnumClass> optionsEnum) {
        Object currentValue = null;
        if (metaProperty != null) {
            currentValue = component.getValue();
        }
        List options = Arrays.asList(optionsEnum.getEnumConstants());
        setComponentContainerDs(createEnumContainer(options));
        setCaptionMode(CaptionMode.ITEM);

        if (metaProperty != null) {
            component.setValue(currentValue);
        }

        this.optionsEnum = optionsEnum;
    }

    @Override
    public boolean isMultiSelect() {
        return component.isMultiSelect();
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        component.setMultiSelect(multiselect);
    }

    @Override
    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        this.captionMode = captionMode;
        switch (captionMode) {
            case ITEM:
                component.setItemCaptionMode(AbstractSelect.ItemCaptionMode.ITEM);
                break;

            case PROPERTY:
                component.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                break;

            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public String getCaptionProperty() {
        return captionProperty;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
        if (optionsDatasource != null) {
            component.setItemCaptionPropertyId(optionsDatasource.getMetaClass().getProperty(captionProperty));
        }
    }

    @Override
    public String getDescriptionProperty() {
        return descriptionProperty;
    }

    @Override
    public void setDescriptionProperty(String descriptionProperty) {
        this.descriptionProperty = descriptionProperty;
        //todo gorodnov: support description for all option fields
    }

    @Override
    public List getOptionsList() {
        return optionsList;
    }

    @Override
    public Map<String, Object> getOptionsMap() {
        return optionsMap;
    }

    @Override
    public CollectionDatasource getOptionsDatasource() {
        return optionsDatasource;
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        this.optionsDatasource = datasource;
        setComponentContainerDs(new CollectionDsWrapper(datasource, true));

        if (captionProperty != null) {
            component.setItemCaptionPropertyId(optionsDatasource.getMetaClass().getProperty(captionProperty));
        }
    }

    @SuppressWarnings({"unchecked"})
    protected <T> T wrapAsCollection(Object o) {
        if (isMultiSelect()) {
            if (o instanceof Collection) {
                return (T) Collections.unmodifiableCollection((Collection) o);
            } else if (o != null) {
                return (T) Collections.singleton(o);
            } else {
                return (T) Collections.emptySet();
            }
        } else {
            return (T) o;
        }
    }
}