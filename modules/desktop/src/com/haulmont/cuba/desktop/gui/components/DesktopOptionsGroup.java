/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.ObjectUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopOptionsGroup
        extends DesktopAbstractOptionsField<JPanel>
        implements OptionsGroup
{
    private boolean multiselect;
    private boolean optionsInitialized;
    private Map<ValueWrapper, JToggleButton> items = new HashMap<ValueWrapper, JToggleButton>();
    private ButtonGroup buttonGroup;

    private Object prevValue = null;
    private Orientation orientation = Orientation.VERTICAL;
    private MigLayout layout;

    public DesktopOptionsGroup() {
        layout = new MigLayout();
        impl = new JPanel(layout);
        updateLayout();
    }

    private void updateLayout() {
        layout.setLayoutConstraints(orientation == Orientation.VERTICAL ? "flowy" : "flowx");
    }

    @Override
    public boolean isMultiSelect() {
        return multiselect;
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        if (this.multiselect != multiselect
                && (optionsDatasource != null || optionsList != null || optionsMap != null))
            throw new IllegalStateException("Set multiselect before initializing options");

        this.multiselect = multiselect;
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        if (optionsInitialized)
            return;

        super.setOptionsDatasource(datasource);
        if (optionsDatasource != null) {
            if (!optionsDatasource.getState().equals(Datasource.State.VALID)) {
                optionsDatasource.refresh();
            }
            for (Object id : optionsDatasource.getItemIds()) {
                addItem(new EntityWrapper(optionsDatasource.getItem(id)));
            }

            optionsDatasource.addListener(
                    new CollectionDsListenerAdapter<Entity<Object>>() {
                        @Override
                        public void collectionChanged(CollectionDatasource ds, Operation operation) {
                            removeAllItems();
                            for (Object id : ds.getItemIds()) {
                                addItem(new EntityWrapper(ds.getItem(id)));
                            }
                            impl.revalidate();
                            impl.repaint();
                        }
                    }
            );

            if ((datasource!= null) && (datasource.getState() == Datasource.State.VALID))
                setValue(datasource.getItem());

            prevValue = getValue();
            optionsInitialized = true;
        }
    }

    @Override
    public void setOptionsList(List optionsList) {
        if (optionsInitialized)
            return;

        super.setOptionsList(optionsList);
        if (optionsList != null) {
            for (Object obj : optionsList) {
                addItem(new ObjectWrapper(obj));
            }

            if ((datasource!= null) && (datasource.getState() == Datasource.State.VALID))
                setValue(datasource.getItem());

            prevValue = getValue();
            optionsInitialized = true;
        }
    }

    @Override
    public void setOptionsMap(Map<String, Object> map) {
        if (optionsInitialized)
            return;

        super.setOptionsMap(map);
        if (optionsMap != null) {
            for (String key : optionsMap.keySet()) {
                addItem(new MapKeyWrapper(key));
            }

            if ((datasource!= null) && (datasource.getState() == Datasource.State.VALID))
                setValue(datasource.getItem());

            prevValue = getValue();
            optionsInitialized = true;
        }
    }

    private void addItem(final ValueWrapper item) {
        JToggleButton button;
        if (multiselect) {
            button = new JCheckBox(item.toString());
        } else {
            if (buttonGroup == null)
                buttonGroup = new ButtonGroup();
            button = new JRadioButton(item.toString());
            buttonGroup.add(button);
        }
        button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateValue(item);
                    }
                }
        );

        impl.add(button);
        items.put(item, button);
    }

    private void removeAllItems() {
        impl.removeAll();
        items.clear();
        buttonGroup = null;
    }

    private void updateValue(ValueWrapper selectedItem) {
        if (datasource != null && metaProperty != null) {
            updatingInstance = true;
            try {
                if (datasource.getItem() != null) {
                    InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(),
                            selectedItem == null ? null : selectedItem.getValue());
                }
            } finally {
                updatingInstance = false;
            }
        }
        Object newValue = getValue();
        if (!ObjectUtils.equals(prevValue, newValue))
            fireValueChanged(prevValue, newValue);
        prevValue = newValue;
    }

    @Override
    public <T> T getValue() {
        if (multiselect) {
            Set<Object> set = new HashSet<Object>();
            for (Map.Entry<ValueWrapper, JToggleButton> entry : items.entrySet()) {
                if (entry.getValue().isSelected()) {
                    set.add(entry.getKey().getValue());
                }
            }
            return (T) set;
        } else {
            return (T) wrapAsCollection(super.getValue());
        }
    }

    @Override
    public void setValue(Object value) {
        if (multiselect && value instanceof Collection) {
            for (Object v : ((Collection) value)) {
                for (Map.Entry<ValueWrapper, JToggleButton> entry : items.entrySet()) {
                    if (ObjectUtils.equals(entry.getKey().getValue(), v))
                        entry.getValue().setSelected(true);
                    else
                        entry.getValue().setSelected(false);
                }
            }
        } else {
            super.setValue(value);
        }

        Object newValue = getValue();
        if (!ObjectUtils.equals(prevValue, newValue))
            fireValueChanged(prevValue, newValue);
        prevValue = newValue;
    }

    protected <T> T wrapAsCollection(Object o) {
        if (isMultiSelect()) {
            if (o != null) {
                return (T) Collections.singleton(o);
            } else {
                return (T) Collections.emptySet();
            }
        } else {
            return (T) o;
        }
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
    protected Object getSelectedItem() {
        for (Map.Entry<ValueWrapper, JToggleButton> entry : items.entrySet()) {
            if (entry.getValue().isSelected())
                return entry.getKey();
        }
        return null;
    }

    @Override
    protected void setSelectedItem(Object item) {
        for (Map.Entry<ValueWrapper, JToggleButton> entry : items.entrySet()) {
            if (entry.getKey().equals(item))
                entry.getValue().setSelected(true);
            else
                entry.getValue().setSelected(false);
        }
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public void setEditable(boolean editable) {
    }

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        if (orientation == null) {
            throw new IllegalArgumentException("Orientation must not be null");
        }
        this.orientation = orientation;
        updateLayout();
    }
}
