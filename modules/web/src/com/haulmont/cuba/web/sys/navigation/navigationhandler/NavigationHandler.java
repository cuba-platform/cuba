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

package com.haulmont.cuba.web.sys.navigation.navigationhandler;

import com.haulmont.cuba.gui.navigation.NavigationState;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.sys.navigation.ScreenNavigator;
import com.haulmont.cuba.web.sys.navigation.UrlChangeHandler;

/**
 * Classes that implement this interface are intended for handling URL navigation.
 *
 * @see UrlChangeHandler
 * @see ScreenNavigator
 */
public interface NavigationHandler {

    /**
     * Defines the highest precedence for {@link org.springframework.core.Ordered} or
     * {@link org.springframework.core.annotation.Order} navigation handlers.
     */
    int HIGHEST_PLATFORM_PRECEDENCE = 100;

    /**
     * Defines the lowest precedence for {@link org.springframework.core.Ordered} or
     * {@link org.springframework.core.annotation.Order} navigation handlers.
     */
    int LOWEST_PLATFORM_PRECEDENCE = 1000;

    /**
     * Performs navigation corresponding the given {@code requestedState}.
     *
     * @param requestedState requested state represented by {@link NavigationState} instance
     * @param ui             current UI
     *
     * @return true if {@code requestedState} is fully handled by the handler or false if {@code requestedState} should
     *         be passed through handlers chain
     */
    boolean doHandle(NavigationState requestedState, AppUI ui);
}
