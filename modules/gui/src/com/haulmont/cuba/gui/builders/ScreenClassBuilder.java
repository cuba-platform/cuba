/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.builders;

import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenOptions;

import java.util.function.Consumer;

/**
 * Screen builder that knows the concrete screen class. It's {@link #build()} method returns that class.
 */
public class ScreenClassBuilder<S extends Screen> extends ScreenBuilder {

    protected Class<S> screenClass;
    protected Consumer<AfterScreenCloseEvent<S>> closeListener;

    public ScreenClassBuilder(ScreenBuilder builder, Class<S> screenClass) {
        super(builder);

        this.screenClass = screenClass;
    }

    @Override
    public ScreenClassBuilder<S> withLaunchMode(Screens.LaunchMode launchMode) {
        super.withLaunchMode(launchMode);
        return this;
    }

    @Override
    public ScreenClassBuilder<S> withOpenMode(OpenMode openMode) {
        super.withOpenMode(openMode);
        return this;
    }

    @Override
    public ScreenClassBuilder<S> withOptions(ScreenOptions options) {
        super.withOptions(options);
        return this;
    }

    @Override
    public ScreenClassBuilder<S> withScreenId(String screenId) {
        throw new IllegalStateException("ScreenClassBuilder does not support screenId");
    }

    /**
     * Adds {@link Screen.AfterCloseEvent} listener to the screen.
     *
     * @param listener listener
     */
    public ScreenClassBuilder<S> withAfterCloseListener(Consumer<AfterScreenCloseEvent<S>> listener) {
        this.closeListener = listener;
        return this;
    }

    /**
     * Returns screen class.
     */
    public Class<S> getScreenClass() {
        return screenClass;
    }

    /**
     * Returns screen close listener.
     */
    public Consumer<AfterScreenCloseEvent<S>> getCloseListener() {
        return closeListener;
    }

    @Override
    public S build() {
        return (S) super.build();
    }

    @Override
    public S show() {
        return (S) super.show();
    }
}