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

import com.haulmont.cuba.core.entity.Entity;
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

    /**
     * @return {@link RouteGenerator} instance
     */
    RouteGenerator getRouteGenerator();

    /**
     * Provides API to generate routes for screens with optional URL parameters.
     */
    interface RouteGenerator {

        /**
         * Generates route for screen with the given {@code screenId}.
         *
         * @param screenId screen id
         *
         * @return generated route
         */
        default String getRoute(String screenId) {
            return getRoute(screenId, Collections.emptyMap());
        }

        /**
         * Generates route for screen with the given {@code screenClass}.
         *
         * @param screenClass screen class
         *
         * @return generated route
         */
        default String getRoute(Class<? extends Screen> screenClass) {
            return getRoute(screenClass, Collections.emptyMap());
        }

        /**
         * Generates route for default editor with the given {@code entity}.
         * <p>
         * Entity id will be passed as URL param.
         *
         * @param entity entity to edit
         *
         * @return generated route
         */
        default String getEditorRoute(Entity entity) {
            return getEditorRoute(entity, Collections.emptyMap());
        }

        /**
         * Generates route for editor with the given {@code screenId} and {@code entity}.
         * <p>
         * Entity id will be passed as URL param.
         *
         * @param entity   entity to edit
         * @param screenId editor screen id
         *
         * @return generated route
         */
        default String getEditorRoute(Entity entity, String screenId) {
            return getEditorRoute(entity, screenId, Collections.emptyMap());
        }

        /**
         * Generates route for editor with the given {@code screenClass} and {@code entity}.
         *
         * @param entity      entity to edit
         * @param screenClass editor screen class
         *
         * @return generated route
         */
        default String getEditorRoute(Entity entity, Class<? extends Screen> screenClass) {
            return getEditorRoute(entity, screenClass, Collections.emptyMap());
        }

        /**
         * Generates route for screen with the given {@code screenId} and {@code urlParams}.
         *
         * @param screenId  screen id
         * @param urlParams URL params
         *
         * @return generated route
         */
        String getRoute(String screenId, Map<String, String> urlParams);

        /**
         * Generates route for screen with the given {@code screenClass} and {@code urlParams}.
         *
         * @param screenClass screen class
         * @param urlParams   URL params
         *
         * @return generated route
         */
        String getRoute(Class<? extends Screen> screenClass, Map<String, String> urlParams);

        /**
         * Generates route for default editor with the given {@code entity} and {@code urlParams}.
         * <p>
         * Entity id will be passed as URL param.
         *
         * @param entity    entity to edit
         * @param urlParams URL params
         *
         * @return generated route
         */
        String getEditorRoute(Entity entity, Map<String, String> urlParams);

        /**
         * Generates route for editor with the given {@code screenId} and {@code entity} and {@code urlParams}.
         * <p>
         * Entity id will be passed as URL param.
         *
         * @param entity    entity to edit
         * @param screenId  editor screen id
         * @param urlParams URL params
         *
         * @return generated route
         */
        String getEditorRoute(Entity entity, String screenId, Map<String, String> urlParams);

        /**
         * Generates route for editor with the given {@code screenClass} and {@code entity} and {@code urlParams}.
         *
         * @param entity      entity to edit
         * @param screenClass editor screen class
         * @param urlParams   URL params
         *
         * @return generated route
         */
        String getEditorRoute(Entity entity, Class<? extends Screen> screenClass, Map<String, String> urlParams);
    }
}
