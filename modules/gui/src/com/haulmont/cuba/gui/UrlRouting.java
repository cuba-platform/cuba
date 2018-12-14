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

package com.haulmont.cuba.gui;

import com.haulmont.cuba.gui.navigation.NavigationState;
import com.haulmont.cuba.gui.screen.Screen;

import java.util.Collections;
import java.util.Map;

/**
 * Interface defining methods for reflecting app state to URL based on currently opened screen.
 * <br>
 * Passed params map enables to reflect inner screen state to URL to use it later.
 * <br>
 * Can be used only Web client.
 * <br>
 * Usage example (this - Screen controller):
 * <pre>
 *     &#64;Inject
 *     private UrlRouting urlRouting;
 *
 *     private void changeUrl() {
 *         Map&lt;String, String&gt; params = ParamsMap.of(
 *                 "param1", "value1",
 *                 "param2", "value2");
 *
 *         urlRouting.pushState(this, params);
 *     }
 * </pre>
 */
public interface UrlRouting {

    /**
     * Pushes the state corresponding to the given {@code screen}.
     * <p>
     * Creates new entry in browser history.
     *
     * @param screen screen that is used to build new navigation state
     */
    default void pushState(Screen screen) {
        pushState(screen, Collections.emptyMap());
    }

    /**
     * Pushes the state corresponding to the given {@code screen}.
     * <p>
     * The given {@code urlParams} will be reflected in URI as GET request params.
     * <p>
     * Creates new entry in browser history.
     *
     * @param screen    screen that is used to build new navigation state
     * @param urlParams URI params map
     */
    void pushState(Screen screen, Map<String, String> urlParams);

    /**
     * Replaces current state by the state corresponding to the given {@code screen}.
     * <p>
     * Doesn't create new entry in browser history.
     *
     * @param screen screen that is used to build new navigation state
     */
    default void replaceState(Screen screen) {
        replaceState(screen, Collections.emptyMap());
    }

    /**
     * Replaces current state by the state corresponding to the given {@code screen}.
     * <p>
     * The given {@code urlParams} will be reflected in URI as GET request params.
     * <p>
     * Doesn't create new entry in browser history.
     *
     * @param screen    screen that is used to build new navigation state
     * @param urlParams URI params map
     */
    void replaceState(Screen screen, Map<String, String> urlParams);

    /**
     * @return current state parsed from URI fragment.
     */
    NavigationState getState();
}
