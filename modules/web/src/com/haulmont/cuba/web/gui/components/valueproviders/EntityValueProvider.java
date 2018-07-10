package com.haulmont.cuba.web.gui.components.valueproviders;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.vaadin.data.ValueProvider;

public class EntityValueProvider<E extends Entity, T> implements ValueProvider<E, T> {

    protected MetaPropertyPath propertyPath;

    public EntityValueProvider(MetaPropertyPath propertyPath) {
        this.propertyPath = propertyPath;
    }

    public MetaPropertyPath getPropertyPath() {
        return propertyPath;
    }

    @Override
    public T apply(E e) {
        return propertyPath != null
                ? e.getValueEx(propertyPath.toPathString())
                : null;
    }
}
