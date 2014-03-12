/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Enumeration;
import com.haulmont.chile.core.model.MetaClass;
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

/**
 * @author abramov
 * @version $Id$
 */
public abstract class WebAbstractOptionsField<T extends com.vaadin.ui.AbstractSelect>
    extends
        WebAbstractField<T>
    implements
        OptionsField {

    protected List optionsList;
    protected Map<String, Object> optionsMap;
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
        metaPropertyPath = metaClass.getPropertyPath(property);
        try {
            metaProperty = metaPropertyPath.getMetaProperty();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Metaproperty name is possibly wrong: " + property, e);
        }

        if (metaProperty.getRange().getCardinality() != null){
            setMultiSelect(metaProperty.getRange().getCardinality().isMany());
        }

        final ItemWrapper wrapper = createDatasourceWrapper(datasource, Collections.singleton(metaPropertyPath));
        final Property itemProperty = wrapper.getItemProperty(metaPropertyPath);

        setRequired(metaProperty.isMandatory());
        if (StringUtils.isEmpty(getRequiredMessage())) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            setRequiredMessage(messageTools.getDefaultRequiredMessage(metaProperty));
        }

        if (metaProperty.getRange().isEnum()) {
            Enumeration enumeration = metaProperty.getRange().asEnumeration();
            List options = Arrays.asList(enumeration.getJavaClass().getEnumConstants());
            setComponentContainerDs(createEnumContainer(options));
            setCaptionMode(CaptionMode.ITEM);
        }
        
        component.setPropertyDataSource(itemProperty);
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

            for (String key : options.keySet()) {
                Object itemId = options.get(key);
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
            for (String key : options.keySet()) {
                Object itemId = options.get(key);
                component.setItemCaption(itemId, key);
                opts.add(itemId);
            }
            setComponentContainerDs(createObjectContainer(opts));
            component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_EXPLICIT_DEFAULTS_ID);
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
                component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_EXPLICIT_DEFAULTS_ID);
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
            case ITEM: {
                component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_ITEM);
                break;
            }
            case PROPERTY: {
                component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
                break;
            }
            default: {
                throw new UnsupportedOperationException();
            }
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
                return (T) Collections.unmodifiableCollection((Collection)o);
            } else if (o != null) {
                return (T) Collections.singleton(o);
            } else {
                return (T) Collections.emptySet();
            }
        } else {
            return (T) o;
        }
    }

    protected abstract <T> T getValueFromKey(Object key);
    protected abstract Object getKeyFromValue(Object value);
}