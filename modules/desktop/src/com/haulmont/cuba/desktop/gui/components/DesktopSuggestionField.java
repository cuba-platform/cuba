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
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.desktop.DesktopConfig;
import com.haulmont.cuba.desktop.gui.executors.impl.DesktopBackgroundWorker;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.vcl.SearchAutoCompleteSupport;
import com.haulmont.cuba.desktop.sys.vcl.SearchComboBox;
import com.haulmont.cuba.gui.components.SuggestionField;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_TAB;

public class DesktopSuggestionField extends DesktopAbstractOptionsField<JComponent> implements SuggestionField {

    private final Logger log = LoggerFactory.getLogger(DesktopSuggestionField.class);

    private AutomaticCompletionSupport automaticCompletion;

    protected BasicEventList<Object> items = new BasicEventList<>();
    protected MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);

    protected boolean resetValueState = false;
    protected boolean enterHandling = false;
    protected boolean popupItemSelectionHandling = false;
    protected boolean settingValue;
    protected boolean disableActionListener = false;
    protected boolean sameValueOnEnterAction = false;
    protected boolean clearInputOnEnterAction = false;

    protected Object nullOption;

    protected SearchComboBox comboBox;

    protected JTextField textField;
    protected JPanel composition;

    protected DefaultValueFormatter valueFormatter;

    protected SwingWorker asyncSearchWorker;
    protected String lastSearchString;

    protected int minSearchStringLength = 0;

    protected int asyncSearchDelayMs;

    protected SearchExecutor<?> searchExecutor;
    protected EnterActionHandler enterActionHandler;

    protected Color searchEditBgColor = (Color) UIManager.get("cubaSearchEditBackground");

    protected String currentSearchComponentText;
    private ArrowDownActionHandler arrowDownActionHandler;
    protected int suggestionsLimit = 10;

    // just stub
    protected String inputPrompt;
    protected String popupWidth;
    // just stub
    protected OptionsStyleProvider optionsStyleProvider;

    public DesktopSuggestionField() {
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
                if (beforeComboEventActionPerformed(e)) {
                    super.actionPerformed(e);
                    afterComboBoxEventActionPerformed(e);
                }
            }
        };

        comboBox.addActionListener(e -> {
            if (settingValue || disableActionListener) {
                return;
            }

            if ("comboBoxEdited".equals(e.getActionCommand())) {
                Object selectedItem = comboBox.getSelectedItem();

                if (popupItemSelectionHandling) {
                    if (selectedItem instanceof ValueWrapper) {
                        Object selectedValue = ((ValueWrapper) selectedItem).getValue();
                        setValue(selectedValue);
                    }
                } else if (enterHandling) {
                    if (selectedItem instanceof String) {
                        boolean found = false;
                        String newFilter = (String) selectedItem;
                        if (prevValue != null) {
                            if (Objects.equals(getDisplayString(prevValue), newFilter)) {
                                found = true;
                            }
                        }

                        final boolean searchStringEqualsToCurrentValue = found;
                        // we need to do it later
                        // unable to change current text from ActionListener
                        SwingUtilities.invokeLater(() -> {
                            updateComponent(prevValue);

                            if (!searchStringEqualsToCurrentValue) {
                                handleOnEnterAction(((String) selectedItem));
                            }
                        });
                    } else if (currentSearchComponentText != null) {
                        // Disable variants after select
                        final String enterActionString = currentSearchComponentText;
                        SwingUtilities.invokeLater(() -> {
                            updateComponent(prevValue);

                            handleOnEnterAction(enterActionString);
                        });

                        currentSearchComponentText = null;
                    }
                }

                clearSearchVariants();

                popupItemSelectionHandling = false;
                enterHandling = false;
            }

            SwingUtilities.invokeLater(this::updateEditState);
        });

        Component editorComponent = comboBox.getEditor().getEditorComponent();
        editorComponent.setFocusTraversalKeysEnabled(false);
        editorComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    updateEditState();

                    if (e.getKeyChar() != '\n') {
                        handleSearchInput();
                    }
                });
            }

            @Override
            public void keyPressed(KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    switch (e.getKeyCode()) {
                        case VK_DOWN:
                            onArrowDownKeyEvent(e);
                            break;
                        case VK_TAB:
                            onTabKeyEvent(e);
                            break;
                    }
                });
            }
        });

        comboBox.setEditable(true);
        comboBox.setPrototypeDisplayValue("AAAAAAAAAAAA");

        automaticCompletion = installAutomaticCompletionSupport();

        for (int i = 0; i < comboBox.getComponentCount(); i++) {
            Component component = comboBox.getComponent(i);
            component.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    clearSearchVariants();
                    // Reset invalid value

                    checkSelectedValue();
                }
            });
        }

        final JTextField searchEditorComponent = getComboBoxEditorField();
        searchEditorComponent.addActionListener(e ->
                currentSearchComponentText = searchEditorComponent.getText());

        // set value only on PopupMenu closing to avoid firing listeners on keyboard navigation
        comboBox.addPopupMenuListener(
                new PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                        comboBox.updatePopupWidth();
                        if (comboBox.getItemCount() > 0) {
                            comboBox.resetScrolling();
                        }
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                        if (!isAutomaticCompletionEditableState()) {
                            popupItemSelectionHandling = comboBox.getSelectedIndex() >= 0;

                            // Only if really item changed
                            if (!enterHandling) {
                                Object selectedItem = comboBox.getSelectedItem();
                                if (selectedItem instanceof ValueWrapper) {
                                    Object selectedValue = ((ValueWrapper) selectedItem).getValue();
                                    setValue(selectedValue);

                                    clearSearchVariants();
                                }
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

        textField = new JTextField();
        textField.setEditable(false);
        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
        valueFormatter = new DefaultValueFormatter(sessionSource.getLocale());

        composition.add(comboBox, BorderLayout.CENTER);
        impl = comboBox;

        DesktopComponentsHelper.adjustSize(comboBox);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        asyncSearchDelayMs = configuration.getConfig(ClientConfig.class).getSuggestionFieldAsyncSearchDelayMs();
    }

    protected boolean beforeComboEventActionPerformed(ActionEvent event) {
        if (SearchAutoCompleteSupport.SEARCH_ENTER_COMMAND.equals(event.getActionCommand())) {
            if (!sameValueOnEnterAction) {
                if (clearInputOnEnterAction) {
                    setValue(nullOption);
                } else {
                    enterHandling = true;
                }
            }
            sameValueOnEnterAction = false;
        } else {
            sameValueOnEnterAction = StringUtils.equals(event.getActionCommand(), getDisplayString(prevValue));
            clearInputOnEnterAction = StringUtils.isEmpty(event.getActionCommand());
        }
        return true;
    }

    protected void afterComboBoxEventActionPerformed(ActionEvent event) {
    }

    protected void onArrowDownKeyEvent(KeyEvent event) {
        if (arrowDownActionHandler != null && !comboBox.isPopupVisible()) {
            arrowDownActionHandler.onArrowDownKeyPressed(getComboBoxEditorField().getText());
        }
    }

    protected void onTabKeyEvent(KeyEvent event) {
        Component source = event.getComponent();
        JTextField editor = getComboBoxEditorField();
        int selected = comboBox.getSelectedIndex();
        String input = editor.getText();

        if (StringUtils.isNotBlank(input) && selected < 0) {
            comboBox.actionPerformed(new ActionEvent(event.getSource(), event.getID(), input));
            comboBox.actionPerformed(new ActionEvent(event.getSource(), event.getID(), SearchAutoCompleteSupport.SEARCH_ENTER_COMMAND));
            source.transferFocus();
        } else if (event.isShiftDown()) {
            source.transferFocusBackward();
        } else {
            source.transferFocus();
        }
    }

    protected AutomaticCompletionSupport installAutomaticCompletionSupport() {
        final SearchAutoCompleteSupport<Object> support = installSearchAutoCompleteSupport();
        setupSearchAutoCompleteSupport(support);
        return new AutomaticCompletionSupport() {
            @Override public boolean isEditableState() {
                return support.isEditableState();
            }
        };
    }

    protected SearchAutoCompleteSupport<Object> installSearchAutoCompleteSupport() {
        return SearchAutoCompleteSupport.install(comboBox, items);
    }

    protected void setupSearchAutoCompleteSupport(SearchAutoCompleteSupport<Object> instance) {
        instance.setFilterEnabled(false);
    }

    protected boolean isAutomaticCompletionEditableState() {
        return automaticCompletion.isEditableState();
    }

    protected void handleOnEnterAction(String currentSearchString) {
        log.debug("On Enter '{}'", currentSearchString);

        if (asyncSearchWorker != null) {
            asyncSearchWorker.cancel(true);
            asyncSearchWorker = null;
        }

        if (enterActionHandler != null) {
            enterActionHandler.onEnterKeyPressed(currentSearchString);
        }
    }

    protected void handleSearchInput() {
        JTextField searchEditor = getComboBoxEditorField();
        String currentSearchString = StringUtils.trimToEmpty(searchEditor.getText());
        if (!Objects.equals(currentSearchString, lastSearchString)) {
            lastSearchString = currentSearchString;

            if (searchExecutor != null) {
                if (asyncSearchWorker != null) {
                    log.debug("Cancel previous search");

                    asyncSearchWorker.cancel(true);
                }

                if (currentSearchString.length() >= minSearchStringLength) {
                    Map<String, Object> params = null;
                    if (searchExecutor instanceof ParametrizedSearchExecutor) {
                        //noinspection unchecked
                        params = ((ParametrizedSearchExecutor) searchExecutor).getParams();
                    }
                    asyncSearchWorker = createSearchWorker(currentSearchString, params);
                    asyncSearchWorker.execute();
                }
            }
        }
    }

    protected SwingWorker<List<?>, Void> createSearchWorker(final String currentSearchString,
                                                                 final Map<String, Object> params) {
        final SearchExecutor<?> currentSearchExecutor = this.searchExecutor;
        final int currentAsyncSearchTimeoutMs = this.asyncSearchDelayMs;

        return new SwingWorker<List<?>, Void>() {
            @Override
            protected List<?> doInBackground() throws Exception {
                Thread.currentThread().setName("ReactiveSearchField_AsyncThread");

                Thread.sleep(currentAsyncSearchTimeoutMs);

                List<?> result;
                try {
                    result = asyncSearch(currentSearchExecutor, currentSearchString, params);
                } catch (RuntimeException e) {
                    log.error("Error in async search thread", e);

                    result = Collections.emptyList();
                }

                return result;
            }

            @Override
            protected void done() {
                super.done();

                List<?> searchResultItems;
                try {
                    searchResultItems = get();
                } catch (InterruptedException | ExecutionException | CancellationException e) {
                    return;
                }

                log.debug("Search results for '{}'", currentSearchString);

                handleSearchResults(searchResultItems);
            }
        };
    }

    // Called on background thread
    protected List<?> asyncSearch(SearchExecutor<?> searchExecutor, String searchString, Map<String, Object> params)
            throws Exception {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        log.debug("Search '{}'", searchString);

        List<?> searchResultItems;
        if (searchExecutor instanceof ParametrizedSearchExecutor) {
            //noinspection unchecked
            ParametrizedSearchExecutor<?> pSearchExecutor = (ParametrizedSearchExecutor<?>) searchExecutor;
            searchResultItems = new ArrayList<>(pSearchExecutor.search(searchString, params));
        } else {
            searchResultItems = new ArrayList<>(searchExecutor.search(searchString, Collections.emptyMap()));
        }

        return searchResultItems;
    }

    protected void handleSearchResults(List<?> searchResultItems) {
        if (isVisible() && isEnabled() && isEditable()) {
            items.clear();
            List<SearchObjectWrapper> wrappers = new ArrayList<>();
            for (int i = 0; i < searchResultItems.size() && i < suggestionsLimit; i++) {
                Object item = searchResultItems.get(i);
                wrappers.add(new SearchObjectWrapper(item));
            }
            items.addAll(wrappers);

            if (items.isEmpty()) {
                comboBox.hideSearchPopup();
            } else {
                comboBox.showSearchPopup();
            }
        }
    }

    protected void updateEditState() {
        Component editorComponent = comboBox.getEditor().getEditorComponent();
        boolean value = required && isEditableWithParent() && isEnabledWithParent() && editorComponent instanceof JTextComponent
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
                        String valueText = getDisplayString(prevValue);

                        if (!Objects.equals(inputText, valueText)) {
                            comboBox.setBackground(searchEditBgColor);
                        }
                    }
                }
            }
        }
    }

    protected String getDisplayString(Object value) {
        if (value == null || value instanceof Entity) {
            return super.getDisplayString((Entity) value);
        }

        return metadataTools.format(value);
    }

    protected void clearSearchVariants() {
        items.clear();
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

    @Override
    public JComponent getComposition() {
        return composition;
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
    public int getSuggestionsLimit() {
        return suggestionsLimit;
    }

    @Override
    public void setSuggestionsLimit(int suggestionsLimit) {
        this.suggestionsLimit = suggestionsLimit;
    }

    @Override
    public void updateMissingValueState() {
        updateEditState();
    }

    @Override
    public int getMinSearchStringLength() {
        return minSearchStringLength;
    }

    @Override
    public void setMinSearchStringLength(int minSearchStringLength) {
        this.minSearchStringLength = minSearchStringLength;
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

            JTextField searchEditor = getComboBoxEditorField();
            if (value instanceof ValueWrapper) {
                searchEditor.setText(value.toString());
            }
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
    public <T> T getValue() {
        T value = super.getValue();
        return value instanceof OptionWrapper
                ? (T) ((OptionWrapper) value).getValue()
                : value;
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

            updateEditState();
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

        lastSearchString = getComboBoxEditorField().getText();
    }

    protected JTextField getComboBoxEditorField() {
        return (JTextField) comboBox.getEditor().getEditorComponent();
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
    public int getAsyncSearchTimeoutMs() {
        return asyncSearchDelayMs;
    }

    /**
     * @param asyncSearchTimeoutMs timeout between the last key press action and async search
     * @see DesktopConfig#getSearchFieldAsyncTimeoutMs()
     */
    @Override
    public void setAsyncSearchTimeoutMs(int asyncSearchTimeoutMs) {
        this.asyncSearchDelayMs = asyncSearchTimeoutMs;
    }

    @Override
    public int getAsyncSearchDelayMs() {
        return asyncSearchDelayMs;
    }

    @Override
    public void setAsyncSearchDelayMs(int asyncSearchDelayMs) {
        this.asyncSearchDelayMs = asyncSearchDelayMs;
    }

    @Override
    public SearchExecutor getSearchExecutor() {
        return searchExecutor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setSearchExecutor(SearchExecutor searchExecutor) {
        this.searchExecutor = searchExecutor;
    }

    @Override
    public EnterActionHandler getEnterActionHandler() {
        return enterActionHandler;
    }

    @Override
    public void setEnterActionHandler(EnterActionHandler enterActionHandler) {
        this.enterActionHandler = enterActionHandler;
    }

    @Override
    public ArrowDownActionHandler getArrowDownActionHandler() {
        return arrowDownActionHandler;
    }

    @Override
    public void setArrowDownActionHandler(ArrowDownActionHandler arrowDownActionHandler) {
        this.arrowDownActionHandler = arrowDownActionHandler;
    }

    @Override
    public void showSuggestions(List<?> suggestions) {
        if (asyncSearchWorker != null) {
            asyncSearchWorker.cancel(true);
            asyncSearchWorker = null;
        }

        handleSearchResults(suggestions);
    }

    @Override
    public String getInputPrompt() {
        return inputPrompt;
    }

    @Override
    public void setInputPrompt(String inputPrompt) {
        this.inputPrompt = inputPrompt;
    }

    // just stub
    @Override
    public void setPopupWidth(String width) {
        this.popupWidth = width;
    }

    // just stub
    @Override
    public String getPopupWidth() {
        return popupWidth;
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

    protected class SearchObjectWrapper extends ObjectWrapper {

        public SearchObjectWrapper(Object obj) {
            super(obj);
        }

        @Override
        public String toString() {
            if (captionFormatter != null) {
                return captionFormatter.formatValue(getValue());
            }
            return getDisplayString(obj);
        }
    }

    protected class NullOption extends SearchObjectWrapper {
        public NullOption() {
            super(null);
        }

        @Override
        public Entity getValue() {
            return null;
        }

        @Override
        public String toString() {
            return String.valueOf(DesktopSuggestionField.this.nullOption);
        }
    }
}