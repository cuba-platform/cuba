/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.haulmont.chile.core.datatypes.Enumeration;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.vcl.ExtendedComboBox;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopLookupField
        extends DesktopAbstractOptionsField<JComponent>
        implements LookupField {

    protected static final FilterMode DEFAULT_FILTER_MODE = FilterMode.CONTAINS;

    protected BasicEventList<Object> items = new BasicEventList<>();
    protected AutoCompleteSupport<Object> autoComplete;
    protected String caption;
    protected NewOptionHandler newOptionHandler;

    protected boolean optionsInitialized;
    protected boolean resetValueState = false;

    protected boolean editable = true;
    protected boolean newOptionAllowed;
    protected boolean settingValue;

    protected Object nullOption;

    protected ExtendedComboBox comboBox;
    protected JTextField textField;

    protected JPanel composition;

    protected DefaultValueFormatter valueFormatter;
    protected boolean enabled = true;

    public DesktopLookupField() {
        composition = new JPanel();
        composition.setLayout(new BorderLayout());
        composition.setFocusable(false);

        comboBox = new ExtendedComboBox();
        comboBox.setEditable(true);
        comboBox.setPrototypeDisplayValue("AAAAAAAAAAAA");
        autoComplete = AutoCompleteSupport.install(comboBox, items);

        for (int i = 0; i < comboBox.getComponentCount(); i++) {
            java.awt.Component component = comboBox.getComponent(i);
            component.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    initOptions();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    // Reset invalid value
                    checkSelectedValue();
                }
            });
        }
        // set value only on PopupMenu closing to avoid firing listeners on keyboard navigation
        comboBox.addPopupMenuListener(
                new PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                        comboBox.updatePopupWidth();
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                        if (!autoComplete.isEditableState()) {
                            // Only if realy item changed
                            Object selectedItem = comboBox.getSelectedItem();
                            if (selectedItem instanceof ValueWrapper) {
                                Object selectedValue = ((ValueWrapper) selectedItem).getValue();
                                setValue(selectedValue);
                                updateOptionsDsItem();
                            } else if (selectedItem instanceof String && newOptionAllowed && newOptionHandler != null) {
                                newOptionHandler.addNewOption((String) selectedItem);
                            } else if ((selectedItem != null) && !newOptionAllowed) {
                                updateComponent(prevValue);
                            }

                            updateMissingValueState();
                        }
                    }

                    @Override
                    public void popupMenuCanceled(PopupMenuEvent e) {
                    }
                }
        );
        comboBox.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (settingValue)
                            return;
                        Object selectedItem = comboBox.getSelectedItem();
                        if (selectedItem instanceof String && newOptionAllowed && newOptionHandler != null) {
                            newOptionHandler.addNewOption((String) selectedItem);
                        }
                    }
                }
        );

        setFilterMode(DEFAULT_FILTER_MODE);

        textField = new JTextField();
        textField.setEditable(false);
        valueFormatter = new DefaultValueFormatter(AppBeans.get(UserSessionSource.class).getLocale());

        composition.add(comboBox, BorderLayout.CENTER);
        impl = comboBox;

        DesktopComponentsHelper.adjustSize(comboBox);
    }

    protected void updateOptionsDsItem() {
        if (optionsDatasource != null) {
            updatingInstance = true;
            if (optionsDatasource.getState() == Datasource.State.VALID) {
                if (!ObjectUtils.equals(getValue(), optionsDatasource.getItem()))
                    optionsDatasource.setItem((Entity) getValue());
            }
            updatingInstance = false;
        }
    }

    protected void checkSelectedValue() {
        if (!resetValueState) {
            resetValueState = true;
            Object selectedItem = comboBox.getSelectedItem();
            if (selectedItem instanceof ValueWrapper) {
            } else if (selectedItem instanceof String && newOptionAllowed && newOptionHandler != null) {
            } else if (selectedItem == null || !newOptionAllowed) {
                if (isRequired())
                    updateComponent(prevValue);
                else
                    updateComponent(nullOption);
            }

            resetValueState = false;
        }
    }

    protected void initOptions() {
        if (optionsInitialized)
            return;

        items.clear();

        if (!isRequired()) {
            items.add(new ObjectWrapper(null));
        }

        if (optionsDatasource != null) {
            if (!(optionsDatasource.getState() == Datasource.State.VALID)) {
                optionsDatasource.refresh();
            }
            for (Object id : optionsDatasource.getItemIds()) {
                items.add(new EntityWrapper(optionsDatasource.getItem(id)));
            }

            optionsDatasource.addListener(
                    new CollectionDsListenerAdapter<Entity<Object>>() {
                        @Override
                        public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity<Object>> dsItems) {
                            items.clear();
                            for (Entity item : optionsDatasource.getItems()) {
                                items.add(new EntityWrapper(item));
                            }
                        }
                    }
            );
        } else if (optionsMap != null) {
            for (String key : optionsMap.keySet()) {
                items.add(new MapKeyWrapper(key));
            }
        } else if (optionsList != null) {
            for (Object obj : optionsList) {
                items.add(new ObjectWrapper(obj));
            }
        } else if (datasource != null && metaProperty != null && metaProperty.getRange().isEnum()) {
            Enumeration<Enum> enumeration = metaProperty.getRange().asEnumeration();
            for (Enum en : enumeration.getValues()) {
                items.add(new ObjectWrapper(en));
            }
        }

        optionsInitialized = true;
    }

    @Override
    public JComponent getComposition() {
        return composition;
    }

    @Override
    public Object getNullOption() {
        return nullOption;
    }

    @Override
    public void setNullOption(Object nullOption) {
        this.nullOption = nullOption;
        autoComplete.setFirstItem(new NullOption());
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
        return newOptionAllowed;
    }

    @Override
    public void setNewOptionAllowed(boolean newOptionAllowed) {
        this.newOptionAllowed = newOptionAllowed;
    }

    @Override
    public NewOptionHandler getNewOptionHandler() {
        return newOptionHandler;
    }

    @Override
    public void setNewOptionHandler(NewOptionHandler newOptionHandler) {
        this.newOptionHandler = newOptionHandler;
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
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public void setOptionsList(List optionsList) {
        super.setOptionsList(optionsList);
        optionsInitialized = false;
    }

    @Override
    public void setOptionsMap(Map<String, Object> map) {
        super.setOptionsMap(map);
        optionsInitialized = false;
    }

    @Override
    public String getDescription() {
        return ((JComponent) comboBox.getEditor().getEditorComponent()).getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        ((JComponent) comboBox.getEditor().getEditorComponent()).setToolTipText(description);
        DesktopToolTipManager.getInstance().registerTooltip((JComponent) comboBox.getEditor().getEditorComponent());
    }

    @Override
    public void updateMissingValueState() {
        Component editorComponent = comboBox.getEditor().getEditorComponent();
        boolean value = required && editable && enabled && editorComponent instanceof JTextComponent
                && StringUtils.isEmpty(((JTextComponent) editorComponent).getText());
        decorateMissingValue(comboBox, value);
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        if (this.editable && !editable) {
            composition.remove(comboBox);
            composition.add(textField, BorderLayout.CENTER);
            impl = textField;

            updateTextField();
        } else if (!this.editable && editable) {
            composition.remove(textField);
            composition.add(comboBox, BorderLayout.CENTER);

            impl = comboBox;
        }
        this.editable = editable;
        updateMissingValueState();
    }

    protected JComponent getInputComponent() {
        if (impl == comboBox) {
            return (JComponent) comboBox.getEditor().getEditorComponent();
        } else {
            return impl;
        }
    }

    private void updateTextField() {
        if (metaProperty != null) {
            valueFormatter.setMetaProperty(metaProperty);
            textField.setText(valueFormatter.formatValue(getValue()));
        } else {
            if (comboBox.getSelectedItem() != null)
                textField.setText(comboBox.getSelectedItem().toString());
            else
                textField.setText("");
        }
    }

    @Override
    protected Object getSelectedItem() {
        return comboBox.getSelectedItem();
    }

    @Override
    protected void setSelectedItem(Object item) {
        comboBox.setSelectedItem(item);
        if (!editable) {
            updateTextField();
        }
        updateMissingValueState();
    }

    @Override
    public void setValue(Object value) {
        settingValue = true;
        try {
            if (value == nullOption) {
                value = null;
            }

            super.setValue(value);
        } finally {
            settingValue = false;
        }
    }

    @Override
    protected void updateComponent(Object value) {
        if (value == null && nullOption != null)
            value = new NullOption();
        super.updateComponent(value);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        comboBox.setEnabled(enabled);
        textField.setEnabled(enabled);

        comboBox.setFocusable(enabled);
        textField.setFocusable(enabled);

        updateMissingValueState();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    protected class NullOption extends EntityWrapper {
        public NullOption() {
            super(new AbstractNotPersistentEntity() {
                @Override
                public String getInstanceName() {
                    return String.valueOf(DesktopLookupField.this.nullOption);
                }

                // Used for captionProperty of null entity
                @Override
                public <T> T getValue(String s) {
                    return (T) getInstanceName();
                }
            });
        }

        @Override
        public Entity getValue() {
            return null;
        }
    }
}