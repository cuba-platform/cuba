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
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.components.SecuredActionsHolder;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.meta.EntityOptions;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.meta.OptionsBinding;
import com.haulmont.cuba.gui.components.data.options.OptionsBinder;
import com.haulmont.cuba.web.gui.components.util.ShortcutListenerDelegate;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaComboBoxPickerField;
import com.haulmont.cuba.web.widgets.CubaPickerField;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.haulmont.cuba.web.gui.components.WebLookupField.NULL_ITEM_ICON_GENERATOR;
import static com.haulmont.cuba.web.gui.components.WebLookupField.NULL_STYLE_GENERATOR;

public class WebLookupPickerField<V extends Entity> extends WebPickerField<V>
        implements LookupPickerField<V>, SecuredActionsHolder {

    protected V nullOption;
    protected boolean nullOptionVisible = true;

    protected FilterMode filterMode = FilterMode.CONTAINS;
    protected FilterPredicate filterPredicate;

    protected Consumer<String> newOptionHandler;

    protected Function<? super V, String> optionIconProvider;
    protected Function<? super V, String> optionStyleProvider;

    protected OptionsBinding<V> optionsBinding;

    protected IconResolver iconResolver;

    protected boolean refreshOptionsOnLookupClose = false;

    protected Locale locale;

    public WebLookupPickerField() {
    }

    @Override
    protected CubaPickerField<V> createComponent() {
        return new CubaComboBoxPickerField<>();
    }

    @Override
    public CubaComboBoxPickerField<V> getComponent() {
        return (CubaComboBoxPickerField<V>) super.getComponent();
    }

    @Inject
    public void setUserSessionSource(UserSessionSource userSessionSource) {
        this.locale = userSessionSource.getLocale();
    }

    @Inject
    public void setIconResolver(IconResolver iconResolver) {
        this.iconResolver = iconResolver;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        Configuration configuration = beanLocator.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        setPageLength(clientConfig.getLookupFieldPageLength());
    }

    @Override
    protected void initComponent(CubaPickerField<V> component) {
        CubaComboBoxPickerField<V> impl = (CubaComboBoxPickerField<V>) component;
        impl.setItemCaptionGenerator(this::generateItemCaption);
        impl.getFieldInternal().setCustomValueEquals(this::fieldValueEquals);

        component.addShortcutListener(new ShortcutListenerDelegate("clearShortcut",
                ShortcutAction.KeyCode.DELETE, new int[]{ShortcutAction.ModifierKey.SHIFT})
                .withHandler(this::handleClearShortcut));
    }

    protected void handleClearShortcut(@SuppressWarnings("unused") Object sender, @SuppressWarnings("unused") Object target) {
        if (!isRequired()
                && isEnabledRecursive()
                && isEditableWithParent()) {
            setValue(null);
        }
    }

    protected String generateDefaultItemCaption(V item) {
        if (valueBinding != null && valueBinding.getSource() instanceof EntityValueSource) {
            EntityValueSource entityValueSource = (EntityValueSource) valueBinding.getSource();
            return metadataTools.format(item, entityValueSource.getMetaPropertyPath().getMetaProperty());
        }

        return metadataTools.format(item);
    }

    protected String generateItemCaption(V item) {
        if (item == null) {
            return "";
        }

        if (optionCaptionProvider != null) {
            return optionCaptionProvider.apply(item);
        }

        return generateDefaultItemCaption(item);
    }

    protected String generateItemStylename(V item) {
        if (optionStyleProvider == null) {
            return null;
        }

        return this.optionStyleProvider.apply(item);
    }

    @Override
    public V getNullOption() {
        return nullOption;
    }

    @Override
    public void setNullOption(V nullOption) {
        this.nullOption = nullOption;
        setNullSelectionCaption(generateItemCaption(nullOption));
    }

    @Override
    public String getNullSelectionCaption() {
        return getComponent().getEmptySelectionCaption();
    }

    @Override
    public void setNullSelectionCaption(String nullOption) {
        getComponent().setEmptySelectionCaption(nullOption);

        setInputPrompt(null);
    }

    @Override
    public FilterMode getFilterMode() {
        return filterMode;
    }

    @Override
    public void setFilterMode(FilterMode filterMode) {
        this.filterMode = filterMode;
    }

    @Override
    public boolean isNewOptionAllowed() {
        return getComponent().getNewItemHandler() != null;
    }

    @Override
    public void setNewOptionAllowed(boolean newItemAllowed) {
        if (newItemAllowed
                && getComponent().getNewItemHandler() == null) {
            getComponent().setNewItemHandler(this::onNewItemEntered);
        }

        if (!newItemAllowed
                && getComponent().getNewItemHandler() != null) {
            getComponent().setNewItemHandler(null);
        }
    }

    protected void onNewItemEntered(String newItemCaption) {
        if (newOptionHandler == null) {
            throw new IllegalStateException("New item handler cannot be NULL");
        }
        newOptionHandler.accept(newItemCaption);
    }

    @Override
    public Subscription addFieldValueChangeListener(Consumer<FieldValueChangeEvent<V>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFieldEditable(boolean editable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isTextInputAllowed() {
        return getComponent().isTextInputAllowed();
    }

    @Override
    public void setTextInputAllowed(boolean textInputAllowed) {
        getComponent().setTextInputAllowed(textInputAllowed);
    }

    @Override
    public void setAutomaticPopupOnFocus(boolean automaticPopupOnFocus) {
    }

    @Override
    public boolean isAutomaticPopupOnFocus() {
        return false;
    }

    @Override
    public Consumer<String> getNewOptionHandler() {
        return newOptionHandler;
    }

    @Override
    public void setNewOptionHandler(Consumer<String> newOptionHandler) {
        this.newOptionHandler = newOptionHandler;

        if (newOptionHandler != null
                && getComponent().getNewItemHandler() == null) {
            getComponent().setNewItemHandler(this::onNewItemEntered);
        }

        if (newOptionHandler == null
                && getComponent().getNewItemHandler() != null) {
            getComponent().setNewItemHandler(null);
        }
    }

    @Override
    public int getPageLength() {
        return getComponent().getPageLength();
    }

    @Override
    public void setPageLength(int pageLength) {
        getComponent().setPageLength(pageLength);
    }

    @Override
    public boolean isNullOptionVisible() {
        return nullOptionVisible;
    }

    @Override
    public void setNullOptionVisible(boolean nullOptionVisible) {
        this.nullOptionVisible = nullOptionVisible;

        getComponent().setEmptySelectionAllowed(!isRequired() && nullOptionVisible);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOptionIconProvider(Function<? super V, String> optionIconProvider) {
        if (this.optionIconProvider != optionIconProvider) {
            this.optionIconProvider = optionIconProvider;

            if (optionIconProvider != null) {
                getComponent().setItemIconGenerator(this::generateOptionIcon);
            } else {
                getComponent().setItemIconGenerator(NULL_ITEM_ICON_GENERATOR);
            }
        }
    }

    @Override
    public void setOptionIconProvider(Class<V> optionClass, Function<? super V, String> optionIconProvider) {
        setOptionIconProvider(optionIconProvider);
    }

    @Override
    public Function<? super V, String> getOptionIconProvider() {
        return optionIconProvider;
    }

    protected Resource generateOptionIcon(V item) {
        String resourceId;
        try {
            resourceId = optionIconProvider.apply(item);
        } catch (Exception e) {
            LoggerFactory.getLogger(WebLookupPickerField.class)
                    .warn("Error invoking optionIconProvider apply method", e);
            return null;
        }

        return iconResolver.getIconResource(resourceId);
    }

    @Override
    public void setFilterPredicate(FilterPredicate filterPredicate) {
        this.filterPredicate = filterPredicate;
    }

    @Override
    public FilterPredicate getFilterPredicate() {
        return filterPredicate;
    }

    @Override
    public String getPopupWidth() {
        return getComponent().getPopupWidth();
    }

    @Override
    public void setPopupWidth(String width) {
        getComponent().setPopupWidth(width);
    }

    @Override
    public String getInputPrompt() {
        return getComponent().getPlaceholder();
    }

    @Override
    public void setInputPrompt(String inputPrompt) {
        if (StringUtils.isNotBlank(inputPrompt)) {
            setNullOption(null);
        }
        getComponent().setPlaceholder(inputPrompt);
    }

    @Override
    public Options<V> getOptions() {
        return optionsBinding != null ? optionsBinding.getSource() : null;
    }

    @Override
    public void setOptions(Options<V> options) {
        if (this.optionsBinding != null) {
            this.optionsBinding.unbind();
            this.optionsBinding = null;
        }

        if (options != null) {
            OptionsBinder optionsBinder = beanLocator.get(OptionsBinder.NAME);
            this.optionsBinding = optionsBinder.bind(options, this, this::setItemsToPresentation);
            this.optionsBinding.activate();

            if (getMetaClass() == null
                    && options instanceof EntityOptions) {
                setMetaClass(((EntityOptions<V>) options).getEntityMetaClass());
            }
        }
    }

    protected void setItemsToPresentation(Stream<V> options) {
        getComponent().setItems(this::filterItemTest, options.collect(Collectors.toList()));
    }

    protected boolean filterItemTest(String itemCaption, String filterText) {
        if (filterPredicate != null) {
            return filterPredicate.test(itemCaption, filterText);
        }

        if (filterMode == FilterMode.NO) {
            return true;
        }

        if (filterMode == FilterMode.STARTS_WITH) {
            return itemCaption
                    .toLowerCase(locale)
                    .startsWith(filterText.toLowerCase(locale));
        }

        return itemCaption
                .toLowerCase(locale)
                .contains(filterText.toLowerCase(locale));
    }

    @Override
    public void setOptionCaptionProvider(Function<? super V, String> optionCaptionProvider) {
        if (this.optionCaptionProvider != optionCaptionProvider) {
            this.optionCaptionProvider = optionCaptionProvider;

            // reset item captions
            getComponent().setItemCaptionGenerator(this::generateItemCaption);
        }
    }

    @Override
    public void setRefreshOptionsOnLookupClose(boolean refresh) {
        refreshOptionsOnLookupClose = refresh;
    }

    @Override
    public boolean isRefreshOptionsOnLookupClose() {
        return refreshOptionsOnLookupClose;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOptionStyleProvider(Function<? super V, String> optionStyleProvider) {
        if (this.optionStyleProvider != optionStyleProvider) {
            this.optionStyleProvider = optionStyleProvider;

            if (optionStyleProvider != null) {
                getComponent().setStyleGenerator(this::generateItemStylename);
            } else {
                getComponent().setStyleGenerator(NULL_STYLE_GENERATOR);
            }
        }
    }

    @Override
    public Function<? super V, String> getOptionStyleProvider() {
        return optionStyleProvider;
    }

    @Override
    protected void checkValueType(V value) {
        // do not check
    }
}