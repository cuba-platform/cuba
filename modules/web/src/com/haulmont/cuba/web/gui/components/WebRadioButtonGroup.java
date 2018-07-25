package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.RadioButtonGroup;
import com.haulmont.cuba.gui.components.data.EntityValueSource;
import com.haulmont.cuba.gui.components.data.OptionsBinding;
import com.haulmont.cuba.gui.components.data.OptionsSource;
import com.haulmont.cuba.gui.components.data.options.OptionsBinder;
import com.haulmont.cuba.web.widgets.CubaRadioButtonGroup;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WebRadioButtonGroup<V> extends WebV8AbstractField<CubaRadioButtonGroup<V>, V, V>
        implements RadioButtonGroup<V>, InitializingBean {

    /* Beans */
    protected MetadataTools metadataTools;

    protected OptionsBinding<V> optionsBinding;

    protected Function<? super V, String> optionCaptionProvider;

    public WebRadioButtonGroup() {
        component = createComponent();

        attachValueChangeListener(component);
    }

    private CubaRadioButtonGroup<V> createComponent() {
        return new CubaRadioButtonGroup<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initComponent(component);
    }

    protected void initComponent(CubaRadioButtonGroup<V> component) {
        component.setItemCaptionGenerator(this::generateItemCaption);
    }

    protected String generateItemCaption(V item) {
        if (item == null) {
            return null;
        }

        if (optionCaptionProvider != null) {
            return optionCaptionProvider.apply(item);
        }

        return generateDefaultItemCaption(item);
    }

    // TODO: gg, refactor, extract to some value provider?
    protected String generateDefaultItemCaption(V item) {
        if (valueBinding != null && valueBinding.getSource() instanceof EntityValueSource) {
            EntityValueSource entityValueSource = (EntityValueSource) valueBinding.getSource();
            return metadataTools.format(item, entityValueSource.getMetaPropertyPath().getMetaProperty());
        }

        return metadataTools.format(item);
    }

    @Inject
    public void setMetadataTools(MetadataTools metadataTools) {
        this.metadataTools = metadataTools;
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

    @Override
    public Orientation getOrientation() {
        return WebWrapperUtils.convertToOrientation(component.getOrientation());
    }

    @Override
    public void setOrientation(Orientation orientation) {
        component.setOrientation(WebWrapperUtils.convertToVaadinOrientation(orientation));
    }

    @Override
    public void setLookupSelectHandler(Runnable selectHandler) {
        // do nothing
    }

    @Override
    public Collection getLookupSelectedItems() {
        V value = getValue();
        return value != null
                ? Collections.singletonList(value)
                : Collections.emptyList();
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
        component.setItems(options.collect(Collectors.toList()));
    }

    @Override
    public void setOptionCaptionProvider(Function<? super V, String> captionProvider) {
        this.optionCaptionProvider = captionProvider;
    }

    @Override
    public Function<? super V, String> getOptionCaptionProvider() {
        return optionCaptionProvider;
    }

    @Override
    public CaptionMode getCaptionMode() {
        // VAADIN8: gg, implement
        return CaptionMode.ITEM;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        // VAADIN8: gg, implement
    }

    @Override
    public String getCaptionProperty() {
        // VAADIN8: gg, implement
        return null;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        // VAADIN8: gg, implement
    }
}
