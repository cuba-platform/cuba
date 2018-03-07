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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.NestedDatasource;
import com.haulmont.cuba.gui.data.impl.WeakCollectionChangeListener;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.widgets.CubaScrollBoxLayout;
import com.haulmont.cuba.web.widgets.CubaTokenListLabel;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.CustomField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class WebTokenList extends WebAbstractField<WebTokenList.CubaTokenList> implements TokenList {

    protected CollectionDatasource datasource;

    protected String captionProperty;

    protected CaptionMode captionMode;

    protected Position position = Position.TOP;

    protected ItemChangeHandler itemChangeHandler;

    protected ItemClickListener itemClickListener;

    protected AfterLookupCloseHandler afterLookupCloseHandler;

    protected AfterLookupSelectionHandler afterLookupSelectionHandler;

    protected boolean inline;

    protected WebButton addButton;

    protected WebButton clearButton;

    protected WebLookupPickerField lookupPickerField;

    protected String lookupScreen;
    protected WindowManager.OpenType lookupOpenMode = WindowManager.OpenType.THIS_TAB;
    protected Map<String, Object> lookupScreenParams;
    protected DialogParams lookupScreenDialogParams;

    protected TokenStyleGenerator tokenStyleGenerator;

    protected boolean lookup = false;

    protected boolean clearEnabled = true;
    protected boolean simple = false;

    protected boolean multiselect;
    protected PickerField.LookupAction lookupAction;

    protected final ValueChangeListener lookupSelectListener = e -> {
        if (isEditable()) {
            addValueFromLookupPickerField();
        }
    };

    protected CollectionDatasource.CollectionChangeListener collectionChangeListener;

    public WebTokenList() {
        addButton = new WebButton();
        Messages messages = AppBeans.get(Messages.NAME);
        addButton.setCaption(messages.getMessage(TokenList.class, "actions.Add"));

        clearButton = new WebButton();
        clearButton.setCaption(messages.getMessage(TokenList.class, "actions.Clear"));

        lookupPickerField = new WebLookupPickerField();
        lookupPickerField.addValueChangeListener(lookupSelectListener);
        component = new CubaTokenList();

        setMultiSelect(false);
    }

    @Override
    public CollectionDatasource getDatasource() {
        return datasource;
    }

    @Override
    public void setDatasource(CollectionDatasource datasource) {
        Preconditions.checkNotNullArgument(datasource, "datasource is null");

        if (this.datasource != null) {
            throw new UnsupportedOperationException("Changing datasource is not supported by the TokenList component");
        }

        this.datasource = datasource;

        collectionChangeListener = e -> {
            if (lookupPickerField != null) {
                if (isLookup()) {
                    if (getLookupScreen() != null) {
                        lookupAction.setLookupScreen(getLookupScreen());
                    } else {
                        lookupAction.setLookupScreen(null);
                    }

                    lookupAction.setLookupScreenOpenType(lookupOpenMode);
                    lookupAction.setLookupScreenParams(lookupScreenParams);
                    lookupAction.setLookupScreenDialogParams(lookupScreenDialogParams);
                }
            }
            component.refreshComponent();
            component.refreshClickListeners(itemClickListener);
        };
        //noinspection unchecked
        datasource.addCollectionChangeListener(new WeakCollectionChangeListener(datasource, collectionChangeListener));
    }

    @Override
    public void setFrame(Frame frame) {
        super.setFrame(frame);
        lookupPickerField.setFrame(frame);
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
    public WindowManager.OpenType getLookupOpenMode() {
        return lookupOpenMode;
    }

    @Override
    public void setLookupOpenMode(WindowManager.OpenType lookupOpenMode) {
        this.lookupOpenMode = lookupOpenMode;
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
    public LookupField.FilterMode getFilterMode() {
        return lookupPickerField.getFilterMode();
    }

    @Override
    public void setFilterMode(LookupField.FilterMode mode) {
        lookupPickerField.setFilterMode(mode);
    }

    @Override
    public String getOptionsCaptionProperty() {
        return lookupPickerField.getCaptionProperty();
    }

    @Override
    public void setOptionsCaptionProperty(String captionProperty) {
        lookupPickerField.setCaptionProperty(captionProperty);
    }

    @Override
    public CaptionMode getOptionsCaptionMode() {
        return lookupPickerField.getCaptionMode();
    }

    @Override
    public void setOptionsCaptionMode(CaptionMode captionMode) {
        lookupPickerField.setCaptionMode(captionMode);
    }

    @Override
    public CollectionDatasource getOptionsDatasource() {
        return lookupPickerField.getOptionsDatasource();
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        lookupPickerField.setOptionsDatasource(datasource);
    }

    @Override
    public void setRefreshOptionsOnLookupClose(boolean refresh) {
        lookupPickerField.setRefreshOptionsOnLookupClose(refresh);
    }

    @Override
    public boolean isRefreshOptionsOnLookupClose() {
        return lookupPickerField.isRefreshOptionsOnLookupClose();
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        throw new UnsupportedOperationException("TokenList does not support datasource with property");
    }

    @Override
    public MetaProperty getMetaProperty() {
        return null;
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        if (datasource != null) {
            List<Object> items = new ArrayList(datasource.getItems());
            return (T) items;
        } else
            return (T) Collections.emptyList();
    }

    @Override
    public void setValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        LoggerFactory.getLogger(WebTokenList.class).warn("addValueChangeListener not implemented for TokenList");
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        LoggerFactory.getLogger(WebTokenList.class).warn("removeValueChangeListener not implemented for TokenList");
    }

    @Override
    public List getOptionsList() {
        return lookupPickerField.getOptionsList();
    }

    @Override
    public void setOptionsList(List optionsList) {
        lookupPickerField.setOptionsList(optionsList);
    }

    @Override
    public Map<String, ?> getOptionsMap() {
        return lookupPickerField.getOptionsMap();
    }

    @Override
    public void setOptionsMap(Map<String, ?> map) {
        lookupPickerField.setOptionsMap(map);
    }

    @Override
    public boolean isClearEnabled() {
        return clearEnabled;
    }

    @Override
    public void setClearEnabled(boolean clearEnabled) {
        if (this.clearEnabled != clearEnabled) {
            clearButton.setVisible(clearEnabled);
            this.clearEnabled = clearEnabled;
            component.refreshComponent();
        }
    }

    @Override
    public boolean isLookup() {
        return lookup;
    }

    @Override
    public void setLookup(boolean lookup) {
        if (this.lookup != lookup) {
            if (lookup) {
                lookupAction = new PickerField.LookupAction(lookupPickerField) {
                    @Nonnull
                    @Override
                    protected Map<String, Object> prepareScreenParams() {
                        Map<String, Object> screenParams = super.prepareScreenParams();
                        if (isMultiSelect()) {
                            screenParams = new HashMap<>(screenParams);
                            WindowParams.MULTI_SELECT.set(screenParams, true);
                            // for backward compatibility
                            screenParams.put("multiSelect", "true");
                        }

                        return screenParams;
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    protected void handleLookupWindowSelection(Collection items) {
                        if (items.isEmpty()) {
                            return;
                        }

                        @SuppressWarnings("unchecked")
                        Collection<Entity> selected = items;

                        CollectionDatasource optionsDatasource = lookupPickerField.getOptionsDatasource();
                        if (optionsDatasource != null
                                && lookupPickerField.isRefreshOptionsOnLookupClose()) {
                            optionsDatasource.refresh();

                            if (datasource != null) {
                                for (Object obj : getDatasource().getItems()) {
                                    Entity entity = (Entity) obj;
                                    if (getOptionsDatasource().containsItem(entity.getId())) {
                                        datasource.updateItem(getOptionsDatasource().getItem(entity.getId()));
                                    }
                                }
                            }
                        }

                        // add all selected items to tokens
                        if (itemChangeHandler != null) {
                            for (Entity newItem : selected) {
                                itemChangeHandler.addItem(newItem);
                            }
                        } else if (datasource != null) {
                            // get master entity and inverse attribute in case of nested datasource
                            Entity masterEntity = getMasterEntity(datasource);
                            MetaProperty inverseProp = getInverseProperty(datasource);

                            for (Entity newItem : selected) {
                                if (!datasource.containsItem(newItem.getId())) {
                                    // Initialize reference to master entity
                                    if (inverseProp != null && isInitializeMasterReference(inverseProp)) {
                                        newItem.setValue(inverseProp.getName(), masterEntity);
                                    }

                                    datasource.addItem(newItem);
                                }
                            }
                        }

                        afterSelect(items);
                        if (afterLookupSelectionHandler != null) {
                            afterLookupSelectionHandler.onSelect(items);
                        }
                    }
                };
                lookupPickerField.addAction(lookupAction);

                if (getLookupScreen() != null) {
                    lookupAction.setLookupScreen(getLookupScreen());
                }
                lookupAction.setLookupScreenOpenType(lookupOpenMode);
                lookupAction.setLookupScreenParams(lookupScreenParams);
                lookupAction.setLookupScreenDialogParams(lookupScreenDialogParams);
            } else {
                lookupPickerField.removeAction(lookupAction);
            }

            lookupAction.setAfterLookupCloseHandler((window, actionId) -> {
                if (afterLookupCloseHandler != null) {
                    afterLookupCloseHandler.onClose(window, actionId);
                }
            });


            lookupAction.setAfterLookupSelectionHandler(items -> {
                if (afterLookupSelectionHandler != null) {
                    afterLookupSelectionHandler.onSelect(items);
                }
            });

        }
        this.lookup = lookup;
        component.refreshComponent();
    }

    @Override
    public String getLookupScreen() {
        return lookupScreen;
    }

    @Override
    public void setLookupScreen(String lookupScreen) {
        this.lookupScreen = lookupScreen;
        if (lookupAction != null) {
            lookupAction.setLookupScreen(lookupScreen);
        }
    }

    @Override
    public void setLookupScreenParams(Map<String, Object> params) {
        this.lookupScreenParams = params;
        if (lookupAction != null) {
            lookupAction.setLookupScreenParams(params);
        }
    }

    @Override
    public Map<String, Object> getLookupScreenParams() {
        return lookupScreenParams;
    }

    @Override
    public void setLookupScreenDialogParams(DialogParams dialogParams) {
        this.lookupScreenDialogParams = dialogParams;
        if (lookupAction != null) {
            lookupAction.setLookupScreenDialogParams(dialogParams);
        }
    }

    @Deprecated
    @Nullable
    @Override
    public DialogParams getLookupScreenDialogParams() {
        return lookupScreenDialogParams;
    }

    @Override
    public boolean isMultiSelect() {
        return multiselect;
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        this.multiselect = multiselect;
        component.refreshComponent();
    }

    @Override
    public String getAddButtonCaption() {
        return addButton.getCaption();
    }

    @Override
    public void setAddButtonCaption(String caption) {
        addButton.setCaption(caption);
    }

    @Override
    public String getAddButtonIcon() {
        return addButton.getIcon();
    }

    @Override
    public void setAddButtonIcon(String icon) {
        addButton.setIcon(icon);
    }

    @Override
    public String getClearButtonCaption() {
        return clearButton.getCaption();
    }

    @Override
    public void setClearButtonCaption(String caption) {
        clearButton.setCaption(caption);
    }

    @Override
    public String getClearButtonIcon() {
        return clearButton.getIcon();
    }

    @Override
    public void setClearButtonIcon(String icon) {
        clearButton.setIcon(icon);
    }

    @Override
    public ItemChangeHandler getItemChangeHandler() {
        return itemChangeHandler;
    }

    @Override
    public void setItemChangeHandler(ItemChangeHandler handler) {
        this.itemChangeHandler = handler;
    }

    @Override
    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    @Override
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        this.component.refreshClickListeners(itemClickListener);
    }

    @Override
    public AfterLookupCloseHandler getAfterLookupCloseHandler() {
        return afterLookupCloseHandler;
    }

    @Override
    public void setAfterLookupCloseHandler(AfterLookupCloseHandler afterLookupCloseHandler) {
        this.afterLookupCloseHandler = afterLookupCloseHandler;
    }

    @Override
    public AfterLookupSelectionHandler getAfterLookupSelectionHandler() {
        return afterLookupSelectionHandler;
    }

    @Override
    public void setAfterLookupSelectionHandler(AfterLookupSelectionHandler afterLookupSelectionHandler) {
        this.afterLookupSelectionHandler = afterLookupSelectionHandler;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;

        component.refreshComponent();
    }

    @Override
    public boolean isInline() {
        return inline;
    }

    @Override
    public void setInline(boolean inline) {
        this.inline = inline;

        component.refreshComponent();
    }

    @Override
    protected void setEditableToComponent(boolean editable) {
        component.refreshComponent();
    }

    @Override
    public boolean isSimple() {
        return simple;
    }

    @Override
    public void setSimple(boolean simple) {
        this.simple = simple;
        this.addButton.setVisible(simple);
        this.component.refreshComponent();
    }

    @Override
    public void setTokenStyleGenerator(TokenStyleGenerator tokenStyleGenerator) {
        this.tokenStyleGenerator = tokenStyleGenerator;
    }

    @Override
    public TokenStyleGenerator getTokenStyleGenerator() {
        return tokenStyleGenerator;
    }

    @Override
    public String getLookupInputPrompt() {
        return lookupPickerField.getInputPrompt();
    }

    @Override
    public void setLookupInputPrompt(String inputPrompt) {
        this.lookupPickerField.setInputPrompt(inputPrompt);
    }

    protected String instanceCaption(Instance instance) {
        if (instance == null) {
            return "";
        }
        if (captionProperty != null) {
            if (instance.getMetaClass().getPropertyPath(captionProperty) != null) {
                Object o = instance.getValueEx(captionProperty);
                return o != null ? o.toString() : " ";
            }

            throw new IllegalArgumentException(String.format("Couldn't find property with name '%s'", captionProperty));
        } else {
            return instance.getInstanceName();
        }
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    public class CubaTokenList extends CustomField {

        protected VerticalLayout composition;

        protected CubaScrollBoxLayout tokenContainer;

        protected HorizontalLayout editor;

        protected Map<Instance, CubaTokenListLabel> itemComponents = new HashMap<>();
        protected Map<CubaTokenListLabel, Instance> componentItems = new HashMap<>();

        public CubaTokenList() {
            setWidthUndefined();

            composition = new VerticalLayout();
            composition.setWidthUndefined();

            tokenContainer = new CubaScrollBoxLayout();
            tokenContainer.setStyleName("c-tokenlist-scrollbox");
            tokenContainer.setWidthUndefined();
            tokenContainer.setMargin(new MarginInfo(true, false, false, false));

            composition.addComponent(tokenContainer);
            setPrimaryStyleName("c-tokenlist");
        }

        @Override
        public boolean isEmpty() {
            if (datasource != null) {
                return datasource.getItems().isEmpty();
            }

            return super.isEmpty();
        }

        @Override
        public void setHeight(String height) {
            super.setHeight(height);

            if (getHeight() > 0) {
                composition.setHeight("100%");
                composition.setExpandRatio(tokenContainer, 1);
                tokenContainer.setHeight("100%");
            } else {
                composition.setHeightUndefined();
                composition.setExpandRatio(tokenContainer, 0);
                tokenContainer.setHeightUndefined();
            }
        }

        @Override
        public void setWidth(float width, Unit unit) {
            super.setWidth(width, unit);

            if (composition != null && tokenContainer != null) {
                if (getWidth() > 0) {
                    composition.setWidth("100%");
                    editor.setWidth("100%");

                    if (!isSimple()) {
                        lookupPickerField.setWidthFull();
                        editor.setExpandRatio(WebComponentsHelper.getComposition(lookupPickerField), 1);
                    }

                    tokenContainer.setWidth("100%");
                } else {
                    composition.setWidthUndefined();
                    editor.setWidthUndefined();

                    if (!isSimple()) {
                        lookupPickerField.setWidthAuto();
                        editor.setExpandRatio(WebComponentsHelper.getComposition(lookupPickerField), 0);
                    }

                    tokenContainer.setWidthUndefined();
                }
            }
        }

        @Override
        protected Component initContent() {
            return composition;
        }

        protected void initField() {
            if (editor == null) {
                editor = new HorizontalLayout();
                editor.setSpacing(true);
                editor.setWidthUndefined();
            }
            editor.removeAllComponents();

            if (!isSimple()) {
                lookupPickerField.setWidthAuto();
                editor.addComponent(WebComponentsHelper.getComposition(lookupPickerField));
            }
            lookupPickerField.setVisible(!isSimple());

            addButton.setVisible(isSimple());
            addButton.setStyleName("add-btn");

            Button wrappedButton = (Button) WebComponentsHelper.unwrap(addButton);
            Collection listeners = wrappedButton.getListeners(Button.ClickEvent.class);
            for (Object listener : listeners) {
                wrappedButton.removeClickListener((Button.ClickListener) listener);
            }

            if (!isSimple()) {
                wrappedButton.addClickListener(e -> {
                    if (isEditable()) {
                        addValueFromLookupPickerField();
                    }
                    wrappedButton.focus();
                });
            } else {
                wrappedButton.addClickListener(e -> {
                    WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);

                    String windowAlias;
                    if (getLookupScreen() != null) {
                        windowAlias = getLookupScreen();
                    } else if (getOptionsDatasource() != null) {
                        windowAlias = windowConfig.getBrowseScreenId(getOptionsDatasource().getMetaClass());
                    } else {
                        windowAlias = windowConfig.getBrowseScreenId(getDatasource().getMetaClass());
                    }

                    WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);

                    Map<String, Object> params = new HashMap<>();
                    params.put("windowOpener", WebTokenList.this.getFrame().getId());
                    if (isMultiSelect()) {
                        WindowParams.MULTI_SELECT.set(params, true);
                        // for backward compatibility
                        params.put("multiSelect", "true");
                    }
                    if (lookupScreenParams != null) {
                        params.putAll(lookupScreenParams);
                    }

                    WindowManager wm = App.getInstance().getWindowManager();
                    if (lookupOpenMode == WindowManager.OpenType.DIALOG) {
                        if (lookupScreenDialogParams != null) {
                            wm.getDialogParams().setWidth(lookupScreenDialogParams.getWidth());
                            wm.getDialogParams().setHeight(lookupScreenDialogParams.getHeight());
                        } else {
                            ThemeConstants theme = App.getInstance().getThemeConstants();
                            String width = theme.get("cuba.web.WebTokenList.lookupDialog.width");
                            String height = theme.get("cuba.web.WebTokenList.lookupDialog.height");

                            wm.getDialogParams().setWidth(width);
                            wm.getDialogParams().setHeight(height);
                        }
                        wm.getDialogParams().setResizable(true);
                    }

                    Window.Lookup lookup = wm.openLookup(windowInfo, items -> {
                        if (lookupPickerField.isRefreshOptionsOnLookupClose()) {
                            lookupPickerField.getOptionsDatasource().refresh();
                        }

                        if (isEditable()) {
                            if (items == null || items.isEmpty()) return;

                            handleLookupInternal(items);

                            if (afterLookupSelectionHandler != null) {
                                afterLookupSelectionHandler.onSelect(items);
                            }
                        }
                        wrappedButton.focus();
                    }, lookupOpenMode, params);

                    if (afterLookupCloseHandler != null) {
                        lookup.addCloseListener(actionId ->
                                afterLookupCloseHandler.onClose(lookup, actionId)
                        );
                    }
                });
            }
            editor.addComponent(wrappedButton);

            clearButton.setVisible(clearEnabled);
            clearButton.setStyleName("clear-btn");

            Button wrappedClearButton = (Button) WebComponentsHelper.unwrap(clearButton);
            wrappedClearButton.addClickListener(e -> {
                for (CubaTokenListLabel item : new ArrayList<>(itemComponents.values())) {
                    doRemove(item);
                }
                wrappedClearButton.focus();
            });
            if (isSimple()) {
                final HorizontalLayout clearLayout = new HorizontalLayout();
                clearLayout.addComponent(wrappedClearButton);
                editor.addComponent(clearLayout);
                editor.setExpandRatio(clearLayout, 1);
            } else {
                editor.addComponent(wrappedClearButton);
            }
        }

        @SuppressWarnings("unchecked")
        protected void handleLookupInternal(Collection items) {
            // get master entity and inverse attribute in case of nested datasource
            Entity masterEntity = getMasterEntity(datasource);
            MetaProperty inverseProp = getInverseProperty(datasource);
            boolean initializeMasterReference = inverseProp != null && isInitializeMasterReference(inverseProp);

            for (final Object item : items) {
                if (itemChangeHandler != null) {
                    itemChangeHandler.addItem(item);
                } else {
                    if (item instanceof Entity) {
                        Entity entity = (Entity) item;
                        if (datasource != null && !datasource.containsItem(entity.getId())) {
                            // Initialize reference to master entity
                            if (initializeMasterReference) {
                                entity.setValue(inverseProp.getName(), masterEntity);
                            }
                            datasource.addItem(entity);
                        }
                    }
                }
            }
        }

        public void refreshComponent() {
            if (inline) {
                addStyleName("inline");
            } else {
                removeStyleName("inline");
            }

            if (editable) {
                removeStyleName("readonly");
            } else {
                addStyleName("readonly");
            }

            if (editor != null) {
                composition.removeComponent(editor);
            }

            initField();

            if (isEditable()) {
                if (position == Position.TOP) {
                    composition.addComponentAsFirst(editor);
                } else {
                    composition.addComponent(editor);
                }
            }

            tokenContainer.removeAllComponents();

            if (datasource != null) {
                List<Instance> usedItems = new ArrayList<>();

                // New tokens
                for (final Object itemId : datasource.getItemIds()) {
                    final Instance item = datasource.getItem(itemId);
                    CubaTokenListLabel f = itemComponents.get(item);
                    if (f == null) {
                        f = createToken();
                        itemComponents.put(item, f);
                        componentItems.put(f, item);
                    }
                    f.setEditable(isEditable());
                    f.setText(instanceCaption(item));
                    f.setWidthUndefined();

                    setTokenStyle(f, itemId);
                    tokenContainer.addComponent(f);
                    usedItems.add(item);
                }

                // Remove obsolete items
                for (Instance componentItem : new ArrayList<>(itemComponents.keySet())) {
                    if (!usedItems.contains(componentItem)) {
                        componentItems.remove(itemComponents.get(componentItem));
                        itemComponents.remove(componentItem);
                    }
                }
            }
            if (getHeight() < 0) {
                tokenContainer.setVisible(!isEmpty());
            } else {
                tokenContainer.setVisible(true);
            }

            updateEditorMargins();

            updateSizes();
        }

        protected void updateEditorMargins() {
            if (tokenContainer.isVisible()) {
                if (position == Position.TOP) {
                    editor.setMargin(new MarginInfo(false, false, true, false));
                } else {
                    editor.setMargin(new MarginInfo(true, false, false, false));
                }
            } else {
                editor.setMargin(false);
            }
        }

        protected void updateSizes() {
            if (getHeight() > 0) {
                composition.setHeight("100%");
                composition.setExpandRatio(tokenContainer, 1);
                tokenContainer.setHeight("100%");
            } else {
                composition.setHeightUndefined();
                composition.setExpandRatio(tokenContainer, 0);
                tokenContainer.setHeightUndefined();
            }

            if (getWidth() > 0) {
                composition.setWidth("100%");
                editor.setWidth("100%");

                if (!isSimple()) {
                    lookupPickerField.setWidthFull();
                    editor.setExpandRatio(WebComponentsHelper.getComposition(lookupPickerField), 1);
                }

                tokenContainer.setWidth("100%");
            } else {
                composition.setWidthUndefined();
                editor.setWidthUndefined();

                if (!isSimple()) {
                    lookupPickerField.setWidthAuto();
                    editor.setExpandRatio(WebComponentsHelper.getComposition(lookupPickerField), 0);
                }

                tokenContainer.setWidthUndefined();
            }
        }

        public void refreshClickListeners(ItemClickListener listener) {
            if (datasource != null && CollectionDatasource.State.VALID.equals(datasource.getState())) {
                for (Object id : datasource.getItemIds()) {
                    Instance item = datasource.getItem(id);
                    final CubaTokenListLabel label = itemComponents.get(item);
                    if (label != null) {
                        if (listener != null) {
                            label.setClickListener(new CubaTokenListLabel.ClickListener() {
                                @Override
                                public void onClick(CubaTokenListLabel source) {
                                    doClick(label);
                                }
                            });
                        } else {
                            label.setClickListener(null);
                        }
                    }
                }
            }
        }

        protected CubaTokenListLabel createToken() {
            final CubaTokenListLabel label = new CubaTokenListLabel();
            label.setWidth("100%");
            label.addListener(new CubaTokenListLabel.RemoveTokenListener() {
                @Override
                public void removeToken(final CubaTokenListLabel source) {
                    if (isEditable()) {
                        doRemove(source);
                    }
                }
            });
            return label;
        }

        protected void doRemove(CubaTokenListLabel source) {
            Instance item = componentItems.get(source);
            if (item != null) {
                itemComponents.remove(item);
                componentItems.remove(source);

                if (itemChangeHandler != null) { //todo test
                    itemChangeHandler.removeItem(item);
                } else {
                    if (datasource != null) {
                        // get inverse attribute in case of nested datasource
                        MetaProperty inverseProp = getInverseProperty(datasource);
                        boolean initializeMasterReference = inverseProp != null
                                && isInitializeMasterReference(inverseProp);

                        if (initializeMasterReference) {
                            item.setValue(inverseProp.getName(), null);
                            datasource.excludeItem((Entity) item);
                            return;
                        } else {
                            datasource.removeItem((Entity) item);
                        }
                    }
                }
            }
        }

        protected void doClick(CubaTokenListLabel source) {
            if (itemClickListener != null) {
                Instance item = componentItems.get(source);
                if (item != null)
                    itemClickListener.onClick(item);
            }
        }

        @Override
        public Class<?> getType() {
            return List.class;
        }

        protected void setTokenStyle(CubaTokenListLabel label, Object itemId) {
            if (tokenStyleGenerator != null) {
                String styleName = tokenStyleGenerator.getStyle(itemId);
                if (styleName != null && !styleName.equals("")) {
                    label.setStyleName(styleName);
                }
            }
        }

        @Override
        public void setBuffered(boolean buffered) {
        }

        @Override
        public boolean isBuffered() {
            return false;
        }

        @Override
        public void removeAllValidators() {
            getValidators().clear();
        }
    }

    @SuppressWarnings("unchecked")
    protected void addValueFromLookupPickerField() {
        final Entity newItem = lookupPickerField.getValue();
        if (newItem == null) return;
        if (itemChangeHandler != null) {
            itemChangeHandler.addItem(newItem);
        } else {
            if (datasource != null) {
                // get master entity and inverse attribute in case of nested datasource
                Entity masterEntity = getMasterEntity(datasource);
                MetaProperty inverseProp = getInverseProperty(datasource);

                if (!datasource.containsItem(newItem.getId())) {
                    // Initialize reference to master entity
                    if (inverseProp != null && isInitializeMasterReference(inverseProp)) {
                        newItem.setValue(inverseProp.getName(), masterEntity);
                    }

                    datasource.addItem(newItem);
                }
            }
        }
        lookupPickerField.setValue(null);
        lookupPickerField.requestFocus();

        if (lookupPickerField.isRefreshOptionsOnLookupClose()) {
            for (Object obj : getDatasource().getItems()) {
                Entity entity = (Entity) obj;
                if (getOptionsDatasource().containsItem(entity.getId())) {
                    datasource.updateItem(getOptionsDatasource().getItem(entity.getId()));
                }
            }
        }
    }

    @Nullable
    protected Entity getMasterEntity(CollectionDatasource datasource) {
        if (datasource instanceof NestedDatasource) {
            Datasource masterDs = ((NestedDatasource) datasource).getMaster();
            com.google.common.base.Preconditions.checkState(masterDs != null);
            return masterDs.getItem();
        }
        return null;
    }

    @Nullable
    protected MetaProperty getInverseProperty(CollectionDatasource datasource) {
        if (datasource instanceof NestedDatasource) {
            MetaProperty metaProperty = ((NestedDatasource) datasource).getProperty();
            com.google.common.base.Preconditions.checkState(metaProperty != null);
            return metaProperty.getInverse();
        }
        return null;
    }

    protected boolean isInitializeMasterReference(MetaProperty inverseProp) {
        return !inverseProp.getRange().getCardinality().isMany()
                && isInversePropertyAssignableFromDsClass(inverseProp);

    }

    protected boolean isInversePropertyAssignableFromDsClass(MetaProperty inverseProp) {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        ExtendedEntities extendedEntities = metadata.getExtendedEntities();

        Class inversePropClass = extendedEntities.getEffectiveClass(inverseProp.getDomain());
        Class dsClass = extendedEntities.getEffectiveClass(datasource.getMetaClass());

        //noinspection unchecked
        return inversePropClass.isAssignableFrom(dsClass);
    }

    @Override
    protected boolean isEmpty(Object value) {
        return super.isEmpty(value) || (value instanceof Collection && ((Collection) value).isEmpty());
    }

    @Override
    public void requestFocus() {
        if (simple) {
            addButton.requestFocus();
        } else {
            lookupPickerField.requestFocus();
        }
    }
}