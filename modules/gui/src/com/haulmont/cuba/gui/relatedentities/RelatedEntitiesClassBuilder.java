/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.relatedentities;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenOptions;

import java.util.Collection;

/**
 * Related entities screen builder that knows the concrete screen class. It's {@link #build()} method returns that class.
 *
 * @param <S> controller class that extends {@link Screen}
 */
public class RelatedEntitiesClassBuilder<S extends Screen> extends RelatedEntitiesBuilder {

    protected Class<S> screenClass;

    public RelatedEntitiesClassBuilder(RelatedEntitiesBuilder builder, Class<S> screenClass) {
        super(builder);

        this.screenClass = screenClass;
    }

    @Override
    public RelatedEntitiesClassBuilder<S> withProperty(String property) {
        super.withProperty(property);
        return this;
    }

    @Override
    public RelatedEntitiesClassBuilder<S> withMetaProperty(MetaProperty metaProperty) {
        super.withMetaProperty(metaProperty);
        return this;
    }

    @Override
    public RelatedEntitiesClassBuilder<S> withEntityClass(Class entityClass) {
        super.withEntityClass(entityClass);
        return this;
    }

    @Override
    public RelatedEntitiesClassBuilder<S> withLaunchMode(Screens.LaunchMode launchMode) {
        super.withLaunchMode(launchMode);
        return this;
    }

    @Override
    public RelatedEntitiesClassBuilder<S> withOpenMode(OpenMode openMode) {
        super.withOpenMode(openMode);
        return this;
    }

    @Override
    public RelatedEntitiesClassBuilder<S> withScreenId(String screenId) {
        throw new IllegalStateException("RelatedEntitiesClassBuilder does not support screenId");
    }

    @Override
    public RelatedEntitiesClassBuilder<S> withOptions(ScreenOptions options) {
        super.withOptions(options);
        return this;
    }

    @Override
    public RelatedEntitiesClassBuilder<S> withMetaClass(MetaClass metaClass) {
        super.withMetaClass(metaClass);
        return this;
    }

    @Override
    public RelatedEntitiesClassBuilder<S> withSelectedEntities(Collection<? extends Entity> selectedEntities) {
        super.withSelectedEntities(selectedEntities);
        return this;
    }

    @Override
    public RelatedEntitiesClassBuilder<S> withFilterCaption(String filterCaption) {
        super.withFilterCaption(filterCaption);
        return this;
    }

    /**
     * @return screen class
     */
    public Class<S> getScreenClass() {
        return screenClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public S build() {
        return (S) this.handler.apply(this);
    }
}
