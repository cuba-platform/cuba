/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.vcl.ExtendedComboBox;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.SearchField;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.synth.SynthComboBoxUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class DesktopSearchField extends DesktopAbstractOptionsField<JComponent> implements SearchField {

    protected static final FilterMode DEFAULT_FILTER_MODE = FilterMode.CONTAINS;

    protected BasicEventList<Object> items = new BasicEventList<>();
    protected AutoCompleteSupport<Object> autoComplete;
    protected String caption;

    protected boolean resetValueState = false;
    protected boolean enterHandling = false;
    protected boolean settingValue;

    protected boolean editable = true;
    protected boolean enabled = true;

    protected Object nullOption;

    protected SearchComboBox comboBox;

    protected JTextField textField;
    protected JPanel composition;

    protected DefaultValueFormatter valueFormatter;

    protected int minSearchStringLength = 0;

    protected Messages messages;

    protected IFrame.NotificationType defaultNotificationType = IFrame.NotificationType.TRAY;

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

    public DesktopSearchField() {
        messages = AppBeans.get(Messages.class);

        composition = new JPanel();
        composition.setLayout(new BorderLayout());
        composition.setFocusable(false);

        comboBox = new SearchComboBox();
        comboBox.setUI(new SynthComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("comboBoxEdited".equals(e.getActionCommand()) && enterHandling) {
                    Object item = comboBox.getSelectedItem();

                    if (item instanceof String) {
                        boolean found = false;
                        String newFilter = (String) item;
                        if (prevValue != null) {
                            if (StringUtils.equals(InstanceUtils.getInstanceName((Entity) prevValue), newFilter)) {
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
                }

                updateMissingValueState();

                enterHandling = false;
            }
        });
        comboBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateEditState();
                    }
                });
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getExtendedKeyCode() == KeyEvent.VK_ENTER && e.getModifiers() == 0) {
                    enterHandling = true;
                }
            }
        });

        comboBox.setButtonVisible(false);
        comboBox.setEditable(true);
        comboBox.setPrototypeDisplayValue("AAAAAAAAAAAA");
        autoComplete = AutoCompleteSupport.install(comboBox, items);

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
                            Object selectedItem = comboBox.getSelectedItem();
                            if (selectedItem instanceof ValueWrapper) {
                                Object selectedValue = ((ValueWrapper) selectedItem).getValue();
                                setValue(selectedValue);
                                updateOptionsDsItem();
                            } else if (selectedItem != null) {
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

        setFilterMode(DEFAULT_FILTER_MODE);

        textField = new JTextField();
        textField.setEditable(false);
        valueFormatter = new DefaultValueFormatter(AppBeans.get(UserSessionSource.class).getLocale());

        composition.add(comboBox, BorderLayout.CENTER);
        impl = comboBox;

        DesktopComponentsHelper.adjustSize(comboBox);
    }

    protected void updateEditState() {
        Component editorComponent = comboBox.getEditor().getEditorComponent();
        boolean value = required && editable && enabled && editorComponent instanceof JTextComponent
                && StringUtils.isEmpty(((JTextComponent) editorComponent).getText());
        if (value) {
            comboBox.setBackground(requiredBgColor);
        } else {
            comboBox.setBackground(defaultBgColor);

            if (editable && enabled) {
                if (editorComponent instanceof JTextComponent) {
                    String inputText = ((JTextComponent) editorComponent).getText();

                    if (prevValue == null) {
                        if (StringUtils.isNotEmpty(inputText)) {
                            comboBox.setBackground(searchEditBgColor);
                        }
                    } else {
                        String valueText = InstanceUtils.getInstanceName((Entity) prevValue);

                        if (!StringUtils.equals(inputText, valueText)) {
                            comboBox.setBackground(searchEditBgColor);
                        }
                    }
                }
            }
        }
    }

    protected void handleSearch(String newFilter) {
        clearSearchVariants();

        if (StringUtils.length(newFilter) >= minSearchStringLength) {
            optionsDatasource.refresh(
                    Collections.singletonMap(SearchField.SEARCH_STRING_PARAM, (Object) newFilter));
            if (optionsDatasource.getState() == Datasource.State.VALID && optionsDatasource.size() == 1) {
                setValue(optionsDatasource.getItems().iterator().next());
                updateOptionsDsItem();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateComponent(getValue());
                    }
                });
            }

            if (searchNotifications != null) {
                if (optionsDatasource.getState() == Datasource.State.VALID && optionsDatasource.size() == 0) {
                    searchNotifications.notFoundSuggestions(newFilter);
                }
            }

            if (optionsDatasource.getState() == Datasource.State.VALID && optionsDatasource.size() > 1) {
                initSearchVariants();
                comboBox.showSearchPopup();
            }
        } else {
            if (optionsDatasource.getState() == Datasource.State.VALID) {
                optionsDatasource.clear();
                if (!isRequired()) {
                    setValue(null);
                    updateOptionsDsItem();
                }
            }

            if (searchNotifications != null && StringUtils.length(newFilter) > 0) {
                searchNotifications.needMinSearchStringLength(newFilter, minSearchStringLength);
            }
        }
    }

    protected void clearSearchVariants() {
        items.clear();
    }

    protected void updateOptionsDsItem() {
        if (optionsDatasource != null) {
            updatingInstance = true;
            if (optionsDatasource.getState() == Datasource.State.VALID) {
                if (!ObjectUtils.equals(getValue(), optionsDatasource.getItem())) {
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
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public void setOptionsList(java.util.List optionsList) {
        super.setOptionsList(optionsList);
    }

    @Override
    public void setOptionsMap(Map<String, Object> map) {
        super.setOptionsMap(map);
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
        updateEditState();
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
            if (comboBox.getSelectedItem() != null) {
                textField.setText(comboBox.getSelectedItem().toString());
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
        if (value == null && nullOption != null) {
            value = new NullOption();
        }
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
    public IFrame.NotificationType getDefaultNotificationType() {
        return defaultNotificationType;
    }

    @Override
    public void setDefaultNotificationType(IFrame.NotificationType defaultNotificationType) {
        this.defaultNotificationType = defaultNotificationType;
    }

    protected class NullOption extends EntityWrapper {
        public NullOption() {
            super(new AbstractNotPersistentEntity() {
                @Override
                public String getInstanceName() {
                    return String.valueOf(DesktopSearchField.this.nullOption);
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

    protected class SearchComboBox extends ExtendedComboBox {
        @Override
        public void setPopupVisible(boolean v) {
            if (!items.isEmpty()) {
                super.setPopupVisible(v);
            } else if (!v) {
                super.setPopupVisible(false);
            }
        }

        public void showSearchPopup() {
            super.setPopupVisible(true);
        }
    }
}