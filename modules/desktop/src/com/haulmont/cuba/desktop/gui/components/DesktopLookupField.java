/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.haulmont.chile.core.datatypes.Enumeration;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.vcl.ExtendedComboBox;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopLookupField extends DesktopAbstractOptionsField<JComponent> implements LookupField {

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

    protected boolean disableActionListener = false;

    protected Object nullOption;

    protected ExtendedComboBox comboBox;
    protected JTextField textField;

    protected JPanel composition;

    protected DefaultValueFormatter valueFormatter;
    protected String inputPrompt;
    protected boolean textInputAllowed = true;

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

                    // update text representation based on entity properties
                    updateTextRepresentation();
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
                            // Only if really item changed
                            Object selectedItem = comboBox.getSelectedItem();
                            if (selectedItem instanceof ValueWrapper) {
                                Object selectedValue = ((ValueWrapper) selectedItem).getValue();
                                setValue(selectedValue);
                            } else if (selectedItem instanceof String && newOptionAllowed && newOptionHandler != null) {
                                restorePreviousItemText();
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
                        if (settingValue || disableActionListener)
                            return;
                        Object selectedItem = comboBox.getSelectedItem();
                        if (selectedItem instanceof String && newOptionAllowed && newOptionHandler != null) {
                            restorePreviousItemText();
                            newOptionHandler.addNewOption((String) selectedItem);
                        }

                        updateMissingValueState();
                    }
                }
        );

        setFilterMode(DEFAULT_FILTER_MODE);

        textField = new JTextField();
        textField.setEditable(false);
        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
        valueFormatter = new DefaultValueFormatter(sessionSource.getLocale());

        composition.add(comboBox, BorderLayout.CENTER);
        impl = comboBox;

        DesktopComponentsHelper.adjustSize(comboBox);
        DesktopComponentsHelper.adjustSize(textField);

        textField.setMinimumSize(new Dimension(comboBox.getMinimumSize().width, textField.getPreferredSize().height));

        initClearShortcut();
    }

    protected void initClearShortcut() {
        JComponent editor = (JComponent) comboBox.getEditor().getEditorComponent();
        KeyStroke clearKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.SHIFT_DOWN_MASK, false);
        editor.getInputMap(JComponent.WHEN_FOCUSED).put(clearKeyStroke, "clearShortcut");
        editor.getActionMap().put("clearShortcut", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isRequired()) {
                    setValue(null);
                }
            }
        });
    }

    protected void restorePreviousItemText() {
        disableActionListener = true;
        try {
            Object value = null;
            if (prevValue != null) {
                for (Object item : items) {
                    ValueWrapper wrapper = (ValueWrapper) item;
                    if (wrapper.getValue() == prevValue) {
                        value = wrapper;
                        break;
                    }
                }
            }

            if (value == null && nullOption != null)
                value = new NullOption();

            comboBox.getEditor().setItem(value);
        } finally {
            disableActionListener = false;
        }
    }

    protected void updateTextRepresentation() {
        disableActionListener = true;
        try {
            Object value = comboBox.getSelectedItem();
            comboBox.getEditor().setItem(value);
        } finally {
            disableActionListener = false;
        }
    }

    @SuppressWarnings("unchecked")
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

            if (!(selectedItem instanceof ValueWrapper)) {
                if (selectedItem instanceof String && newOptionAllowed && newOptionHandler != null) {
                    updateComponent(prevValue);
                } else if (selectedItem == null || !newOptionAllowed) {
                    if (isRequired()) {
                        updateComponent(prevValue);
                    } else {
                        updateComponent(nullOption);
                    }
                }
            }

            resetValueState = false;
        }
    }

    protected void initOptions() {
        if (optionsInitialized)
            return;

        items.clear();

        if (!isRequired() && nullOption == null) {
            items.add(new ObjectWrapper(null));
        }

        if (optionsDatasource != null) {
            if (!(optionsDatasource.getState() == Datasource.State.VALID)) {
                optionsDatasource.refresh();
            }
            for (Object id : optionsDatasource.getItemIds()) {
                items.add(new EntityWrapper(optionsDatasource.getItem(id)));
            }

            optionsDatasource.addCollectionChangeListener(e -> {
                items.clear();
                for (Entity item : optionsDatasource.getItems()) {
                    items.add(new EntityWrapper(item));
                }
            });
        } else if (optionsMap != null) {
            for (String key : optionsMap.keySet()) {
                items.add(new MapKeyWrapper(key));
            }
        } else if (optionsList != null) {
            for (Object obj : optionsList) {
                items.add(new ObjectWrapper(obj));
            }
        } else if (optionsEnum != null) {
            List options = Arrays.asList(optionsEnum.getEnumConstants());
            for (Object obj : options) {
                items.add(new ObjectWrapper(obj));
            }
        } else if (datasource != null && metaProperty != null && metaProperty.getRange().isEnum()) {
            @SuppressWarnings("unchecked")
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
        if (getValue() == null) {
            setValue(null);
        }
        optionsInitialized = false;
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
    public boolean isTextInputAllowed() {
        return textInputAllowed;
    }

    @Override
    public void setTextInputAllowed(boolean textInputAllowed) {
        this.textInputAllowed = textInputAllowed;
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
    public String getInputPrompt() {
        return inputPrompt;
    }

    @Override
    public void setInputPrompt(String inputPrompt) {
        this.inputPrompt = inputPrompt;
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
        if (!ObjectUtils.equals(this.caption, caption)) {
            this.caption = caption;

            requestContainerUpdate();
        }
    }

    @Override
    public void setOptionsList(List optionsList) {
        super.setOptionsList(optionsList);
        if (optionsInitialized) {
            optionsInitialized = false;

            initOptions();
        }
    }

    @Override
    public void setOptionsMap(Map<String, Object> map) {
        super.setOptionsMap(map);
        if (optionsInitialized) {
            optionsInitialized = false;

            initOptions();
        }
    }

    @Override
    public void setOptionsEnum(Class<? extends EnumClass> optionsEnum) {
        super.setOptionsEnum(optionsEnum);
        if (optionsInitialized) {
            optionsInitialized = false;

            initOptions();
        }
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
        // #PL-4040
        // CAUTION do not set editable to combobox
        this.editable = editable;

        updateMissingValueState();
        requestContainerUpdate();

        composition.revalidate();
        composition.repaint();
    }

    protected JComponent getInputComponent() {
        if (impl == comboBox) {
            return (JComponent) comboBox.getEditor().getEditorComponent();
        } else {
            return impl;
        }
    }

    protected void updateTextField() {
        if (metaProperty != null) {
            Object value = getValue();
            if (value == null && nullOption != null) {
                textField.setText(nullOption.toString());
            } else {
                valueFormatter.setMetaProperty(metaProperty);
                textField.setText(valueFormatter.formatValue(value));
            }
        } else {
            if (comboBox.getSelectedItem() != null) {
                textField.setText(comboBox.getSelectedItem().toString());
            } else if (nullOption != null) {
                textField.setText(nullOption.toString());
            } else {
                textField.setText("");
            }
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

    protected Object getValueFromOptions(Object value) {
        if (optionsDatasource != null && value instanceof Entity) {
            if (Datasource.State.INVALID == optionsDatasource.getState()) {
                optionsDatasource.refresh();
            }
            Object itemId = ((Entity) value).getId();
            if (optionsDatasource.containsItem(itemId)) {
                value = optionsDatasource.getItem(itemId);
            }
        }

        return value;
    }

    @Override
    public void setValue(Object value) {
        settingValue = true;
        try {
            if (value == nullOption) {
                value = null;
            }

            super.setValue(getValueFromOptions(value));
        } finally {
            settingValue = false;
        }

        comboBox.hidePopup();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue() {
        final Object value = super.getValue();
        return (T) getValueFromOptions(value);
    }

    @Override
    protected void fireChangeListeners(Object newValue) {
        Object oldValue = prevValue;
        prevValue = newValue;
        if (!ObjectUtils.equals(oldValue, newValue)) {
            updateOptionsDsItem();
            fireValueChanged(oldValue, newValue);
        }
    }

    @Override
    protected void updateComponent(Object value) {
        if (value == null && nullOption != null)
            value = new NullOption();
        super.updateComponent(value);
    }

    @Override
    public void updateEnabled() {
        super.updateEnabled();

        boolean resultEnabled = isEnabledWithParent();

        comboBox.setEnabled(resultEnabled);
        textField.setEnabled(resultEnabled);

        comboBox.setFocusable(resultEnabled);
        textField.setFocusable(resultEnabled);

        updateMissingValueState();
    }

    @Override
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }
        if (datasource != null && StringUtils.isNotEmpty(datasource.getId()) && metaPropertyPath != null) {
            return getClass().getSimpleName() + "_" + datasource.getId() + "_" + metaPropertyPath.toString();
        }
        if (optionsDatasource != null &&  StringUtils.isNotEmpty(optionsDatasource.getId())) {
            return getClass().getSimpleName() + "_" + optionsDatasource.getId();
        }

        return getClass().getSimpleName();
    }

    protected class NullOption extends EntityWrapper {
        public NullOption() {
            super(new AbstractNotPersistentEntity() {
                @Override
                public String getInstanceName() {
                    // NullOption class is used for any type of nullOption value
                    if (nullOption instanceof Instance) {
                        return InstanceUtils.getInstanceName((Instance) nullOption);
                    } else if (nullOption instanceof Enum) {
                        return messages.getMessage((Enum) nullOption);
                    }

                    if (nullOption == null) {
                        return "";
                    } else {
                        return nullOption.toString();
                    }
                }

                // Used for captionProperty of null entity
                @SuppressWarnings("unchecked")
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