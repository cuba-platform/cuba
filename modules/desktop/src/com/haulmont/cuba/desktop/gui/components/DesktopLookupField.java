/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopLookupField
    extends DesktopAbstractComponent<JComboBox>
    implements LookupField
{
    private BasicEventList<Object> items = new BasicEventList<Object>();
    private CaptionMode captionMode = CaptionMode.ITEM;
    private String captionProperty;
    private String descProperty;
    private CollectionDatasource<Entity<Object>, Object> optionsDatasource;
    private List<Object> optionsList;
    private Map<String, Object> optionsMap;
    private boolean required;
    private Datasource datasource;
    private MetaProperty metaProperty;
    private MetaPropertyPath metaPropertyPath;
    private boolean optionsInitialized;
    private boolean updatingInstance;
    private AutoCompleteSupport<Object> autoComplete;

    public DesktopLookupField() {
        impl = new JComboBox();
        impl.setEditable(true);
        impl.setPrototypeDisplayValue("AAAAAAAAAAAA");
        autoComplete = AutoCompleteSupport.install(impl, items);

        for (int i = 0; i < impl.getComponentCount(); i++) {
            java.awt.Component component = impl.getComponent(i);
            component.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    initOptions();
                }
            });
        }

        impl.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateValue((Wrapper) impl.getSelectedItem());
                    }
                }
        );

        setFilterMode(FilterMode.CONTAINS);

        DesktopComponentsHelper.adjustSize(impl);
    }

    private void updateValue(Wrapper selectedItem) {
        if (datasource != null && metaProperty != null) {
            updatingInstance = true;
            try {
                InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(),
                        selectedItem == null ? null : selectedItem.getValue());
            } finally {
                updatingInstance = false;
            }
        }
    }

    private void initOptions() {
        if (optionsInitialized)
            return;

        items.clear();

        if (optionsDatasource != null) {
            if (!optionsDatasource.getState().equals(Datasource.State.VALID)) {
                optionsDatasource.refresh();
            }
            for (Object id : optionsDatasource.getItemIds()) {
                items.add(new EntityWrapper(optionsDatasource.getItem(id)));
            }
        } else if (optionsMap != null) {
            for (String key : optionsMap.keySet()) {
                items.add(new MapKeyWrapper(key));
            }
        } else if (optionsList != null) {
            for (Object obj : optionsList) {
                items.add(new ObjectWrapper(obj));
            }
        }

        optionsInitialized = true;
    }

    @Override
    public Object getNullOption() {
        return null;
    }

    @Override
    public void setNullOption(Object nullOption) {
    }

    @Override
    public FilterMode getFilterMode() {
        return autoComplete.getFilterMode() == TextMatcherEditor.CONTAINS
                ? FilterMode.CONTAINS : FilterMode.STARTS_WITH;
    }

    @Override
    public void setFilterMode(FilterMode mode) {
        autoComplete.setFilterMode(FilterMode.CONTAINS.equals(mode)
                ? TextMatcherEditor.CONTAINS : TextMatcherEditor.STARTS_WITH);
    }

    @Override
    public boolean isNewOptionAllowed() {
        return false;
    }

    @Override
    public void setNewOptionAllowed(boolean newOptionAllowed) {
    }

    @Override
    public NewOptionHandler getNewOptionHandler() {
        return null;
    }

    @Override
    public void setNewOptionHandler(NewOptionHandler newOptionHandler) {
    }

    @Override
    public void disablePaging() {
    }

    @Override
    public boolean isMultiSelect() {
        return false;
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
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
    public CollectionDatasource getOptionsDatasource() {
        return optionsDatasource;
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        this.optionsDatasource = datasource;
    }

    @Override
    public List getOptionsList() {
        return optionsList;
    }

    @Override
    public void setOptionsList(List optionsList) {
        this.optionsList = optionsList;
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
    public boolean isRequired() {
        return required;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public void setRequiredMessage(String msg) {
    }

    @Override
    public <T> T getValue() {
        Wrapper selectedItem = (Wrapper) impl.getSelectedItem();
        return (T) selectedItem.getValue();
    }

    @Override
    public void setValue(Object value) {
        if (value == null) {
            impl.setSelectedItem(null);
            return;
        }

        Object selectedItem;
        if (optionsMap != null) {
            for (Map.Entry<String, Object> entry : optionsMap.entrySet()) {
                if (value.equals(entry.getValue())) {
                    impl.setSelectedItem(new MapKeyWrapper(entry.getKey()));
                    return;
                }
            }
            impl.setSelectedItem(null);
            return;
        }

        if (value instanceof Entity) {
            selectedItem = new EntityWrapper((Entity) value);
        } else
            selectedItem = new ObjectWrapper(value);
        impl.setSelectedItem(selectedItem);

    }

    @Override
    public void addListener(ValueListener listener) {
    }

    @Override
    public void removeListener(ValueListener listener) {
    }

    @Override
    public void addValidator(Validator validator) {
    }

    @Override
    public void removeValidator(Validator validator) {
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void validate() throws ValidationException {
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
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        final MetaClass metaClass = datasource.getMetaClass();
        metaPropertyPath = metaClass.getPropertyPath(property);
        try {
            metaProperty = metaPropertyPath.getMetaProperty();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Metaproperty name is possibly wrong: " + property, e);
        }

        datasource.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                        if (updatingInstance)
                            return;
                        Object value = InstanceUtils.getValueEx(item, metaPropertyPath.getPath());
                        if (value == null) {
                            impl.setSelectedItem(null);
                            return;
                        }

                        Object selectedItem;
                        if (optionsMap != null) {
                            for (Map.Entry<String, Object> entry : optionsMap.entrySet()) {
                                if (value.equals(entry.getValue())) {
                                    impl.setSelectedItem(new MapKeyWrapper(entry.getKey()));
                                    return;
                                }
                            }
                            impl.setSelectedItem(null);
                            return;
                        }

                        if (value instanceof Entity) {
                            selectedItem = new EntityWrapper((Entity) value);
                        } else
                            selectedItem = new ObjectWrapper(value);
                        impl.setSelectedItem(selectedItem);
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        if (updatingInstance)
                            return;
                        if (property.equals(metaPropertyPath.toString())) {
                            updatingInstance = true;
                            try {
                                Object selectedItem;
                                if (value instanceof Entity) {
                                    selectedItem = new EntityWrapper((Entity) value);
                                } else
                                    throw new UnsupportedOperationException(); // TODO
                                impl.setSelectedItem(selectedItem);
                            } finally {
                                updatingInstance = false;
                            }
                        }
                    }
                }
        );
    }

    @Override
    public String getCaption() {
        return null;
    }

    @Override
    public void setCaption(String caption) {
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public void setEditable(boolean editable) {
    }

    private interface Wrapper<T> {
        public T getValue();
    }

    private class EntityWrapper implements Wrapper<Entity> {

        private Entity entity;

        private EntityWrapper(Entity entity) {
            this.entity = entity;
        }

        @Override
        public Entity getValue() {
            return entity;
        }

        @Override
        public String toString() {
            if (entity == null)
                return "";
            if (captionMode.equals(CaptionMode.PROPERTY) && !StringUtils.isBlank(captionProperty))
                return entity.getValueEx(captionProperty);
            else
                return entity.getInstanceName();
        }
    }

    private class MapKeyWrapper implements Wrapper<Object> {

        private String key;

        private MapKeyWrapper(String key) {
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
    }

    private class ObjectWrapper implements Wrapper<Object> {

        private Object obj;

        private ObjectWrapper(Object obj) {
            this.obj = obj;
        }

        @Override
        public Object getValue() {
            return obj;
        }

        @Override
        public String toString() {
            if (obj == null)
                return "";
            return obj.toString();
        }
    }
}
