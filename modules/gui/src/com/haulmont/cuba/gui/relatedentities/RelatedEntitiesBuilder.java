/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.relatedentities;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenOptions;

import java.util.Collection;
import java.util.function.Function;

/**
 * Builder class creates screen for related entities.
 * For instance:
 * <pre>{@code
 *      RelatedEntitiesBuilder builder = relatedEntitiesAPI.builder(this);
 *      Screen colourBrowser = builder
 *              .withEntityClass(Car.class)
 *              .withProperty("colour")
 *              .withSelectedEntities(carsTable.getSelected())
 *              .withScreenId("ColourBrowser")
 *              .build();
 *      colourBrowser.show();}
 * </pre>
 */
public class RelatedEntitiesBuilder {

    protected final Function<RelatedEntitiesBuilder, Screen> handler;

    protected String property;
    protected MetaProperty metaProperty;
    protected Class entityClass;
    protected MetaClass metaClass;

    protected Screens.LaunchMode launchMode = OpenMode.THIS_TAB;
    protected String screenId;

    protected FrameOwner origin;
    protected ScreenOptions options = FrameOwner.NO_OPTIONS;
    protected Collection<? extends Entity> selectedEntities;

    protected String filterCaption;

    public RelatedEntitiesBuilder(RelatedEntitiesBuilder builder) {
        this.handler = builder.handler;

        this.property = builder.property;
        this.metaProperty = builder.metaProperty;
        this.entityClass = builder.entityClass;
        this.metaClass = builder.metaClass;

        this.launchMode = builder.launchMode;
        this.screenId = builder.screenId;

        this.origin = builder.origin;
        this.options = builder.options;
        this.selectedEntities = builder.selectedEntities;
    }

    public RelatedEntitiesBuilder(FrameOwner origin, Function<RelatedEntitiesBuilder, Screen> handler) {
        this.handler = handler;
        this.origin = origin;
    }

    /**
     * @return property set by {@link #withProperty(String)}
     */
    public String getProperty() {
        return property;
    }

    /**
     * @return meta property set by {@link #withMetaProperty(MetaProperty)}
     */
    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    /**
     * @return entity class set by {@link #withEntityClass(Class)}
     */
    public Class getEntityClass() {
        return entityClass;
    }

    /**
     * @return launch mode set by {@link #withLaunchMode(Screens.LaunchMode)}
     */
    public Screens.LaunchMode getLaunchMode() {
        return launchMode;
    }

    /**
     * @return screen id set by {@link #withScreenId(String)}
     */
    public String getScreenId() {
        return screenId;
    }

    /**
     * @return invoking screen
     */
    public FrameOwner getOrigin() {
        return origin;
    }

    /**
     * @return screen options set by {@link #withOptions(ScreenOptions)}
     */
    public ScreenOptions getOptions() {
        return options;
    }

    /**
     * @return meta class set by {@link #withMetaClass(MetaClass)}
     */
    public MetaClass getMetaClass() {
        return metaClass;
    }

    /**
     * @return selected entities set by {@link #withSelectedEntities(Collection)}
     */
    public Collection<? extends Entity> getSelectedEntities() {
        return selectedEntities;
    }

    /**
     * @return filter caption set by {@link #withFilterCaption(String)}
     */
    public String getFilterCaption() {
        return filterCaption;
    }

    /**
     * Sets property from which you want to show related entities.
     *
     * @param property property
     * @return current instance of builder
     */
    public RelatedEntitiesBuilder withProperty(String property) {
        this.property = property;
        return this;
    }

    /**
     * Sets MetaProperty from which you want to show related entities.
     *
     * @param metaProperty meta property
     * @return current instance of builder
     */
    public RelatedEntitiesBuilder withMetaProperty(MetaProperty metaProperty) {
        this.metaProperty = metaProperty;
        return this;
    }

    /**
     * Sets class of entity for which you want to see related entities.
     *
     * @param entityClass class
     * @return current instance of builder
     */
    public RelatedEntitiesBuilder withEntityClass(Class entityClass) {
        this.entityClass = entityClass;
        return this;
    }

    /**
     * Sets {@link Screens.LaunchMode} for the related entities screen and returns the builder for chaining.
     * <p>
     * For example: {@code builder.withLaunchMode(OpenMode.DIALOG).build();}
     *
     * @param launchMode launch mode
     * @return current instance of builder
     */
    public RelatedEntitiesBuilder withLaunchMode(Screens.LaunchMode launchMode) {
        this.launchMode = launchMode;
        return this;
    }

    /**
     * Sets {@link OpenMode} for the lookup screen and returns the builder for chaining.
     * <p>
     * For example: {@code builder.withOpenMode(OpenMode.DIALOG).build();}
     *
     * @param openMode open mode
     * @return current instance of builder
     */
    public RelatedEntitiesBuilder withOpenMode(OpenMode openMode) {
        this.launchMode = openMode;
        return this;
    }

    /**
     * Sets screen id and returns the builder for chaining.
     *
     * @param screenId identifier of the screen
     * @return current instance of builder
     */
    public RelatedEntitiesBuilder withScreenId(String screenId) {
        this.screenId = screenId;
        return this;
    }

    /**
     * Sets screen class and returns the {@link RelatedEntitiesClassBuilder} for chaining.
     *
     * @param screenClass class of the screen controller
     * @return RelatedEntitiesClassBuilder with copied fields
     */
    public <S extends Screen> RelatedEntitiesClassBuilder<S> withScreenClass(Class<S> screenClass) {
        return new RelatedEntitiesClassBuilder<>(this, screenClass);
    }

    /**
     * Sets {@link ScreenOptions} for the lookup screen and returns the builder for chaining.
     *
     * @param options screen options
     * @return current instance of builder
     */
    public RelatedEntitiesBuilder withOptions(ScreenOptions options) {
        this.options = options;
        return this;
    }

    /**
     * Sets MetaClass of entity for which you want to see related entities.
     *
     * @param metaClass meta class
     * @return current instance of builder
     */
    public RelatedEntitiesBuilder withMetaClass(MetaClass metaClass) {
        this.metaClass = metaClass;
        return this;
    }

    /**
     * Sets collection of selected items.
     *
     * @param selectedEntities selected entities
     * @return current instance of builder
     */
    public RelatedEntitiesBuilder withSelectedEntities(Collection<? extends Entity> selectedEntities) {
        this.selectedEntities = selectedEntities;
        return this;
    }

    /**
     * Sets caption to filter in opened screen.
     *
     * @param filterCaption caption
     * @return current instance of builder
     */
    public RelatedEntitiesBuilder withFilterCaption(String filterCaption) {
        this.filterCaption = filterCaption;
        return this;
    }

    /**
     * Builds the screen.
     *
     * @return created screen
     */
    public Screen build() {
        return handler.apply(this);
    }
}
