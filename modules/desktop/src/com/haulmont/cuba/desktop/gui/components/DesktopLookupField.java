/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopLookupField
    extends DesktopAbstractOptionsField<JComboBox>
    implements LookupField
{
    private BasicEventList<Object> items = new BasicEventList<Object>();
    private boolean optionsInitialized;
    private AutoCompleteSupport<Object> autoComplete;
    private ValueWrapper prevValue;

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
                        ValueWrapper newValue = (ValueWrapper) impl.getSelectedItem();
                        if (newValue != prevValue) {
                            updateValue(newValue);
                            fireValueChanged(prevValue == null ? null : prevValue.getValue(), newValue == null ? null : newValue.getValue());
                            prevValue = newValue;
                        }
                    }
                }
        );

        setFilterMode(FilterMode.CONTAINS);

        DesktopComponentsHelper.adjustSize(impl);
    }

    private void updateValue(ValueWrapper selectedItem) {
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

        if (!isRequired()) {
            items.add(new ObjectWrapper(null));
        }

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
    public boolean isValid() {
        return true;
    }

    @Override
    public void validate() throws ValidationException {
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

    @Override
    protected Object getSelectedItem() {
        return impl.getSelectedItem();
    }

    @Override
    protected void setSelectedItem(Object item) {
        impl.setSelectedItem(item);
    }
}
