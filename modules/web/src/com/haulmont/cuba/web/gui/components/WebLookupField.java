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

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.OptionsStyleProvider;
import com.haulmont.cuba.gui.components.data.EntityValueSource;
import com.haulmont.cuba.gui.components.data.OptionsBinding;
import com.haulmont.cuba.gui.components.data.OptionsSource;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.options.EnumOptions;
import com.haulmont.cuba.gui.components.data.options.OptionsBinder;
import com.haulmont.cuba.web.gui.components.util.ShortcutListenerDelegate;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CComboBox;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Resource;
import com.vaadin.ui.ItemCaptionGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.vaadin.event.ShortcutAction.KeyCode;
import static com.vaadin.event.ShortcutAction.ModifierKey;

public class WebLookupField<V> extends WebV8AbstractField<CComboBox<V>, V, V>
        implements LookupField<V>, InitializingBean {

    protected FilterMode filterMode = FilterMode.CONTAINS;

    protected V nullOption;
    protected boolean nullOptionVisible = true;

    protected NewOptionHandler newOptionHandler;

    protected ComponentErrorHandler componentErrorHandler;

    protected OptionsStyleProvider optionsStyleProvider;
    protected OptionIconProvider<? super V> optionIconProvider;
    protected Function<? super V, String> optionCaptionProvider;
    protected FilterPredicate filterPredicate;

    @Inject
    protected MetadataTools metadataTools;
    @Inject
    protected IconResolver iconResolver;

    protected OptionsBinding<V> optionsBinding;

    protected Locale locale;

    public WebLookupField() {
        this.component = createComponent();

        attachValueChangeListener(component);
        setNewOptionAllowed(false);

        component.setItemCaptionGenerator(this::generateDefaultItemCaption);

        /* vaadin8 move to new item handler
        component.setNewItemHandler(newItemCaption -> {
            if (newOptionHandler == null) {
                throw new IllegalStateException("New item handler cannot be NULL");
            }
            newOptionHandler.addNewOption(newItemCaption);
        });
        */

        component.addShortcutListener(
                new ShortcutListenerDelegate("clearShortcut", KeyCode.DELETE, new int[]{ModifierKey.SHIFT})
                        .withHandler((sender, target) -> {
                            if (!isRequired()
                                    && isEnabledRecursive()
                                    && isEditableWithParent()) {
                                setValue(null);
                            }
                        }));
    }

    protected CComboBox<V> createComponent() {
        return new CComboBox<>();
    }

    @Override
    public void afterPropertiesSet() {
        Configuration configuration = applicationContext.getBean(Configuration.NAME, Configuration.class);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        setPageLength(clientConfig.getLookupFieldPageLength());

        UserSessionSource userSessionSource =
                applicationContext.getBean(UserSessionSource.NAME, UserSessionSource.class);

        this.locale = userSessionSource.getLocale();
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
    protected void valueBindingConnected(ValueSource<V> valueSource) {
        super.valueBindingConnected(valueSource);

        if (valueSource instanceof EntityValueSource) {
            MetaPropertyPath propertyPath = ((EntityValueSource) valueSource).getMetaPropertyPath();
            MetaProperty metaProperty = propertyPath.getMetaProperty();

            if (metaProperty.getRange().isEnum()) {
                //noinspection unchecked
                setOptionsSource(new EnumOptions(metaProperty.getJavaType()));
            }

            // vaadin8 dynamic attributes
            /*
            if (DynamicAttributesUtils.isDynamicAttribute(metaProperty)) {
                CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(metaProperty);
                if (categoryAttribute != null && categoryAttribute.getDataType() == PropertyType.ENUMERATION) {
                    setOptionsMap(categoryAttribute.getLocalizedEnumerationMap());
                }
            }*/
        }
    }

    // vaadin8
    /*protected void createComponent() {
        this.component = new CubaComboBox() {
            @Override
            public void setPropertyDataSource(Property newDataSource) {
                if (newDataSource == null)
                    super.setPropertyDataSource(null);
                else
                    super.setPropertyDataSource(new LookupPropertyAdapter(newDataSource));
            }

            @Override
            public void setComponentError(ErrorMessage componentError) {
                boolean handled = false;
                if (componentErrorHandler != null)
                    handled = componentErrorHandler.handleError(componentError);

                if (!handled)
                    super.setComponentError(componentError);
            }
        };
    }*/

    @SuppressWarnings("unchecked")
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

        component.setEmptySelectionCaption(generateItemCaption(nullOption));

        setInputPrompt(null);
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
//        vaadin8
//        this.captionProperty = captionProperty;
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
        }
    }

    protected void setItemsToPresentation(Stream<V> options) {
        component.setItems(this::filterItemTest, options.collect(Collectors.toList()));
    }

    @Override
    public void setOptionCaptionProvider(Function<? super V, String> captionProvider) {
        this.optionCaptionProvider = captionProvider;

        component.setItemCaptionGenerator((ItemCaptionGenerator<V>) captionProvider::apply);
    }

    @Override
    public Function<? super V, String> getOptionCaptionProvider() {
        return optionCaptionProvider;
    }

    @Override
    public CaptionMode getCaptionMode() {
        // vaadin8
        return CaptionMode.ITEM;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
//        vaadin8
//        super.setCaptionMode(captionMode);
    }

    @Override
    public String getCaptionProperty() {
        return null;
    }

    @Override
    public boolean isNewOptionAllowed() {
        return false;
//        vaadin8 | the same for the WebLookupPickerField
//        return component.isNewItemsAllowed();
    }

    @Override
    public void setNewOptionAllowed(boolean newItemAllowed) {
//        vaadin8 | the same for the WebLookupPickerField
//        component.setNewItemsAllowed(newItemAllowed);
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
    public NewOptionHandler getNewOptionHandler() {
        return newOptionHandler;
    }

    @Override
    public void setNewOptionHandler(NewOptionHandler newItemHandler) {
        this.newOptionHandler = newItemHandler;
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
    public void setOptionIconProvider(OptionIconProvider<? super V> optionIconProvider) {
        if (this.optionIconProvider != optionIconProvider) {
            // noinspection unchecked
            this.optionIconProvider = optionIconProvider;

            if (optionIconProvider == null) {
                component.setItemIconGenerator(null);
            } else {
                component.setItemIconGenerator(this::generateOptionIcon);
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
    public void setLookupSelectHandler(Runnable selectHandler) {
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

    @Override
    public void setOptionsStyleProvider(OptionsStyleProvider optionsStyleProvider) {
        this.optionsStyleProvider = optionsStyleProvider;

//        vaadin8 | the same for the WebLookupPickerField, WebSearchPickerField
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
    public void setFilterPredicate(FilterPredicate filterPredicate) {
        this.filterPredicate = filterPredicate;

        if (filterPredicate != null) {
//            vaadin8 | the same for the WebLookupPickerField, WebSearchPickerField
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

    // vaadin8 replace with Consumer<ErrorMessage>
    protected interface ComponentErrorHandler {
        boolean handleError(ErrorMessage message);
    }
}