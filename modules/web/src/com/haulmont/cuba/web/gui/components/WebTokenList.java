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

import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.events.sys.VoidSubscription;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.NestedDatasource;
import com.haulmont.cuba.gui.data.impl.WeakCollectionChangeListener;
import com.haulmont.cuba.gui.screen.StandardCloseAction;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.widgets.CubaScrollBoxLayout;
import com.haulmont.cuba.web.widgets.CubaTokenListLabel;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.haulmont.cuba.gui.WindowManager.OpenType;

public class WebTokenList<V> extends WebV8AbstractField<WebTokenList.CubaTokenList<V>, V, Collection<V>> implements TokenList<V>,
        InitializingBean {

    protected CollectionDatasource datasource;
    protected CollectionDatasource.CollectionChangeListener collectionChangeListener;

    protected String captionProperty;
    protected CaptionMode captionMode;

    protected ItemChangeHandler itemChangeHandler;
    protected ItemClickListener itemClickListener;

    protected AfterLookupCloseHandler afterLookupCloseHandler;
    protected AfterLookupSelectionHandler afterLookupSelectionHandler;

    protected Button addButton;
    protected Button clearButton;

    protected LookupPickerField<Entity> lookupPickerField;
    protected PickerField.LookupAction lookupAction;
    protected String lookupScreen;
    protected Map<String, Object> lookupScreenParams;
    protected OpenType lookupOpenMode = OpenType.THIS_TAB;

    protected Position position = Position.TOP;
    protected boolean inline;
    protected boolean lookup = false;
    protected boolean clearEnabled = true;
    protected boolean simple = false;
    protected boolean multiselect;

    protected UiComponents uiComponents;
    protected Messages messages;
    protected Metadata metadata;
    protected WindowConfig windowConfig;

    protected Function<Object, String> tokenStyleGenerator;

    protected final Consumer<ValueChangeEvent<Entity>> lookupSelectListener = e -> {
        if (isEditable()) {
            addValueFromLookupPickerField();
        }
    };

    public WebTokenList() {
        component = new CubaTokenList<>(this);
    }

    @Inject
    public void setUiComponents(UiComponents uiComponents) {
        this.uiComponents = uiComponents;
    }

    @Inject
    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Inject
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Inject
    public void setWindowConfig(WindowConfig windowConfig) {
        this.windowConfig = windowConfig;
    }

    @Override
    public void afterPropertiesSet() {
        createComponents();
        initComponentsCaptions();
    }

    protected void createComponents() {
        addButton = uiComponents.create(Button.class);
        clearButton = uiComponents.create(Button.class);

        //noinspection unchecked
        lookupPickerField = uiComponents.create(LookupPickerField.class);
        lookupPickerField.addValueChangeListener(lookupSelectListener);

        setMultiSelect(false);
    }

    protected void initComponentsCaptions() {
        addButton.setCaption(messages.getMessage(TokenList.class, "actions.Add"));
        clearButton.setCaption(messages.getMessage(TokenList.class, "actions.Clear"));
    }

    @Override
    public CollectionDatasource getDatasource() {
        return datasource;
    }

    @Override
    public void setDatasource(CollectionDatasource datasource) {
        Preconditions.checkNotNullArgument(datasource, "Datasource is null");

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
    public OpenType getLookupOpenMode() {
        return lookupOpenMode;
    }

    @Override
    public void setLookupOpenMode(OpenType lookupOpenMode) {
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
    public Collection<V> getValue() {
        if (datasource != null) {
            List<Object> items = new ArrayList(datasource.getItems());
            return (Collection<V>) items;
        } else
            return Collections.emptyList();
    }

    @Override
    public void setValue(Collection<V> value) {
        throw new UnsupportedOperationException("Setting value to TokenList is not supported");
    }

    @Override
    public Subscription addValueChangeListener(Consumer listener) {
        LoggerFactory.getLogger(WebTokenList.class)
                .warn("addValueChangeListener not implemented for TokenList");

        return VoidSubscription.INSTANCE;
    }

    @Override
    public void removeValueChangeListener(Consumer listener) {
        LoggerFactory.getLogger(WebTokenList.class)
                .warn("removeValueChangeListener not implemented for TokenList");
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
        lookupPickerField.setOptionsMap((Map<String, Entity>) map);
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
    public void setTokenStyleGenerator(Function<Object, String> tokenStyleGenerator) {
        this.tokenStyleGenerator = tokenStyleGenerator;
    }

    @Override
    public Function<Object, String> getTokenStyleGenerator() {
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
            return metadata.getTools().getInstanceName(instance);
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
        lookupPickerField.focus();

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
    public void focus() {
        if (simple) {
            addButton.focus();
        } else {
            lookupPickerField.focus();
        }
    }

    public static class CubaTokenList<T> extends CustomField<T> {

        protected static final String TOKENLIST_STYLENAME = "c-tokenlist";
        protected static final String TOKENLIST_SCROLLBOX_STYLENAME = "c-tokenlist-scrollbox";

        protected static final String ADD_BTN_STYLENAME = "add-btn";
        protected static final String CLEAR_BTN_STYLENAME = "clear-btn";
        protected static final String INLINE_STYLENAME = "inline";
        protected static final String READONLY_STYLENAME = "readonly";

        protected WebTokenList owner;

        protected VerticalLayout composition;
        protected CubaScrollBoxLayout tokenContainer;
        protected HorizontalLayout editor;

        protected Map<Instance, CubaTokenListLabel> itemComponents = new HashMap<>();
        protected Map<CubaTokenListLabel, Instance> componentItems = new HashMap<>();

        protected Subscription addButtonSub;

        public CubaTokenList(WebTokenList owner) {
            this.owner = owner;

            setWidthUndefined();

            composition = new VerticalLayout();
            composition.setWidthUndefined();

            tokenContainer = new CubaScrollBoxLayout();
            tokenContainer.setStyleName(TOKENLIST_SCROLLBOX_STYLENAME);
            tokenContainer.setWidthUndefined();
            tokenContainer.setMargin(new MarginInfo(true, false, false, false));

            composition.addComponent(tokenContainer);
            setPrimaryStyleName(TOKENLIST_STYLENAME);
        }

        @Override
        public T getValue() {
            // do nothing
            return null;
        }

        @Override
        protected void doSetValue(T value) {
            // do nothing
        }

        @Override
        public boolean isEmpty() {
            if (owner.datasource != null) {
                return owner.datasource.getItems().isEmpty();
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

                    if (!owner.isSimple()) {
                        owner.lookupPickerField.setWidthFull();
                        editor.setExpandRatio(WebComponentsHelper.getComposition(owner.lookupPickerField), 1);
                    }
                } else {
                    composition.setWidthUndefined();
                    editor.setWidthUndefined();

                    if (!owner.isSimple()) {
                        owner.lookupPickerField.setWidthAuto();
                        editor.setExpandRatio(WebComponentsHelper.getComposition(owner.lookupPickerField), 0);
                    }
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

            if (!owner.isSimple()) {
                owner.lookupPickerField.setWidthAuto();
                editor.addComponent(WebComponentsHelper.getComposition(owner.lookupPickerField));
            }
            owner.lookupPickerField.setVisible(!owner.isSimple());

            owner.addButton.setVisible(owner.isSimple());
            owner.addButton.setStyleName(ADD_BTN_STYLENAME);

            if (addButtonSub != null) {
                addButtonSub.remove();
            }

            if (!owner.isSimple()) {
                addButtonSub = owner.addButton.addClickListener(e -> {
                    if (owner.isEditable()) {
                        owner.addValueFromLookupPickerField();
                    }
                    owner.addButton.focus();
                });
            } else {
                addButtonSub = owner.addButton.addClickListener(e -> {
                    String windowAlias;
                    if (owner.getLookupScreen() != null) {
                        windowAlias = owner.getLookupScreen();
                    } else if (owner.getOptionsDatasource() != null) {
                        windowAlias = owner.windowConfig.getBrowseScreenId(owner.getOptionsDatasource().getMetaClass());
                    } else {
                        windowAlias = owner.windowConfig.getBrowseScreenId(owner.getDatasource().getMetaClass());
                    }

                    WindowInfo windowInfo = owner.windowConfig.getWindowInfo(windowAlias);

                    Map<String, Object> params = new HashMap<>();
                    params.put("windowOpener", owner.getFrame().getId());
                    if (owner.isMultiSelect()) {
                        WindowParams.MULTI_SELECT.set(params, true);
                        // for backward compatibility
                        params.put("multiSelect", "true");
                    }
                    if (owner.lookupScreenParams != null) {
                        //noinspection unchecked
                        params.putAll(owner.lookupScreenParams);
                    }

                    WindowManager wm = App.getInstance().getWindowManager();

                    AbstractLookup lookupWindow = (AbstractLookup) wm.openLookup(windowInfo, items -> {
                        if (owner.lookupPickerField.isRefreshOptionsOnLookupClose()) {
                            owner.lookupPickerField.getOptionsDatasource().refresh();
                        }

                        if (owner.isEditable()) {
                            if (items == null || items.isEmpty()) return;

                            handleLookupInternal(items);

                            if (owner.afterLookupSelectionHandler != null) {
                                owner.afterLookupSelectionHandler.onSelect(items);
                            }
                        }
                        owner.addButton.focus();
                    }, owner.lookupOpenMode, params);

                    if (owner.afterLookupCloseHandler != null) {
                        lookupWindow.addAfterCloseListener(event -> {
                            String actionId = ((StandardCloseAction) event.getCloseAction()).getActionId();
                            owner.afterLookupCloseHandler.onClose(lookupWindow, actionId);
                        });
                    }
                });
            }
            editor.addComponent(owner.addButton.unwrap(com.vaadin.ui.Button.class));

            owner.clearButton.setVisible(owner.clearEnabled);
            owner.clearButton.setStyleName(CLEAR_BTN_STYLENAME);
            owner.clearButton.addClickListener(e -> {
                for (CubaTokenListLabel item : new ArrayList<>(itemComponents.values())) {
                    doRemove(item);
                }
                owner.clearButton.focus();
            });

            com.vaadin.ui.Button vClearButton = owner.clearButton.unwrap(com.vaadin.ui.Button.class);
            if (owner.isSimple()) {
                final HorizontalLayout clearLayout = new HorizontalLayout();
                clearLayout.addComponent(vClearButton);
                editor.addComponent(clearLayout);
                editor.setExpandRatio(clearLayout, 1);
            } else {
                editor.addComponent(vClearButton);
            }
        }

        @SuppressWarnings("unchecked")
        protected void handleLookupInternal(Collection items) {
            // get master entity and inverse attribute in case of nested datasource
            Entity masterEntity = owner.getMasterEntity(owner.datasource);
            MetaProperty inverseProp = owner.getInverseProperty(owner.datasource);
            boolean initializeMasterReference = inverseProp != null && owner.isInitializeMasterReference(inverseProp);

            for (final Object item : items) {
                if (owner.itemChangeHandler != null) {
                    owner.itemChangeHandler.addItem(item);
                } else {
                    if (item instanceof Entity) {
                        Entity entity = (Entity) item;
                        if (owner.datasource != null && !owner.datasource.containsItem(entity.getId())) {
                            // Initialize reference to master entity
                            if (initializeMasterReference) {
                                entity.setValue(inverseProp.getName(), masterEntity);
                            }
                            owner.datasource.addItem(entity);
                        }
                    }
                }
            }
        }

        public void refreshComponent() {
            if (owner.inline) {
                addStyleName(INLINE_STYLENAME);
            } else {
                removeStyleName(INLINE_STYLENAME);
            }

            if (owner.editable) {
                removeStyleName(READONLY_STYLENAME);
            } else {
                addStyleName(READONLY_STYLENAME);
            }

            if (editor != null) {
                composition.removeComponent(editor);
            }

            initField();

            if (owner.isEditable()) {
                if (owner.position == Position.TOP) {
                    composition.addComponentAsFirst(editor);
                } else {
                    composition.addComponent(editor);
                }
            }

            tokenContainer.removeAllComponents();

            if (owner.datasource != null) {
                List<Instance> usedItems = new ArrayList<>();

                // New tokens
                for (final Object itemId : owner.datasource.getItemIds()) {
                    //noinspection unchecked
                    final Instance item = owner.datasource.getItem(itemId);
                    CubaTokenListLabel f = itemComponents.get(item);
                    if (f == null) {
                        f = createToken();
                        itemComponents.put(item, f);
                        componentItems.put(f, item);
                    }
                    f.setEditable(owner.isEditable());
                    f.setText(owner.instanceCaption(item));
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
                if (owner.position == Position.TOP) {
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

                if (!owner.isSimple()) {
                    owner.lookupPickerField.setWidthFull();
                    editor.setExpandRatio(WebComponentsHelper.getComposition(owner.lookupPickerField), 1);
                }
            } else {
                composition.setWidthUndefined();
                editor.setWidthUndefined();

                if (!owner.isSimple()) {
                    owner.lookupPickerField.setWidthAuto();
                    editor.setExpandRatio(WebComponentsHelper.getComposition(owner.lookupPickerField), 0);
                }
            }
        }

        public void refreshClickListeners(ItemClickListener listener) {
            if (owner.datasource != null && CollectionDatasource.State.VALID.equals(owner.datasource.getState())) {
                for (Object id : owner.datasource.getItemIds()) {
                    //noinspection unchecked
                    Instance item = owner.datasource.getItem(id);
                    final CubaTokenListLabel label = itemComponents.get(item);
                    if (label != null) {
                        if (listener != null) {
                            label.setClickListener(source ->
                                    doClick(label));
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
            label.addListener((CubaTokenListLabel.RemoveTokenListener) source -> {
                if (owner.isEditable()) {
                    doRemove(source);
                }
            });
            return label;
        }

        @SuppressWarnings("unchecked")
        protected void doRemove(CubaTokenListLabel source) {
            Instance item = componentItems.get(source);
            if (item != null) {
                itemComponents.remove(item);
                componentItems.remove(source);

                if (owner.itemChangeHandler != null) {
                    owner.itemChangeHandler.removeItem(item);
                } else {
                    if (owner.datasource != null) {
                        // get inverse attribute in case of nested datasource
                        MetaProperty inverseProp = owner.getInverseProperty(owner.datasource);
                        boolean initializeMasterReference = inverseProp != null
                                && owner.isInitializeMasterReference(inverseProp);

                        if (initializeMasterReference) {
                            item.setValue(inverseProp.getName(), null);
                            owner.datasource.excludeItem((Entity) item);
                        } else {
                            owner.datasource.removeItem((Entity) item);
                        }
                    }
                }
            }
        }

        protected void doClick(CubaTokenListLabel source) {
            if (owner.itemClickListener != null) {
                Instance item = componentItems.get(source);
                if (item != null) {
                    owner.itemClickListener.onClick(item);
                }
            }
        }

        protected void setTokenStyle(CubaTokenListLabel label, Object itemId) {
            if (owner.tokenStyleGenerator != null) {
                //noinspection unchecked
                String styleName = ((Function<Object, String>) owner.getTokenStyleGenerator()).apply(itemId);
                if (styleName != null && !styleName.equals("")) {
                    label.setStyleName(styleName);
                }
            }
        }
    }
}