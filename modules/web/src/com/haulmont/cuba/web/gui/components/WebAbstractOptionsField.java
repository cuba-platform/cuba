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

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.OptionsField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenersWrapper;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.EnumerationContainer;
import com.haulmont.cuba.web.gui.data.ObjectContainer;
import com.vaadin.v7.ui.AbstractSelect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class WebAbstractOptionsField<T extends com.vaadin.v7.ui.AbstractSelect, V>
        extends WebAbstractField<T, V>
        implements OptionsField<V> {

    protected List optionsList;
    protected Map<String, ?> optionsMap;
    protected Class<? extends EnumClass> optionsEnum;
    protected CollectionDatasource optionsDatasource;

    protected CollectionDsListenersWrapper collectionDsListenersWrapper;

    protected String captionProperty;
    protected String descriptionProperty;

    /*
todo
    @Override
    public void setDatasource(Datasource datasource, String property) {
        if ((datasource == null && property != null) || (datasource != null && property == null))
            throw new IllegalArgumentException("Datasource and property should be either null or not null at the same time");

        if (datasource == this.datasource && ((metaPropertyPath != null && metaPropertyPath.toString().equals(property)) ||
                (metaPropertyPath == null && property == null)))
            return;

        if (this.datasource != null) {
            metaProperty = null;
            metaPropertyPath = null;

            component.setPropertyDataSource(null);

            //noinspection unchecked
            this.datasource.removeItemChangeListener(securityWeakItemChangeListener);
            securityWeakItemChangeListener = null;

            this.datasource = null;

            if (itemWrapper != null) {
                itemWrapper.unsubscribe();
            }

            disableBeanValidator();
        }

        if (datasource != null) {
            // noinspection unchecked
            this.datasource = datasource;
            MetaClass metaClass = datasource.getMetaClass();
            resolveMetaPropertyPath(metaClass, property);

            if (metaProperty.getRange().getCardinality() != null) {
                setMultiSelect(metaProperty.getRange().getCardinality().isMany());
            }

            itemWrapper = createDatasourceWrapper(datasource, Collections.singleton(metaPropertyPath));
            Property itemProperty = itemWrapper.getItemProperty(metaPropertyPath);

            initRequired(metaPropertyPath);

            if (metaProperty.getRange().isEnum()) {
                Enumeration enumeration = metaProperty.getRange().asEnumeration();
                List options = Arrays.asList(enumeration.getJavaClass().getEnumConstants());
                setComponentContainerDs(createEnumContainer(options));

                setCaptionMode(CaptionMode.ITEM);
            }

            if (DynamicAttributesUtils.isDynamicAttribute(metaProperty)) {
                CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(metaProperty);
                if (categoryAttribute != null && categoryAttribute.getDataType() == PropertyType.ENUMERATION) {
                    setOptionsMap(categoryAttribute.getLocalizedEnumerationMap());
                }
            }

            component.setPropertyDataSource(itemProperty);

            if (metaProperty.isReadOnly()) {
                setEditable(false);
            }

            handleFilteredAttributes(this, this.datasource, metaPropertyPath);
            securityItemChangeListener = e -> handleFilteredAttributes(this, this.datasource, metaPropertyPath);
            securityWeakItemChangeListener = new WeakItemChangeListener(datasource, securityItemChangeListener);
            //noinspection unchecked
            this.datasource.addItemChangeListener(securityWeakItemChangeListener);

            initBeanValidator();
        }
    }*/

    protected EnumerationContainer createEnumContainer(List options) {
        return new EnumerationContainer(options);
    }

    protected ObjectContainer createObjectContainer(List opts) {
        return new ObjectContainer(opts);
    }
/*
    @Override
    public void setOptionsMap(Map<String, ?> options) {
        if (getMetaProperty() != null && getMetaProperty().getRange().isEnum()) {
            List constants = Arrays.asList(getMetaProperty().getRange().asEnumeration().getJavaClass().getEnumConstants());
            List opts = new ArrayList();

            for (Map.Entry<String, ?> entry : options.entrySet()) {
                String key = entry.getKey();
                Object itemId = entry.getValue();

                component.setItemCaption(itemId, key);
                if (!constants.contains(itemId)) {
                    throw new UnsupportedOperationException(itemId + " is not of class of meta property" + getMetaProperty());
                }
                opts.add(itemId);
            }
            this.optionsList = opts;

            setComponentContainerDs(createEnumContainer(opts));
            setCaptionMode(CaptionMode.ITEM);
        } else {
            List opts = new ArrayList();
            for (Map.Entry<String, ?> entry : options.entrySet()) {
                String key = entry.getKey();
                Object itemId = entry.getValue();

                component.setItemCaption(itemId, key);
                opts.add(itemId);
            }
            setComponentContainerDs(createObjectContainer(opts));
            component.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
            this.optionsMap = options;
        }
    }*/

    protected void setComponentContainerDs(com.vaadin.v7.data.Container newDataSource) {
        component.setContainerDataSource(newDataSource);
    }

    @Override
    public void setOptionsList(List optionsList) {
        if (getMetaProperty() != null) {
            Object currentValue = component.getValue();
            if (getMetaProperty().getRange().isEnum()) {
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

    /* vaadin8
    @Override
    public Class<? extends EnumClass> getOptionsEnum() {
        return optionsEnum;
    }

    @Override
    public void setOptionsEnum(Class<? extends EnumClass> optionsEnum) {
        Object currentValue = null;
        if (getMetaProperty() != null) {
            currentValue = component.getValue();
        }
        List options = Arrays.asList(optionsEnum.getEnumConstants());
        setComponentContainerDs(createEnumContainer(options));
        setCaptionMode(CaptionMode.ITEM);

        if (getMetaProperty() != null) {
            component.setValue(currentValue);
        }

        this.optionsEnum = optionsEnum;
    }*/

    public boolean isMultiSelect() {
        return component.isMultiSelect();
    }

    public void setMultiSelect(boolean multiselect) {
        component.setMultiSelect(multiselect);
    }

    @Override
    public CaptionMode getCaptionMode() {
        return WebWrapperUtils.toCaptionMode(component.getItemCaptionMode());
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        component.setItemCaptionMode(WebWrapperUtils.toVaadinCaptionMode(captionMode));
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
    public List getOptionsList() {
        return optionsList;
    }

    @Override
    public Map<String, ?> getOptionsMap() {
        return optionsMap;
    }

    @Override
    public CollectionDatasource getOptionsDatasource() {
        return optionsDatasource;
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        if (datasource == this.optionsDatasource)
            return;

        if (this.optionsDatasource != null) {
            com.vaadin.v7.data.Container containerDataSource = component.getContainerDataSource();
            if (containerDataSource instanceof CollectionDsWrapper) {
                CollectionDsWrapper wrapper = (CollectionDsWrapper) containerDataSource;
                wrapper.unsubscribe();
            }
            setComponentContainerDs(null);
        }

        this.optionsDatasource = datasource;

        if (datasource != null) {
            collectionDsListenersWrapper = createCollectionDsListenersWrapper();
            setComponentContainerDs(new CollectionDsWrapper(datasource, true, collectionDsListenersWrapper));

            if (captionProperty != null) {
                component.setItemCaptionPropertyId(optionsDatasource.getMetaClass().getProperty(captionProperty));
            }
        }
    }

    protected CollectionDsListenersWrapper createCollectionDsListenersWrapper() {
        return new CollectionDsListenersWrapper();
    }

    @SuppressWarnings({"unchecked"})
    protected <CT> CT wrapAsCollection(Object o) {
        if (isMultiSelect()) {
            if (o instanceof Collection) {
                return (CT) Collections.unmodifiableCollection((Collection) o);
            } else if (o != null) {
                return (CT) Collections.singleton(o);
            } else {
                return (CT) Collections.emptySet();
            }
        } else {
            return (CT) o;
        }
    }
}