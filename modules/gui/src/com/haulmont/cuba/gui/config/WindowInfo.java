/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */
package com.haulmont.cuba.gui.config;

import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import org.dom4j.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * Screen's registration information.
 *
 * @see WindowConfig
 */
public class WindowInfo {

    private final String id;

    private final WindowAttributesProvider windowAttributesProvider;

    private final Element descriptor;
    private final String screenClassName;

    public WindowInfo(String id, WindowAttributesProvider windowAttributesProvider, Element descriptor) {
        checkNotNullArgument(id);
        checkNotNullArgument(descriptor);

        this.id = id;
        this.windowAttributesProvider = windowAttributesProvider;
        this.descriptor = descriptor;
        this.screenClassName = null;
    }

    public WindowInfo(String id, WindowAttributesProvider windowAttributesProvider,
                      String screenClassName) {
        checkNotNullArgument(id);
        checkNotNullArgument(screenClassName);

        this.id = id;
        this.windowAttributesProvider = windowAttributesProvider;
        this.screenClassName = screenClassName;
        this.descriptor = null;
    }

    /**
     * Screen ID as set in <code>screens.xml</code>
     */
    public String getId() {
        return id;
    }

    /**
     * JavaDoc
     */
    public Type getType() {
        return windowAttributesProvider.getType(this);
    }

    @Nonnull
    public Class<? extends FrameOwner> getControllerClass() {
        return windowAttributesProvider.getControllerClass(this);
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Screen> asScreen() {
        Class<? extends FrameOwner> controllerClass = getControllerClass();
        if (!Screen.class.isAssignableFrom(controllerClass)) {
            throw new IllegalStateException("WindowInfo is not Screen - " + this.toString());
        }

        return (Class<? extends Screen>) controllerClass;
    }

    @SuppressWarnings("unchecked")
    public Class<? extends ScreenFragment> asFragment() {
        Class<? extends FrameOwner> controllerClass = getControllerClass();
        if (!ScreenFragment.class.isAssignableFrom(controllerClass)) {
            throw new IllegalStateException("WindowInfo is not ScreenFragment - " + this.toString());
        }

        return (Class<? extends ScreenFragment>) controllerClass;
    }

    /**
     * The whole XML element of the screen as set in <code>screens.xml</code>
     */
    @Nullable
    public Element getDescriptor() {
        return descriptor;
    }

    /**
     * Screen class as set in <code>screens.xml</code>
     *
     * JavaDoc
     */
    @Nullable
    public String getControllerClassName() {
        return screenClassName;
    }

    /**
     * Screen template path as set in <code>screens.xml</code>
     *
     * JavaDoc
     */
    @Nullable
    public String getTemplate() {
        return windowAttributesProvider.getTemplate(this);
    }

    /**
     * JavaDoc
     */
    public boolean getMultipleOpen() {
        return windowAttributesProvider.isMultiOpen(this);
    }

    @Override
    public String toString() {
        String template = getTemplate();
        return "WindowInfo{" +
                "id='" + id + '\'' +
                (template != null ? ", template=" + template : "") +
                (screenClassName != null ? ", screenClass=" + screenClassName : "") +
                "}";
    }

    /**
     * Type of registered controller.
     */
    public enum Type {
        SCREEN,
        FRAGMENT
    }
}