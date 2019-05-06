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

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.data.DataAwareComponentsTools;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.meta.OptionsBinding;
import com.haulmont.cuba.gui.components.data.options.OptionsBinder;
import com.haulmont.cuba.web.gui.components.util.ShortcutListenerDelegate;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaComboBox;
import com.vaadin.server.Resource;
import com.vaadin.ui.IconGenerator;
import com.vaadin.ui.StyleGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.vaadin.event.ShortcutAction.KeyCode;
import static com.vaadin.event.ShortcutAction.ModifierKey;

public class WebLookupField<V> extends WebV8AbstractField<CubaComboBox<V>, V, V>
        implements LookupField<V>, InitializingBean {

    public static final StyleGenerator NULL_STYLE_GENERATOR = item -> null;
    public static final IconGenerator NULL_ITEM_ICON_GENERATOR = item -> null;

    protected FilterMode filterMode = FilterMode.CONTAINS;

    protected V nullOption;
    protected boolean nullOptionVisible = true;

    protected Consumer<String> newOptionHandler;

    protected Function<? super V, String> optionIconProvider;
    protected Function<? super V, String> optionCaptionProvider;
    protected Function<? super V, String> optionStyleProvider;

    protected FilterPredicate filterPredicate;

    protected MetadataTools metadataTools;
    protected IconResolver iconResolver;

    protected OptionsBinding<V> optionsBinding;

    protected Locale locale;

    public WebLookupField() {
        this.component = createComponent();

        attachValueChangeListener(component);
    }

    protected CubaComboBox<V> createComponent() {
        return new CubaComboBox<>();
    }

    @Inject
    public void setMetadataTools(MetadataTools metadataTools) {
        this.metadataTools = metadataTools;
    }

    @Inject
    public void setIconResolver(IconResolver iconResolver) {
        this.iconResolver = iconResolver;
    }

    protected void handleClearShortcut(@SuppressWarnings("unused") Object sender, @SuppressWarnings("unused") Object target) {
        if (!isRequired()
                && isEnabledRecursive()
                && isEditableWithParent()) {
            setValue(null);
        }
    }

    @Override
    public void afterPropertiesSet() {
        initComponent(component);

        Configuration configuration = beanLocator.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        setPageLength(clientConfig.getLookupFieldPageLength());

        UserSessionSource userSessionSource = beanLocator.get(UserSessionSource.class);

        this.locale = userSessionSource.getLocale();
    }

    protected void initComponent(CubaComboBox<V> component) {
        component.setItemCaptionGenerator(this::generateItemCaption);

        component.addShortcutListener(new ShortcutListenerDelegate("clearShortcut",
                KeyCode.DELETE, new int[]{ModifierKey.SHIFT})
                        .withHandler(this::handleClearShortcut));
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
    protected void valueBindingConnected(ValueSource<V> valueSource) {
        super.valueBindingConnected(valueSource);

        if (valueSource instanceof EntityValueSource) {
            DataAwareComponentsTools dataAwareComponentsTools = beanLocator.get(DataAwareComponentsTools.class);
            dataAwareComponentsTools.setupOptions(this, (EntityValueSource) valueSource);
        }
    }

    @Override
    public V getValue() {
        return internalValue;
    }

    @Override
    public void setFilterMode(FilterMode filterMode) {
        this.filterMode = filterMode;
    }

    @Override
    public FilterMode getFilterMode() {
        return filterMode;
    }

    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);

        component.setEmptySelectionAllowed(!required && nullOptionVisible);
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
        return component.getEmptySelectionCaption();
    }

    @Override
    public void setNullSelectionCaption(String nullOption) {
        component.setEmptySelectionCaption(nullOption);

        setInputPrompt(null);
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
        }
    }

    protected void setItemsToPresentation(Stream<V> options) {
        component.setItems(this::filterItemTest, options.collect(Collectors.toList()));
    }

    @Override
    public void setOptionCaptionProvider(Function<? super V, String> optionCaptionProvider) {
        if (this.optionCaptionProvider != optionCaptionProvider) {
            this.optionCaptionProvider = optionCaptionProvider;

            // reset item captions
            component.setItemCaptionGenerator(this::generateItemCaption);
        }
    }

    @Override
    public Function<? super V, String> getOptionCaptionProvider() {
        return optionCaptionProvider;
    }

    @Override
    public boolean isNewOptionAllowed() {
        return component.getNewItemHandler() != null;
    }

    @Override
    public void setNewOptionAllowed(boolean newItemAllowed) {
        if (newItemAllowed
                && component.getNewItemHandler() == null) {
            component.setNewItemHandler(this::onNewItemEntered);
        }

        if (!newItemAllowed
                && component.getNewItemHandler() != null) {
            component.setNewItemHandler(null);
        }
    }

    protected void onNewItemEntered(String newItemCaption) {
        if (newOptionHandler == null) {
            throw new IllegalStateException("New item handler cannot be NULL");
        }
        newOptionHandler.accept(newItemCaption);
    }

    @Override
    public boolean isTextInputAllowed() {
        return component.isTextInputAllowed();
    }

    @Override
    public void setTextInputAllowed(boolean textInputAllowed) {
        component.setTextInputAllowed(textInputAllowed);
    }

    @Override
    public boolean isAutomaticPopupOnFocus() {
        return false;
    }

    @Override
    public void setAutomaticPopupOnFocus(boolean popup) {}

    @Override
    public Consumer<String> getNewOptionHandler() {
        return newOptionHandler;
    }

    @Override
    public void setNewOptionHandler(Consumer<String> newOptionHandler) {
        this.newOptionHandler = newOptionHandler;

        if (newOptionHandler != null
                && component.getNewItemHandler() == null) {
            component.setNewItemHandler(this::onNewItemEntered);
        }

        if (newOptionHandler == null
                && component.getNewItemHandler() != null) {
            component.setNewItemHandler(null);
        }
    }

    @Override
    public int getPageLength() {
        return component.getPageLength();
    }

    @Override
    public void setPageLength(int pageLength) {
        component.setPageLength(pageLength);
    }

    @Override
    public void setNullOptionVisible(boolean nullOptionVisible) {
        this.nullOptionVisible = nullOptionVisible;

        component.setEmptySelectionAllowed(!isRequired() && nullOptionVisible);
    }

    @Override
    public boolean isNullOptionVisible() {
        return nullOptionVisible;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOptionIconProvider(Function<? super V, String> optionIconProvider) {
        if (this.optionIconProvider != optionIconProvider) {
            this.optionIconProvider = optionIconProvider;

            if (optionIconProvider != null) {
                component.setItemIconGenerator(this::generateOptionIcon);
            } else {
                component.setItemIconGenerator(NULL_ITEM_ICON_GENERATOR);
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
            LoggerFactory.getLogger(WebLookupField.class)
                    .warn("Error invoking optionIconProvider apply method", e);
            return null;
        }

        return iconResolver.getIconResource(resourceId);
    }

    @Override
    public String getInputPrompt() {
        return component.getPlaceholder();
    }

    @Override
    public void setInputPrompt(String inputPrompt) {
        if (StringUtils.isNotBlank(inputPrompt)) {
            setNullOption(null);
        }
        component.setPlaceholder(inputPrompt);
    }

    @Override
    public void setLookupSelectHandler(Consumer selectHandler) {
        // do nothing
    }

    @Override
    public Collection getLookupSelectedItems() {
        return Collections.singleton(getValue());
    }

    @Override
    public void commit() {
        super.commit();
    }

    @Override
    public void discard() {
        super.discard();
    }

    @Override
    public boolean isBuffered() {
        return super.isBuffered();
    }

    @Override
    public void setBuffered(boolean buffered) {
        super.setBuffered(buffered);
    }

    @Override
    public boolean isModified() {
        return super.isModified();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOptionStyleProvider(Function<? super V, String> optionStyleProvider) {
        if (this.optionStyleProvider != optionStyleProvider) {
            this.optionStyleProvider = optionStyleProvider;

            if (optionStyleProvider != null) {
                component.setStyleGenerator(this::generateItemStylename);
            } else {
                component.setStyleGenerator(NULL_STYLE_GENERATOR);
            }
        }
    }

    @Override
    public Function<? super V, String> getOptionStyleProvider() {
        return optionStyleProvider;
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
        return component.getPopupWidth();
    }

    @Override
    public void setPopupWidth(String width) {
        component.setPopupWidth(width);
    }

    @Override
    public void focus() {
        component.focus();
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }
}
