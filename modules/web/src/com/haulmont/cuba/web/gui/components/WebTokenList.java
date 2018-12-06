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
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.EntityOptions;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.value.LegacyCollectionDsValueSource;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.web.widgets.CubaScrollBoxLayout;
import com.haulmont.cuba.web.widgets.CubaTokenListLabel;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.haulmont.cuba.gui.WindowManager.OpenType;

public class WebTokenList<V extends Entity> extends WebV8AbstractField<WebTokenList.CubaTokenList<V>, V, Collection<V>>
        implements TokenList<V>, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(WebTokenList.class);

    protected String captionProperty;
    protected CaptionMode captionMode;

    protected ItemChangeHandler itemChangeHandler;
    protected ItemClickListener itemClickListener;

    protected AfterLookupCloseHandler afterLookupCloseHandler;
    protected AfterLookupSelectionHandler afterLookupSelectionHandler;

    protected Button addButton;
    protected Button clearButton;

    protected LookupPickerField<V> lookupPickerField;
    protected Action lookupAction;
    protected String lookupScreen;
    protected Map<String, Object> lookupScreenParams;
    protected OpenType lookupOpenMode;

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
    protected ScreenBuilders screenBuilders;
    protected Icons icons;

    protected Function<Object, String> tokenStyleGenerator;

    protected final Consumer<ValueChangeEvent<V>> lookupSelectListener = e -> {
        if (isEditable()) {
            addValueFromLookupPickerField();
        }
    };

    protected Supplier<Screen> lookupProvider;

    public WebTokenList() {
        component = new CubaTokenList<>(this);
    }

    @Inject
    public void setScreenBuilders(ScreenBuilders screenBuilders) {
        this.screenBuilders = screenBuilders;
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

    @Inject
    public void setIcons(Icons icons) {
        this.icons = icons;
    }

    @Override
    public void afterPropertiesSet() {
        createComponents();
    }

    protected void createComponents() {
        addButton = uiComponents.create(Button.class);
        addButton.setCaption(messages.getMainMessage("actions.Add"));

        clearButton = uiComponents.create(Button.class);
        clearButton.setCaption(messages.getMainMessage("actions.Clear"));

        //noinspection unchecked
        lookupPickerField = uiComponents.create(LookupPickerField.class);
        lookupPickerField.addValueChangeListener(lookupSelectListener);

        setMultiSelect(false);
    }

    @Override
    public void setValueSource(ValueSource<Collection<V>> valueSource) {
        super.setValueSource(valueSource);

        if (valueSource != null) {
            valueSource.addValueChangeListener(e -> {
                component.refreshComponent();
                component.refreshClickListeners(itemClickListener);
            });
        }
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
    public void setRefreshOptionsOnLookupClose(boolean refresh) {
        lookupPickerField.setRefreshOptionsOnLookupClose(refresh);
    }

    @Override
    public boolean isRefreshOptionsOnLookupClose() {
        return lookupPickerField.isRefreshOptionsOnLookupClose();
    }

    @Override
    public void setOptions(Options<V> options) {
        if (options != null
                && !(options instanceof EntityOptions)) {
            throw new IllegalArgumentException("TokenList supports only EntityOptions");
        }
        lookupPickerField.setOptions(options);
    }

    @Override
    public Options<V> getOptions() {
        return lookupPickerField.getOptions();
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
                lookupAction = createLookupAction();
                lookupPickerField.addAction(lookupAction);
            } else {
                lookupPickerField.removeAction(lookupAction);
            }
        }
        this.lookup = lookup;
        component.refreshComponent();
    }

    protected Action createLookupAction() {
        return new BaseAction("")
                .withIcon(icons.get(CubaIcon.PICKERFIELD_LOOKUP))
                .withHandler(e -> openLookup(null));
    }

    protected void openLookup(@Nullable Runnable afterLookupSelect) {
        Screen lookupScreen;

        if (lookupProvider == null) {
            lookupScreen = createLookupScreen(afterLookupSelect);
        } else {
            lookupScreen = lookupProvider.get();
            if (!(LookupScreen.class.isAssignableFrom(lookupScreen.getClass()))) {
                log.info("Not suitable screen is returned from LookupScreen provider. Default implementation will be used");

                lookupScreen = createLookupScreen(afterLookupSelect);
            }

            //noinspection unchecked
            ((LookupScreen<V>) lookupScreen).setSelectHandler(selected -> {
                handleLookupSelection(selected);
                if (afterLookupSelect != null) {
                    afterLookupSelect.run();
                }
            });
        }

        lookupScreen.show();

        if (afterLookupCloseHandler != null) {
            lookupScreen.addAfterCloseListener(event -> {
                String actionId = ((StandardCloseAction) event.getCloseAction()).getActionId();
                afterLookupCloseHandler.onClose(event.getScreen().getWindow(), actionId);
            });
        }
    }

    protected Screen createLookupScreen(Runnable afterLookupSelect) {
        Class<V> entityClass = getLookupEntityClass();
        OpenMode openMode = lookupOpenMode != null
                ? lookupOpenMode.getOpenMode()
                : OpenMode.DIALOG;

        Screen lookupScreen = screenBuilders.lookup(entityClass, getFrame().getFrameOwner())
                .withScreen(getLookupScreenInternal())
                .withLaunchMode(openMode)
                .withOptions(new MapScreenOptions(getLookupScreenParamsInternal()))
                .withSelectHandler(selected -> {
                    handleLookupSelection(selected);
                    if (afterLookupSelect != null) {
                        afterLookupSelect.run();
                    }
                })
                .build();

        if (lookupOpenMode != null) {
            applyOpenTypeParameters(lookupScreen.getWindow(), lookupOpenMode);
        }

        return lookupScreen;
    }

    protected String getLookupScreenInternal() {
        return StringUtils.isNotEmpty(getLookupScreen())
                ? getLookupScreen()
                : windowConfig.getLookupScreen(getLookupEntityClass()).getId();
    }

    protected Class<V> getLookupEntityClass() {
        Class<V> entityClass;

        ValueSource<Collection<V>> valueSource = getValueSource();
        if (valueSource != null) {
            if (valueSource instanceof EntityValueSource) {
                //noinspection unchecked
                entityClass = ((EntityValueSource) valueSource).getMetaPropertyPath().getRangeJavaClass();
            } else {
                entityClass = ((LegacyCollectionDsValueSource<V>) valueSource).getDatasource().getMetaClass().getJavaClass();
            }
        } else if (getOptions() instanceof EntityOptions) {
            entityClass = ((EntityOptions<V>) getOptions()).getEntityMetaClass().getJavaClass();
        } else {
            throw new RuntimeException("Unable to determine entity class to open lookup");
        }

        return entityClass;
    }

    protected Map<String, Object> getLookupScreenParamsInternal() {
        Map<String, Object> params = new HashMap<>();
        params.put("windowOpener", getFrame().getId());
        if (isMultiSelect()) {
            WindowParams.MULTI_SELECT.set(params, true);
            // for backward compatibility
            params.put("multiSelect", "true");
        }
        if (lookupScreenParams != null) {
            params.putAll(lookupScreenParams);
        }
        return params;
    }

    @Deprecated
    protected void applyOpenTypeParameters(Window window, OpenType openType) {
        if (window instanceof DialogWindow) {
            DialogWindow dialogWindow = (DialogWindow) window;

            if (openType.getCloseOnClickOutside() != null) {
                dialogWindow.setCloseOnClickOutside(openType.getCloseOnClickOutside());
            }
            if (openType.getMaximized() != null) {
                dialogWindow.setWindowMode(openType.getMaximized() ? DialogWindow.WindowMode.MAXIMIZED : DialogWindow.WindowMode.NORMAL);
            }
            if (openType.getModal() != null) {
                dialogWindow.setModal(openType.getModal());
            }
            if (openType.getResizable() != null) {
                dialogWindow.setResizable(openType.getResizable());
            }
            if (openType.getWidth() != null) {
                dialogWindow.setDialogWidth(openType.getWidth() + openType.getWidthUnit().getSymbol());
            }
            if (openType.getHeight() != null) {
                dialogWindow.setDialogHeight(openType.getHeight() + openType.getHeightUnit().getSymbol());
            }
        }

        if (openType.getCloseable() != null) {
            window.setCloseable(openType.getCloseable());
        }
    }

    protected void handleLookupSelection(Collection<V> selectedEntities) {
        if (CollectionUtils.isEmpty(selectedEntities)) {
            return;
        }

        handleSelection(selectedEntities);

        if (afterLookupSelectionHandler != null) {
            afterLookupSelectionHandler.onSelect(selectedEntities);
        }
    }

    protected void handleSelection(Collection<V> selected) {
        if (itemChangeHandler != null) {
            selected.forEach(itemChangeHandler::addItem);
        } else {
            ValueSource<Collection<V>> valueSource = getValueSource();
            if (valueSource != null) {
                Collection<V> modelValue = refreshValueIfNeeded();

                for (V newItem : selected) {
                    if (!modelValue.contains(newItem)) {
                        modelValue.add(newItem);
                    }
                }

                valueSource.setValue(modelValue);
            }
        }
    }

    protected Collection<V> refreshValueIfNeeded() {
        EntityOptions<V> options = (EntityOptions<V>) getOptions();
        Collection<V> valueSourceValue = getValueSourceValue();

        if (options == null || !isRefreshOptionsOnLookupClose()) {
            return valueSourceValue;
        }

        options.refresh();

        for (V value : valueSourceValue) {
            options.getOptions()
                    .filter(option -> Objects.equals(option.getId(), value.getId()))
                    .findFirst()
                    .ifPresent(option -> {
                        valueSourceValue.remove(value);
                        valueSourceValue.add(option);
                    });
        }

        return valueSourceValue;
    }

    @Nonnull
    protected Collection<V> getValueSourceValue() {
        ValueSource<Collection<V>> valueSource = getValueSource();
        if (valueSource == null) {
            return Collections.emptyList();
        }

        Collection<V> modelValue;

        if (valueSource instanceof EntityValueSource) {
            Class<?> modelCollectionType = ((EntityValueSource) valueSource)
                    .getMetaPropertyPath().getMetaProperty().getJavaType();

            Collection<V> valueSourceValue = valueSource.getValue() == null
                    ? Collections.emptyList()
                    : valueSource.getValue();

            if (Set.class.isAssignableFrom(modelCollectionType)) {
                modelValue = new LinkedHashSet<>(valueSourceValue);
            } else {
                modelValue = new ArrayList<>(valueSourceValue);
            }
        } else {
            modelValue = new ArrayList<>(valueSource.getValue());
        }

        return modelValue;
    }

    @Override
    public String getLookupScreen() {
        return lookupScreen;
    }

    @Override
    public void setLookupScreen(String lookupScreen) {
        this.lookupScreen = lookupScreen;
    }

    @Override
    public void setLookupScreenParams(Map<String, Object> params) {
        this.lookupScreenParams = params;
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
            if (instance.getMetaClass().getPropertyPath(captionProperty) == null) {
                throw new IllegalArgumentException(String.format("Couldn't find property with name '%s'", captionProperty));
            }

            Object o = instance.getValueEx(captionProperty);
            return o != null ? o.toString() : " ";
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

    protected void addValueFromLookupPickerField() {
        V newItem = lookupPickerField.getValue();
        if (newItem == null) {
            return;
        }

        handleSelection(Collections.singleton(newItem));

        lookupPickerField.setValue(null);
        lookupPickerField.focus();
    }

    @Override
    protected boolean isEmpty(Object value) {
        return super.isEmpty(value)
                || (value instanceof Collection && ((Collection) value).isEmpty());
    }

    @Override
    public void focus() {
        if (simple) {
            addButton.focus();
        } else {
            lookupPickerField.focus();
        }
    }

    @Override
    public void setLookupProvider(Supplier<Screen> lookupProvider) {
        this.lookupProvider = lookupProvider;
    }

    @Override
    public Supplier<Screen> getLookupProvider() {
        return lookupProvider;
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
            return null;
        }

        @Override
        protected void doSetValue(T value) {
        }

        @Override
        public boolean isEmpty() {
            return owner.getValueSource() != null
                    ? owner.getValueSourceValue().isEmpty()
                    : super.isEmpty();
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
                addButtonSub = owner.addButton.addClickListener(e ->
                        owner.openLookup(() -> owner.addButton.focus()));
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
                HorizontalLayout clearLayout = new HorizontalLayout();
                clearLayout.addComponent(vClearButton);
                editor.addComponent(clearLayout);
                editor.setExpandRatio(clearLayout, 1);
            } else {
                editor.addComponent(vClearButton);
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

            //noinspection unchecked
            ValueSource<Collection<Entity>> valueSource = owner.getValueSource();

            if (valueSource != null && CollectionUtils.isNotEmpty(valueSource.getValue())) {
                List<Instance> usedItems = new ArrayList<>();

                // New tokens
                for (Entity entity : valueSource.getValue()) {
                    CubaTokenListLabel f = itemComponents.get(entity);
                    if (f == null) {
                        f = createToken();
                        itemComponents.put(entity, f);
                        componentItems.put(f, entity);
                    }
                    f.setEditable(owner.isEditable());
                    f.setText(owner.instanceCaption(entity));
                    f.setWidthUndefined();

                    setTokenStyle(f, entity.getId());
                    tokenContainer.addComponent(f);
                    usedItems.add(entity);
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
            //noinspection unchecked
            ValueSource<Collection<Entity>> valueSource = owner.getValueSource();
            if (valueSource != null
                    && CollectionUtils.isNotEmpty(valueSource.getValue())
                    && BindingState.ACTIVE == valueSource.getState()) {

                for (Entity entity : valueSource.getValue()) {
                    CubaTokenListLabel label = itemComponents.get(entity);
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
            CubaTokenListLabel label = new CubaTokenListLabel();
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
                    ValueSource<Collection<? extends Entity>> valueSource = owner.getValueSource();
                    if (valueSource != null) {
                        Collection<? extends Entity> value = owner.getValueSourceValue();

                        value.remove((Entity) item);

                        valueSource.setValue(value);
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
                if (styleName != null && !styleName.isEmpty()) {
                    label.setStyleName(styleName);
                }
            }
        }
    }
}