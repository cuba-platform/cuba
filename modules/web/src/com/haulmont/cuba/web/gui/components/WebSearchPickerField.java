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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.OptionsStyleProvider;
import com.haulmont.cuba.gui.components.SearchPickerField;
import com.haulmont.cuba.gui.components.SecuredActionsHolder;
import com.haulmont.cuba.gui.components.data.EntityOptionsSource;
import com.haulmont.cuba.gui.components.data.EntityValueSource;
import com.haulmont.cuba.gui.components.data.OptionsBinding;
import com.haulmont.cuba.gui.components.data.OptionsSource;
import com.haulmont.cuba.gui.components.data.options.OptionsBinder;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.widgets.CubaPickerField;
import com.haulmont.cuba.web.widgets.CubaSearchSelectPickerField;
import com.vaadin.server.Resource;
import com.vaadin.ui.ItemCaptionGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WebSearchPickerField<V extends Entity> extends WebPickerField<V>
        implements SearchPickerField<V>, SecuredActionsHolder {

    protected V nullOption;
    protected boolean nullOptionVisible = true;

    protected FilterMode filterMode = FilterMode.CONTAINS;
    protected FilterPredicate filterPredicate;

    protected NewOptionHandler newOptionHandler;

    protected OptionsStyleProvider optionsStyleProvider;
    protected OptionIconProvider<? super V> optionIconProvider;
    protected Function<? super V, String> optionCaptionProvider;

    protected OptionsBinding<V> optionsBinding;

    protected int minSearchStringLength = 0;
    protected Mode mode = Mode.CASE_SENSITIVE;
    protected boolean escapeValueForLike = false;

    protected Frame.NotificationType defaultNotificationType = Frame.NotificationType.TRAY;

    protected SearchNotifications searchNotifications = createSearchNotifications();

    protected Locale locale;

    public WebSearchPickerField() {
        /* vaadin8
        final ComboBox selectComponent = component;
        WebPickerField.Picker picker = new WebPickerField.Picker(this, component) {
            @Override
            public void setRequired(boolean required) {
                super.setRequired(required);
                selectComponent.setNullSelectionAllowed(!required);
            }
        };*/
    }

    @Override
    protected CubaPickerField<V> createComponent() {
        return new CubaSearchSelectPickerField<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        Configuration configuration = applicationContext.getBean(Configuration.NAME, Configuration.class);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        setPageLength(clientConfig.getLookupFieldPageLength());

        UserSessionSource userSessionSource =
                applicationContext.getBean(UserSessionSource.NAME, UserSessionSource.class);

        this.locale = userSessionSource.getLocale();
    }

    @Override
    protected void initComponent(CubaPickerField<V> component) {
        // FIXME: gg, wait for WebSearchField implementation
//        ((CubaSearchSelectPickerField<V>) component).setFilterHandler(this::executeSearch);

        Messages messages = applicationContext.getBean(Messages.NAME, Messages.class);
        setInputPrompt(messages.getMainMessage("searchPickerField.inputPrompt"));
    }

    protected SearchNotifications createSearchNotifications() {
        return new SearchNotifications() {
            protected Messages messages = AppBeans.get(Messages.NAME);

            @Override
            public void notFoundSuggestions(String filterString) {
                String message = messages.formatMessage("com.haulmont.cuba.gui", "searchSelect.notFound", filterString);
                App.getInstance().getWindowManager().showNotification(message, defaultNotificationType);
            }

            @Override
            public void needMinSearchStringLength(String filterString, int minSearchStringLength) {
                String message = messages.formatMessage(
                        "com.haulmont.cuba.gui", "searchSelect.minimumLengthOfFilter", minSearchStringLength);
                App.getInstance().getWindowManager().showNotification(message, defaultNotificationType);
            }
        };
    }

    @Override
    public CubaSearchSelectPickerField<V> getComponent() {
        //noinspection unchecked
        return (CubaSearchSelectPickerField<V>) super.getComponent();
    }

    @Override
    public void addFieldListener(FieldListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFieldEditable(boolean editable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMinSearchStringLength() {
        return minSearchStringLength;
    }

    @Override
    public void setMinSearchStringLength(int searchStringLength) {
        this.minSearchStringLength = searchStringLength;
    }

    @Override
    public SearchNotifications getSearchNotifications() {
        return searchNotifications;
    }

    @Override
    public void setSearchNotifications(SearchNotifications searchNotifications) {
        this.searchNotifications = searchNotifications;
    }

    @Override
    public Frame.NotificationType getDefaultNotificationType() {
        return defaultNotificationType;
    }

    @Override
    public void setDefaultNotificationType(Frame.NotificationType defaultNotificationType) {
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
    public V getNullOption() {
        return nullOption;
    }

    @Override
    public void setNullOption(V nullOption) {
        this.nullOption = nullOption;

        getComponent().setEmptySelectionCaption(generateItemCaption(nullOption));

        setInputPrompt(null);
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

    @Override
    public FilterMode getFilterMode() {
        return filterMode;
    }

    @Override
    public void setFilterMode(FilterMode mode) {
        this.filterMode = mode;
    }

    @Override
    public boolean isNewOptionAllowed() {
        return false;
        // VAADIN8: gg, implement
//        return component.isNewItemsAllowed();
    }

    @Override
    public void setNewOptionAllowed(boolean newOptionAllowed) {
        // VAADIN8: gg, implement
//        component.setNewItemsAllowed(newItemAllowed);
    }

    @Override
    public boolean isTextInputAllowed() {
        return false;
    }

    @Override
    public void setTextInputAllowed(boolean textInputAllowed) {
        throw new UnsupportedOperationException("Option textInputAllowed is unsupported for Search field");
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
        return getComponent().getPageLength();
    }

    @Override
    public void setPageLength(int pageLength) {
        getComponent().setPageLength(pageLength);
    }

    @Override
    public void setNullOptionVisible(boolean nullOptionVisible) {
        this.nullOptionVisible = nullOptionVisible;

        getComponent().setEmptySelectionAllowed(!isRequired() && nullOptionVisible);
    }

    @Override
    public boolean isNullOptionVisible() {
        return nullOptionVisible;
    }

    @Override
    public void setOptionIconProvider(OptionIconProvider<? super V> optionIconProvider) {
        if (this.optionIconProvider != optionIconProvider) {
            // noinspection unchecked
            this.optionIconProvider = optionIconProvider;

            if (optionIconProvider == null) {
                getComponent().setItemIconGenerator(null);
            } else {
                getComponent().setItemIconGenerator(this::generateOptionIcon);
            }
        }
    }

    @Override
    public void setOptionIconProvider(Class<V> optionClass, OptionIconProvider<V> optionIconProvider) {
        setOptionIconProvider(optionIconProvider);
    }

    @Override
    public OptionIconProvider<? super V> getOptionIconProvider() {
        return optionIconProvider;
    }

    protected Resource generateOptionIcon(V item) {
        String resourceId;
        try {
            // noinspection unchecked
            resourceId = optionIconProvider.getItemIcon(item);
        } catch (Exception e) {
            LoggerFactory.getLogger(WebLookupField.class)
                    .warn("Error invoking OptionIconProvider getItemIcon method", e);
            return null;
        }

        return iconResolver.getIconResource(resourceId);
    }

    @Override
    public void setFilterPredicate(FilterPredicate filterPredicate) {
        this.filterPredicate = filterPredicate;

        if (filterPredicate != null) {
            // VAADIN8: gg, implement
//            component.setFilterPredicate(filterPredicate::test);
        } else {
//            component.setFilterPredicate(null);
        }
    }

    @Override
    public FilterPredicate getFilterPredicate() {
        return filterPredicate;
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
    public void setOptionsStyleProvider(OptionsStyleProvider optionsStyleProvider) {
        this.optionsStyleProvider = optionsStyleProvider;

//        vaadin8
        /*if (optionsStyleProvider != null) {
            component.setItemStyleGenerator((comboBox, item) ->
                    optionsStyleProvider.getItemStyleName(this, item));
        } else {
            component.setItemStyleGenerator(null);
        }*/
    }

    @Override
    public OptionsStyleProvider getOptionsStyleProvider() {
        return optionsStyleProvider;
    }

    @Override
    public OptionsSource<V> getOptionsSource() {
        return optionsBinding != null ? optionsBinding.getSource() : null;
    }

    @Override
    public void setOptionsSource(OptionsSource<V> optionsSource) {
        if (this.optionsBinding != null) {
            this.optionsBinding.unbind();
            this.optionsBinding = null;
        }

        if (optionsSource != null) {
            OptionsBinder optionsBinder = applicationContext.getBean(OptionsBinder.NAME, OptionsBinder.class);
            this.optionsBinding = optionsBinder.bind(optionsSource, this, this::setItemsToPresentation);
            this.optionsBinding.activate();

            if (getMetaClass() == null
                    && optionsSource instanceof EntityOptionsSource) {
                setMetaClass(((EntityOptionsSource<V>) optionsSource).getEntityMetaClass());
            }
        }
    }

    protected void setItemsToPresentation(Stream<V> options) {
        getComponent().setItems(this::filterItemTest, options.collect(Collectors.toList()));
    }

    protected boolean filterItemTest(String itemCaption, String filterText) {
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
    public Function<? super V, String> getOptionCaptionProvider() {
        return optionCaptionProvider;
    }

    @Override
    public void setOptionCaptionProvider(Function<? super V, String> captionProvider) {
        this.optionCaptionProvider = captionProvider;

        getComponent().setItemCaptionGenerator((ItemCaptionGenerator<V>) captionProvider::apply);
    }
}