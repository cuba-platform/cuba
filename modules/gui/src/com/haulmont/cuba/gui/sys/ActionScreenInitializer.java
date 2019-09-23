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

package com.haulmont.cuba.gui.sys;

import com.haulmont.cuba.gui.builders.EditorBuilder;
import com.haulmont.cuba.gui.builders.LookupBuilder;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenOptions;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Used in actions that open screens ({@code EditAction}, etc.) to initialize a screen builder.
 */
public class ActionScreenInitializer {

    protected OpenMode openMode;
    protected String screenId;
    protected Class screenClass;
    protected Supplier<ScreenOptions> screenOptionsSupplier;
    protected Consumer<Screen> screenConfigurer;
    protected Consumer<Screen.AfterCloseEvent> afterCloseHandler;

    public OpenMode getOpenMode() {
        return openMode;
    }

    public String getScreenId() {
        return screenId;
    }

    public Class getScreenClass() {
        return screenClass;
    }

    public void setOpenMode(OpenMode openMode) {
        this.openMode = openMode;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public void setScreenClass(Class screenClass) {
        this.screenClass = screenClass;
    }

    public void setScreenOptionsSupplier(Supplier<ScreenOptions> screenOptionsSupplier) {
        this.screenOptionsSupplier = screenOptionsSupplier;
    }

    public void setScreenConfigurer(Consumer<Screen> screenConfigurer) {
        this.screenConfigurer = screenConfigurer;
    }

    public void setAfterCloseHandler(Consumer<Screen.AfterCloseEvent> afterCloseHandler) {
        this.afterCloseHandler = afterCloseHandler;
    }

    public EditorBuilder initBuilder(EditorBuilder builder) {
        if (screenClass != null) {
            builder = builder.withScreenClass(screenClass);
        }

        if (screenId != null) {
            builder = builder.withScreenId(screenId);
        }

        if (screenOptionsSupplier != null) {
            ScreenOptions screenOptions = screenOptionsSupplier.get();
            builder = builder.withOptions(screenOptions);
        }

        if (openMode != null) {
            builder = builder.withOpenMode(openMode);
        }

        return builder;
    }

    public LookupBuilder initBuilder(LookupBuilder builder) {
        if (screenClass != null) {
            builder = builder.withScreenClass(screenClass);
        }

        if (screenId != null) {
            builder = builder.withScreenId(screenId);
        }

        if (screenOptionsSupplier != null) {
            ScreenOptions screenOptions = screenOptionsSupplier.get();
            builder = builder.withOptions(screenOptions);
        }

        if (openMode != null) {
            builder = builder.withOpenMode(openMode);
        }

        return builder;
    }

    public void initScreen(Screen screen) {
        if (afterCloseHandler != null) {
            screen.addAfterCloseListener(afterCloseHandler);
        }

        if (screenConfigurer != null) {
            screenConfigurer.accept(screen);
        }
    }
}
