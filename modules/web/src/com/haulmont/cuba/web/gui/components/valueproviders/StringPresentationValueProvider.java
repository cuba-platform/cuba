package com.haulmont.cuba.web.gui.components.valueproviders;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.vaadin.data.ValueProvider;

public class StringPresentationValueProvider<T> implements ValueProvider<T, String> {

    protected MetaProperty metaProperty;
    protected MetadataTools metadataTools;

    public StringPresentationValueProvider(MetaProperty metaProperty, MetadataTools metadataTools) {
        this.metaProperty = metaProperty;
        this.metadataTools = metadataTools;
    }

    @Override
    public String apply(T value) {
        return metaProperty != null
                ? metadataTools.format(value, metaProperty)
                : metadataTools.format(value);
    }

    public MetaProperty getMetaProperty() {
        return metaProperty;
    }
}
