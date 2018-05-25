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

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Enumeration;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.desktop.gui.executors.impl.DesktopBackgroundWorker;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.vcl.ExtendedComboBox;
import com.haulmont.cuba.desktop.sys.vcl.UserSelectionHandler;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.WeakCollectionChangeListener;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class DesktopLookupField extends DesktopAbstractOptionsField<JComponent> implements LookupField, UserSelectionHandler {

    protected static final FilterMode DEFAULT_FILTER_MODE = FilterMode.CONTAINS;

    protected BasicEventList<Object> items = new BasicEventList<>();
    protected AutoCompleteSupport<Object> autoComplete;
    protected String caption;
    protected NewOptionHandler newOptionHandler;

    protected boolean optionsInitialized;
    protected boolean resetValueState = false;

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

    protected boolean nullOptionVisible = true;

    protected List<UserSelectionListener> userSelectionListeners = null; // lazy initialized list

    protected CollectionDatasource.CollectionChangeListener collectionChangeListener;

    // just stub
    protected OptionIconProvider optionIconProvider;
    // just stub
    protected OptionsStyleProvider optionsStyleProvider;
    // just stub
    private FilterPredicate filterPredicate;

    public DesktopLookupField() {
        composition = new JPanel();
        composition.setLayout(new BorderLayout());
        composition.setFocusable(false);

        comboBox = new ExtendedComboBox() {
            @Override
            public void flushValue() {
                super.flushValue();

                flushSelectedValue();
            }
        };
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

                            fireUserSelectionListeners();
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

    protected void fireUserSelectionListeners() {
        if (userSelectionListeners != null) {
            for (UserSelectionListener listener : userSelectionListeners) {
                listener.userSelectionApplied(this);
            }
        }
    }

    protected void initClearShortcut() {
        JComponent editor = (JComponent) comboBox.getEditor().getEditorComponent();
        KeyStroke clearKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.SHIFT_DOWN_MASK, false);
        editor.getInputMap(JComponent.WHEN_FOCUSED).put(clearKeyStroke, "clearShortcut");
        editor.getActionMap().put("clearShortcut", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isRequired() && isEditable() && isEnabled()) {
                    setValue(null);

                    fireUserSelectionListeners();
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
                if (!Objects.equals(getValue(), optionsDatasource.getItem()))
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

    protected void flushSelectedValue() {
        if (!resetValueState) {
            resetValueState = true;
            Object selectedItem = comboBox.getEditor().getItem();

            if (!(selectedItem instanceof ValueWrapper)) {
                if (selectedItem instanceof String && newOptionAllowed && newOptionHandler != null) {
                    restorePreviousItemText();
                    newOptionHandler.addNewOption((String) selectedItem);
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

        if (!isRequired() && nullOption == null && nullOptionVisible) {
            items.add(new ObjectWrapper(null));
        }

        if (optionsDatasource != null) {
            if (!(optionsDatasource.getState() == Datasource.State.VALID)) {
                optionsDatasource.refresh();
            }
            for (Object id : optionsDatasource.getItemIds()) {
                items.add(new EntityWrapper(optionsDatasource.getItem(id)));
            }

            collectionChangeListener = e -> {
                items.clear();
                for (Entity item : optionsDatasource.getItems()) {
                    items.add(new EntityWrapper(item));
                }
            };
            optionsDatasource.addCollectionChangeListener(new WeakCollectionChangeListener(optionsDatasource, collectionChangeListener));
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
    public int getPageLength() {
        return 0;
    }

    @Override
    public void setPageLength(int pageLength) {
        // do nothing
    }

    @Override
    public void setNullOptionVisible(boolean nullOptionVisible) {
        this.nullOptionVisible = nullOptionVisible;
    }

    @Override
    public boolean isNullOptionVisible() {
        return nullOptionVisible;
    }

    public void setOptionIconProvider(OptionIconProvider<?> optionIconProvider) {
        this.optionIconProvider = optionIconProvider;
    }

    @Override
    public <T> void setOptionIconProvider(Class<T> optionClass, OptionIconProvider<T> optionIconProvider) {
        this.optionIconProvider = optionIconProvider;
    }

    public OptionIconProvider<?> getOptionIconProvider() {
        return optionIconProvider;
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
    protected void setCaptionToComponent(String caption) {
        super.setCaptionToComponent(caption);

        requestContainerUpdate();
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
    public void setOptionsMap(Map<String, ?> map) {
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
        if (!Objects.equals(this.getDescription(), description)) {
            JComponent editorComponent = (JComponent) comboBox.getEditor().getEditorComponent();

            editorComponent.setToolTipText(description);
            DesktopToolTipManager.getInstance().registerTooltip(editorComponent);

            requestContainerUpdate();
        }
    }

    @Override
    public void updateMissingValueState() {
        Component editorComponent = comboBox.getEditor().getEditorComponent();
        boolean value = required && isEditableWithParent() && isEnabledWithParent() && editorComponent instanceof JTextComponent
                && StringUtils.isEmpty(((JTextComponent) editorComponent).getText());
        decorateMissingValue(comboBox, value);
    }

    @Override
    protected void setEditableToComponent(boolean editable) {
        if (!editable) {
            composition.remove(comboBox);
            composition.add(textField, BorderLayout.CENTER);
            impl = textField;

            updateTextField();
        } else {
            composition.remove(textField);
            composition.add(comboBox, BorderLayout.CENTER);

            impl = comboBox;
        }

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
        if (impl == textField) {
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
        DesktopBackgroundWorker.checkSwingUIAccess();

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
        if (!Objects.equals(oldValue, newValue)) {
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
        if (optionsDatasource != null && StringUtils.isNotEmpty(optionsDatasource.getId())) {
            return getClass().getSimpleName() + "_" + optionsDatasource.getId();
        }

        return getClass().getSimpleName();
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;

        if (captionProperty != null) {
            setCaptionMode(CaptionMode.PROPERTY);
        } else {
            setCaptionMode(CaptionMode.ITEM);
        }
    }

    @Override
    public void addUserSelectionListener(UserSelectionListener listener) {
        Preconditions.checkNotNullArgument(listener);

        if (userSelectionListeners == null) {
            userSelectionListeners = new LinkedList<>();
        }
        if (!userSelectionListeners.contains(listener)) {
            userSelectionListeners.add(listener);
        }
    }

    @Override
    public void removeUserSelectionListener(UserSelectionListener listener) {
        if (userSelectionListeners != null) {
            userSelectionListeners.remove(listener);
        }
    }

    @Override
    protected String getDisplayString(Entity entity) {
        // This code was copied from superclass
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

    @Override
    public void setLookupSelectHandler(Runnable selectHandler) {
        // do nothing
    }

    @Override
    public Collection getLookupSelectedItems() {
        return Collections.singleton(getValue());
    }

    @Override
    public void commit() {
        // do nothing
    }

    @Override
    public void discard() {
        // do nothing
    }

    @Override
    public boolean isBuffered() {
        // do nothing
        return false;
    }

    @Override
    public void setBuffered(boolean buffered) {
        // do nothing
    }

    @Override
    public boolean isModified() {
        // do nothing
        return false;
    }

    // just stub
    @Override
    public void setOptionsStyleProvider(OptionsStyleProvider optionsStyleProvider) {
        this.optionsStyleProvider = optionsStyleProvider;
    }

    // just stub
    @Override
    public OptionsStyleProvider getOptionsStyleProvider() {
        return optionsStyleProvider;
    }

    // just stub
    @Override
    public void setFilterPredicate(FilterPredicate filterPredicate) {
        this.filterPredicate = filterPredicate;
    }

    // just stub
    @Override
    public FilterPredicate getFilterPredicate() {
        return filterPredicate;
    }

    protected class NullOption extends EntityWrapper {
        public NullOption() {
            //noinspection IncorrectCreateEntity
            super(new BaseUuidEntity() {
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