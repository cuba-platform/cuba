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
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenOptions;

import java.util.function.Function;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * Screen builder that is not aware of concrete screen class. It's {@link #build()} method returns {@link Screen}.
 */
public class ScreenBuilder {

    protected final FrameOwner origin;
    protected final Function<ScreenBuilder, Screen> handler;

    protected Screens.LaunchMode launchMode = OpenMode.THIS_TAB;
    protected ScreenOptions options = FrameOwner.NO_OPTIONS;
    protected String screenId;

    public ScreenBuilder(ScreenBuilder builder) {
        this.origin = builder.origin;
        this.handler = builder.handler;

        this.options = builder.options;
        this.launchMode = builder.launchMode;
        this.screenId = builder.screenId;
    }

    public ScreenBuilder(FrameOwner origin, Function<ScreenBuilder, Screen> handler) {
        this.origin = origin;
        this.handler = handler;
    }

    /**
     * Sets {@link Screens.LaunchMode} for the screen and returns the builder for chaining.
     * <p>For example: {@code builder.withLaunchMode(OpenMode.DIALOG).build();}
     */
    public ScreenBuilder withLaunchMode(Screens.LaunchMode launchMode) {
        checkNotNullArgument(launchMode);

        this.launchMode = launchMode;
        return this;
    }

    /**
     * Sets {@link Screens.LaunchMode} for the screen and returns the builder for chaining.
     * <p>For example: {@code builder.withOpenMode(OpenMode.DIALOG).build();}
     */
    public ScreenBuilder withOpenMode(OpenMode openMode) {
        checkNotNullArgument(openMode);

        this.launchMode = openMode;
        return this;
    }

    /**
     * Sets screen id and returns the builder for chaining.
     *
     * @param screenId identifier of the screen as specified in the {@code UiController} annotation
     *                 or {@code screens.xml}.
     */
    public ScreenBuilder withScreenId(String screenId) {
        this.screenId = screenId;
        return this;
    }

    /**
     * Sets {@link ScreenOptions} for the screen and returns the builder for chaining.
     */
    public ScreenBuilder withOptions(ScreenOptions options) {
        this.options = options;
        return this;
    }

    /**
     * Sets screen class and returns the {@link EditorClassBuilder} for chaining.
     *
     * @param screenClass class of the screen controller
     */
    public <S extends Screen> ScreenClassBuilder<S> withScreenClass(Class<S> screenClass) {
        return new ScreenClassBuilder<>(this, screenClass);
    }

    public FrameOwner getOrigin() {
        return origin;
    }

    /**
     * Returns launch mode set by {@link #withLaunchMode(Screens.LaunchMode)}.
     */
    public Screens.LaunchMode getLaunchMode() {
        return launchMode;
    }

    /**
     * Returns screen options set by {@link #withOptions(ScreenOptions)}.
     */
    public ScreenOptions getOptions() {
        return options;
    }

    /**
     * Returns screen id set by {@link #withScreenId(String)}.
     */
    public String getScreenId() {
        return screenId;
    }

    /**
     * Builds the screen. Screen should be shown using {@link Screen#show()}.
     */
    public Screen build() {
        return handler.apply(this);
    }

    /**
     * Builds and shows the editor screen.
     */
    public Screen show() {
        return handler.apply(this)
                .show();
    }
}