/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.builders.LookupBuilder;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.LookupScreenFacet;
import com.haulmont.cuba.gui.screen.LookupScreen;
import com.haulmont.cuba.gui.screen.Screen;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public class WebLookupScreenFacet<E extends Entity, S extends Screen & LookupScreen<E>>
        extends WebAbstractEntityAwareScreenFacet<E, S>
        implements LookupScreenFacet<E, S> {

    protected Consumer<Collection<E>> selectHandler;
    protected Predicate<LookupScreen.ValidationContext<E>> selectValidator;
    protected Function<Collection<E>, Collection<E>> transformation;

    @Override
    public void setSelectHandler(Consumer<Collection<E>> selectHandler) {
        this.selectHandler = selectHandler;
    }

    @Override
    public Consumer<Collection<E>> getSelectHandler() {
        return selectHandler;
    }

    @Override
    public void setSelectValidator(Predicate<LookupScreen.ValidationContext<E>> selectValidator) {
        this.selectValidator = selectValidator;
    }

    @Override
    public Predicate<LookupScreen.ValidationContext<E>> getSelectValidator() {
        return selectValidator;
    }

    @Override
    public void setTransformation(Function<Collection<E>, Collection<E>> transformation) {
        this.transformation = transformation;
    }

    @Override
    public Function<Collection<E>, Collection<E>> getTransformation() {
        return transformation;
    }

    @Override
    public S create() {
        Frame owner = getOwner();
        if (owner == null) {
            throw new IllegalStateException("Screen facet is not attached to Frame");
        }

        LookupBuilder<E> lookupBuilder = createLookupBuilder(owner);

        screen = createScreen(lookupBuilder);

        initScreenListeners(screen);
        injectScreenProperties(screen, properties);

        return screen;
    }

    @Override
    public S show() {
        return (S) create().show();
    }

    protected S createScreen(LookupBuilder<E> builder) {
        return (S) builder
                .withField(pickerField)
                .withListComponent(listComponent)
                .withContainer(container)
                .withScreenId(screenId)
                .withLaunchMode(launchMode)
                .withOptions(getScreenOptions())
                .withSelectValidator(selectValidator)
                .withSelectHandler(selectHandler)
                .withTransformation(transformation)
                .build();
    }

    protected LookupBuilder<E> createLookupBuilder(Frame owner) {
        LookupBuilder<E> builder;

        if (beanLocator == null) {
            throw new IllegalStateException("Unable to create LookupScreenFacet. BeanLocator is null");
        }
        ScreenBuilders screenBuilders = beanLocator.get(ScreenBuilders.class);

        if (entityClass != null) {
            builder = screenBuilders.lookup(entityClass, owner.getFrameOwner());
        } else if (listComponent != null) {
            builder = screenBuilders.lookup(listComponent);
        } else if (pickerField != null) {
            builder = screenBuilders.lookup(pickerField);
        } else {
            throw new IllegalStateException(
                    "Unable to create EditorScreen Facet. At least one of entityClass, listComponent or field must be specified");
        }

        if (screenClass != null) {
            builder.withScreenClass(screenClass);
        }

        return builder;
    }
}
