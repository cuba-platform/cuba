package com.haulmont.cuba.web.gui.components.valueproviders;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MetadataTools;
import com.vaadin.data.ValueProvider;

public class EntityNameValueProvider<E extends Entity> implements ValueProvider<E, String> {

    protected MetadataTools metadataTools;

    public EntityNameValueProvider(MetadataTools metadataTools) {
        this.metadataTools = metadataTools;
    }

    @Override
    public String apply(E entity) {
        return entity != null
                ? metadataTools.getInstanceName(entity)
                : "";
    }
}
