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

package com.haulmont.cuba.gui.screen;

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.core.global.BeanLocator;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * Extensions API for {@link Screen} and {@link ScreenFragment}.
 */
public final class Extensions {

    private Extensions() {
    }

    /**
     * Register extension class in screen.
     *
     * @param frameOwner     screen or screen fragment
     * @param extensionClass class of the extension
     * @param extension      extension
     * @param <T>            type of the extension
     */
    public static <T> void register(FrameOwner frameOwner, Class<T> extensionClass, T extension) {
        checkNotNullArgument(frameOwner);
        checkNotNullArgument(extensionClass);
        checkNotNullArgument(extension);

        if (frameOwner instanceof Screen) {
            Screen screen = (Screen) frameOwner;

            Map<Class<?>, Object> extensions = screen.getExtensions();
            if (extensions == null) {
                screen.setExtensions(ImmutableMap.of(extensionClass, extension));
            } else {
                Map<Class<?>, Object> newExtensions = ImmutableMap.<Class<?>, Object>builder()
                        .putAll(extensions)
                        .put(extensionClass, extension)
                        .build();
                screen.setExtensions(newExtensions);
            }
        } else if (frameOwner instanceof ScreenFragment) {
            ScreenFragment screen = (ScreenFragment) frameOwner;

            Map<Class<?>, Object> extensions = screen.getExtensions();
            if (extensions == null) {
                screen.setExtensions(ImmutableMap.of(extensionClass, extension));
            } else {
                Map<Class<?>, Object> newExtensions = ImmutableMap.<Class<?>, Object>builder()
                        .putAll(extensions)
                        .put(extensionClass, extension)
                        .build();
                screen.setExtensions(newExtensions);
            }
        }
    }

    /**
     * Get extension instance.
     *
     * @param frameOwner     screen or screen fragment
     * @param extensionClass class of the extension
     * @param <T>            type of extension
     * @return extension
     * @throws IllegalStateException in case extension class is not registered
     */
    @Nonnull
    public static <T> T get(FrameOwner frameOwner, Class<T> extensionClass) {
        Optional<T> optional = getOptional(frameOwner, extensionClass);
        if (!optional.isPresent()) {
            throw new IllegalStateException("There is no extension with class: " + extensionClass);
        }

        return optional.get();
    }

    /**
     * Get optional extension instance.
     *
     * @param frameOwner     screen or screen fragment
     * @param extensionClass class of the extension
     * @param <T>            type of extension
     * @return optional extension
     */
    public static <T> Optional<T> getOptional(FrameOwner frameOwner, Class<T> extensionClass) {
        checkNotNullArgument(frameOwner);
        checkNotNullArgument(extensionClass);

        if (frameOwner instanceof Screen) {
            Screen screen = (Screen) frameOwner;

            Map<Class<?>, Object> extensions = screen.getExtensions();
            if (extensions != null) {
                return Optional.ofNullable(extensionClass.cast(extensions.get(extensionClass)));
            }
        } else if (frameOwner instanceof ScreenFragment) {
            ScreenFragment screen = (ScreenFragment) frameOwner;

            Map<Class<?>, Object> extensions = screen.getExtensions();
            if (extensions != null) {
                return Optional.ofNullable(extensionClass.cast(extensions.get(extensionClass)));
            }
        }

        return Optional.empty();
    }

    /**
     * Remove extension instance.
     *
     * @param frameOwner     screen or screen fragment
     * @param extensionClass class of the extension
     */
    public static void remove(FrameOwner frameOwner, Class<?> extensionClass) {
        checkNotNullArgument(frameOwner);
        checkNotNullArgument(extensionClass);

        if (frameOwner instanceof Screen) {
            Screen screen = (Screen) frameOwner;

            Map<Class<?>, Object> extensions = screen.getExtensions();
            if (extensions != null && extensions.containsKey(extensionClass)) {
                Map<Class<?>, Object> newExtensions = new HashMap<>(extensions);
                newExtensions.remove(extensionClass);
                screen.setExtensions(ImmutableMap.copyOf(newExtensions));
            }
        } else if (frameOwner instanceof ScreenFragment) {
            ScreenFragment screen = (ScreenFragment) frameOwner;

            Map<Class<?>, Object> extensions = screen.getExtensions();
            if (extensions != null && extensions.containsKey(extensionClass)) {
                Map<Class<?>, Object> newExtensions = new HashMap<>(extensions);
                newExtensions.remove(extensionClass);
                screen.setExtensions(ImmutableMap.copyOf(newExtensions));
            }
        }
    }

    /**
     * Gets bean locator associated with the frame owner. Extensions can get various application beans from bean locator.
     * <br>
     * Example:
     * <pre>{@code
     *    BeanLocator beanLocator = Extensions.getBeanLocator(screen);
     *    Messages messages = beanLocator.get(Messages.class);
     * }</pre>
     *
     * @param frameOwner UI controller
     * @return bean locator
     */
    public static BeanLocator getBeanLocator(FrameOwner frameOwner) {
        if (frameOwner instanceof Screen) {
            return ((Screen) frameOwner).getBeanLocator();
        } else if (frameOwner instanceof ScreenFragment) {
            return ((ScreenFragment) frameOwner).getBeanLocator();
        }

        throw new IllegalArgumentException("Unsupported type of screen " + frameOwner);
    }
}