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

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.EntityOptions;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.value.LegacyCollectionDsValueSource;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.screen.*;
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

public class WebTokenList<V extends Entity> extends WebV8AbstractField<CubaTokenList<V>, V, Collection<V>>
        implements TokenList<V>, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(WebTokenList.class);

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
    protected ScreenBuilders screenBuilders;
    protected Icons icons;

    protected Function<Object, String> tokenStyleGenerator;

    protected final Consumer<ValueChangeEvent<V>> lookupSelectListener = e -> {
        if (isEditable()) {
            addValueFromLookupPickerField();
        }
    };

    protected Supplier<Screen> lookupProvider;
    protected Function<? super V, String> optionCaptionProvider;

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
    public OpenType getLookupOpenMode() {
        return lookupOpenMode;
    }

    @Override
    public void setLookupOpenMode(OpenType lookupOpenMode) {
        this.lookupOpenMode = lookupOpenMode;
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
    public void setLookupFieldOptionsCaptionProvider(Function<? super V, String> optionsCaptionProvider) {
        lookupPickerField.setOptionCaptionProvider(optionCaptionProvider);
    }

    @Override
    public Function<? super V, String> getLookupFieldOptionsCaptionProvider() {
        return lookupPickerField.getOptionCaptionProvider();
    }

    @Override
    public String getOptionsCaptionProperty() {
        return lookupPickerField.getCaptionProperty();
    }

    @Override
    public void setOptionsCaptionProperty(String optionsCaptionProperty) {
        lookupPickerField.setCaptionProperty(optionsCaptionProperty);
    }

    @Override
    public CaptionMode getOptionsCaptionMode() {
        return lookupPickerField.getCaptionMode();
    }

    @Override
    public void setOptionsCaptionMode(CaptionMode optionsCaptionMode) {
        lookupPickerField.setCaptionMode(optionsCaptionMode);
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
                .withScreenId(getLookupScreenInternal())
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

        if (lookupScreen instanceof MultiSelectLookupScreen) {
            ((MultiSelectLookupScreen) lookupScreen).setLookupComponentMultiSelect(isMultiSelect());
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
        // we create mutable map only for compatibilty with legacy code
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
                dialogWindow.setDialogWidth(openType.getWidthString());
            }
            if (openType.getHeight() != null) {
                dialogWindow.setDialogHeight(openType.getHeightString());
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

    protected String getInstanceCaption(V instance) {
        if (instance == null) {
            return "";
        }

        if (optionCaptionProvider != null) {
            return optionCaptionProvider.apply(instance);
        }

        return metadata.getTools().getInstanceName(instance);
    }

    @Override
    public void setOptionCaptionProvider(Function<? super V, String> optionCaptionProvider) {
        this.optionCaptionProvider = optionCaptionProvider;
    }

    @Override
    public Function<? super V, String> getOptionCaptionProvider() {
        return optionCaptionProvider;
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
}