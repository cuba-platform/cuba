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
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.QueryUtils;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.executors.impl.DesktopBackgroundWorker;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.vcl.SearchAutoCompleteSupport;
import com.haulmont.cuba.desktop.sys.vcl.SearchComboBox;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.SearchField;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class DesktopSearchField extends DesktopAbstractOptionsField<JComponent> implements SearchField {

    protected static final FilterMode DEFAULT_FILTER_MODE = FilterMode.CONTAINS;

    protected BasicEventList<Object> items = new BasicEventList<>();
    protected SearchAutoCompleteSupport<Object> autoComplete;

    protected boolean resetValueState = false;
    protected boolean enterHandling = false;
    protected boolean popupItemSelectionHandling = false;
    protected boolean settingValue;
    protected boolean disableActionListener = false;

    protected Mode mode = Mode.CASE_SENSITIVE;
    protected boolean escapeValueForLike = false;

    protected Object nullOption;

    protected SearchComboBox comboBox;

    protected JTextField textField;
    protected JPanel composition;

    protected DefaultValueFormatter valueFormatter;

    protected int minSearchStringLength = 0;

    protected boolean nullOptionVisible = true;

    protected Frame.NotificationType defaultNotificationType = Frame.NotificationType.TRAY;

    protected Color searchEditBgColor = (Color) UIManager.get("cubaSearchEditBackground");

    protected SearchField.SearchNotifications searchNotifications = new SearchField.SearchNotifications() {
        @Override
        public void notFoundSuggestions(String filterString) {
            String message = messages.formatMessage("com.haulmont.cuba.gui", "searchSelect.notFound", filterString);
            App.getInstance().getMainFrame().showNotification(message, defaultNotificationType);
        }

        @Override
        public void needMinSearchStringLength(String filterString, int minSearchStringLength) {
            String message = messages.formatMessage(
                    "com.haulmont.cuba.gui", "searchSelect.minimumLengthOfFilter", minSearchStringLength);
            App.getInstance().getMainFrame().showNotification(message, defaultNotificationType);
        }
    };
    protected String inputPrompt;

    // just stub
    protected OptionIconProvider optionIconProvider;
    // just stub
    protected OptionsStyleProvider optionsStyleProvider;
    // just stub
    protected FilterPredicate filterPredicate;

    public DesktopSearchField() {
        composition = new JPanel();
        composition.setLayout(new BorderLayout());
        composition.setFocusable(false);

        comboBox = new SearchComboBox() {
            @Override
            public void setPopupVisible(boolean v) {
                if (!items.isEmpty()) {
                    super.setPopupVisible(v);
                } else if (!v) {
                    super.setPopupVisible(false);
                }
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (SearchAutoCompleteSupport.SEARCH_ENTER_COMMAND.equals(e.getActionCommand())) {
                    enterHandling = true;
                }

                super.actionPerformed(e);
            }
        };
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (settingValue || disableActionListener)
                    return;

                if ("comboBoxEdited".equals(e.getActionCommand())) {
                    Object selectedItem = comboBox.getSelectedItem();

                    if (popupItemSelectionHandling) {
                        if (selectedItem instanceof ValueWrapper) {
                            Object selectedValue = ((ValueWrapper) selectedItem).getValue();
                            setValue(selectedValue);
                            updateOptionsDsItem();
                        } else if (selectedItem instanceof String) {
                            handleSearch((String) selectedItem);
                        }
                        popupItemSelectionHandling = false;
                    } else if (enterHandling) {
                        if (selectedItem instanceof String) {
                            boolean found = false;
                            String newFilter = (String) selectedItem;
                            if (prevValue != null) {
                                if (Objects.equals(getDisplayString((Entity) prevValue), newFilter)) {
                                    found = true;
                                }
                            }
                            if (!found) {
                                handleSearch(newFilter);
                            } else {
                                updateComponent(prevValue);
                                clearSearchVariants();
                            }
                        } else {
                            // Disable variants after select
                            clearSearchVariants();
                        }
                        enterHandling = false;
                    }
                }

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateEditState();
                    }
                });
            }
        });

        Component editorComponent = comboBox.getEditor().getEditorComponent();
        editorComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateEditState();
                    }
                });
            }
        });

        comboBox.setEditable(true);
        comboBox.setPrototypeDisplayValue("AAAAAAAAAAAA");
        autoComplete = SearchAutoCompleteSupport.install(comboBox, items);
        autoComplete.setFilterEnabled(false);

        for (int i = 0; i < comboBox.getComponentCount(); i++) {
            java.awt.Component component = comboBox.getComponent(i);
            component.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    clearSearchVariants();
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
                            if (!enterHandling) {
                                Object selectedItem = comboBox.getSelectedItem();
                                if (selectedItem instanceof ValueWrapper) {
                                    Object selectedValue = ((ValueWrapper) selectedItem).getValue();
                                    setValue(selectedValue);
                                    updateOptionsDsItem();
                                } else if (selectedItem instanceof String) {
                                    handleSearch((String) selectedItem);
                                }
                            } else {
                                popupItemSelectionHandling = true;
                            }

                            updateMissingValueState();
                        }
                    }

                    @Override
                    public void popupMenuCanceled(PopupMenuEvent e) {
                        clearSearchVariants();
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
    }

    protected void updateEditState() {
        Component editorComponent = comboBox.getEditor().getEditorComponent();
        boolean value = required
                && isEditableWithParent()
                && isEnabledWithParent()
                && editorComponent instanceof JTextComponent
                && StringUtils.isEmpty(((JTextComponent) editorComponent).getText());
        if (value) {
            comboBox.setBackground(requiredBgColor);
        } else {
            comboBox.setBackground(defaultBgColor);

            if (editable && enabled) {
                if (editorComponent instanceof JTextComponent) {
                    String inputText = StringUtils.trimToNull(((JTextComponent) editorComponent).getText());

                    if (prevValue == null) {
                        String nullOptionText = null;
                        if (nullOption != null) {
                            nullOptionText = String.valueOf(nullOption);
                        }

                        if (StringUtils.isNotEmpty(inputText) && nullOption == null
                                || !Objects.equals(nullOptionText, inputText)) {
                            comboBox.setBackground(searchEditBgColor);
                        }
                    } else {
                        String valueText = getDisplayString((Entity) prevValue);

                        if (!Objects.equals(inputText, valueText)) {
                            comboBox.setBackground(searchEditBgColor);
                        }
                    }
                }
            }
        }
    }

    protected void handleSearch(final String newFilter) {
        clearSearchVariants();

        String filterForDs = newFilter;
        if (mode == Mode.LOWER_CASE) {
            filterForDs = StringUtils.lowerCase(newFilter);
        } else if (mode == Mode.UPPER_CASE) {
            filterForDs = StringUtils.upperCase(newFilter);
        }

        if (escapeValueForLike && StringUtils.isNotEmpty(filterForDs)) {
            filterForDs = QueryUtils.escapeForLike(filterForDs);
        }

        if (!isRequired() && StringUtils.isEmpty(filterForDs)) {
            if (optionsDatasource.getState() == Datasource.State.VALID) {
                optionsDatasource.clear();
            }

            setValue(null);
            updateOptionsDsItem();
            return;
        }

        if (StringUtils.length(filterForDs) >= minSearchStringLength) {
            optionsDatasource.refresh(
                    Collections.singletonMap(SearchField.SEARCH_STRING_PARAM, (Object) filterForDs));

            if (optionsDatasource.getState() == Datasource.State.VALID) {
                if (optionsDatasource.size() == 0) {
                    if (searchNotifications != null)
                        searchNotifications.notFoundSuggestions(newFilter);
                } else if (optionsDatasource.size() == 1) {
                    setValue(optionsDatasource.getItems().iterator().next());
                    updateOptionsDsItem();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            updateComponent(getValue());
                        }
                    });
                } else {
                    initSearchVariants();
                    comboBox.showSearchPopup();
                }
            }
        } else {
            if (optionsDatasource.getState() == Datasource.State.VALID) {
                optionsDatasource.clear();
            }

            if (searchNotifications != null && StringUtils.length(filterForDs) > 0) {
                searchNotifications.needMinSearchStringLength(newFilter, minSearchStringLength);
            }
        }
    }

    protected void clearSearchVariants() {
        items.clear();
    }

    @SuppressWarnings("unchecked")
    protected void updateOptionsDsItem() {
        if (optionsDatasource != null) {
            updatingInstance = true;
            if (optionsDatasource.getState() == Datasource.State.VALID) {
                if (!Objects.equals(getValue(), optionsDatasource.getItem())) {
                    optionsDatasource.setItem((Entity) getValue());
                }
            }
            updatingInstance = false;
        }
    }

    protected void checkSelectedValue() {
        if (!resetValueState) {
            resetValueState = true;
            Object selectedItem = comboBox.getSelectedItem();
            if (selectedItem instanceof String || selectedItem == null) {
                updateComponent(prevValue);
            }

            resetValueState = false;
            updateEditState();
        }
    }

    protected void initSearchVariants() {
        items.clear();

        if (!isRequired()) {
            items.add(new ObjectWrapper(null));
        }

        if (optionsDatasource != null) {
            for (Object id : optionsDatasource.getItemIds()) {
                items.add(new EntityWrapper(optionsDatasource.getItem(id)));
            }
        }
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
        return false;
    }

    @Override
    public void setNewOptionAllowed(boolean newOptionAllowed) {
    }

    @Override
    public boolean isTextInputAllowed() {
        return true;
    }

    @Override
    public void setTextInputAllowed(boolean textInputAllowed) {
        throw new UnsupportedOperationException("Option textInputAllowed is unsupported for Search field");
    }

    @Override
    public NewOptionHandler getNewOptionHandler() {
        return null;
    }

    @Override
    public void setNewOptionHandler(NewOptionHandler newOptionHandler) {
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
    public void setOptionsList(java.util.List optionsList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOptionsMap(Map<String, ?> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOptionsEnum(Class<? extends EnumClass> optionsEnum) {
        throw new UnsupportedOperationException();
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
        updateEditState();
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

        updateTextField();

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

    protected void updateTextRepresentation() {
        disableActionListener = true;
        try {
            Object value = comboBox.getSelectedItem();
            comboBox.getEditor().setItem(value);
        } finally {
            disableActionListener = false;
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
        DesktopBackgroundWorker.checkSwingUIAccess();

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
        if (value == null && nullOption != null) {
            value = new NullOption();
        }
        super.updateComponent(value);

        updateTextRepresentation();
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
    public void setMinSearchStringLength(int searchStringLength) {
        this.minSearchStringLength = searchStringLength;
    }

    @Override
    public int getMinSearchStringLength() {
        return minSearchStringLength;
    }

    @Override
    public void setSearchNotifications(SearchNotifications searchNotifications) {
        this.searchNotifications = searchNotifications;
    }

    @Override
    public SearchNotifications getSearchNotifications() {
        return searchNotifications;
    }

    @Override
    public Frame.NotificationType getDefaultNotificationType() {
        return defaultNotificationType;
    }

    @Override
    public void setDefaultNotificationType(com.haulmont.cuba.gui.components.Frame.NotificationType defaultNotificationType) {
        this.defaultNotificationType = defaultNotificationType;
    }

    @Override
    public Mode getMode() {
        return mode;
    }

    @Override
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public boolean isEscapeValueForLike() {
        return escapeValueForLike;
    }

    @Override
    public void setEscapeValueForLike(boolean escapeValueForLike) {
        this.escapeValueForLike = escapeValueForLike;
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
                    return String.valueOf(DesktopSearchField.this.nullOption);
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