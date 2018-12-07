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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.HasContextHelp;
import elemental.json.JsonArray;
import elemental.json.JsonValue;

import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.List;
import java.util.function.Consumer;

/**
 * A JavaScript wrapper.
 */
public interface JavaScriptComponent extends Component,
        Component.HasCaption, Component.HasDescription, Component.HasIcon, Component.BelongToFrame, HasContextHelp {

    String NAME = "jsComponent";

    /**
     * @return a list of dependencies
     */
    List<ClientDependency> getDependencies();

    /**
     * Sets a list of dependencies.
     * Each dependency represented with a {@link ClientDependency} object which path corresponds to one of the sources:
     *
     * <ul>
     * <li>WebJar resource - starts with {@code webjar://}</li>
     * <li>VAADIN directory - starts with {@code vaadin://}</li>
     * </ul>
     *
     * @param dependencies dependencies to set
     */
    void setDependencies(List<ClientDependency> dependencies);

    /**
     * Adds a dependency. Path path corresponds to one of the sources:
     *
     * <ul>
     * <li>WebJar resource - starts with {@code webjar://}</li>
     * <li>VAADIN directory - starts with {@code vaadin://}</li>
     * </ul>
     *
     * @param path a dependency path
     * @param type a dependency type
     */
    void addDependency(String path, DependencyType type);

    /**
     * Adds dependency paths. Each path corresponds to one of the sources:
     *
     * <ul>
     * <li>WebJar resource - starts with {@code webjar://}</li>
     * <li>VAADIN directory - starts with {@code vaadin://}</li>
     * </ul>
     *
     * @param dependencies dependencies to add
     */
    void addDependencies(String... dependencies);

    /**
     * @return an initialization function name that will be
     * used to find an entry point for the JS component connector
     */
    String getInitFunctionName();

    /**
     * Sets an initialization function name that will be
     * used to find an entry point for the JS component connector.
     * <p>
     * CAUTION: the initialization function name must be unique within window.
     *
     * @param initFunctionName an initialization function name
     */
    void setInitFunctionName(String initFunctionName);

    /**
     * @return Returns a state object
     */
    Object getState();

    /**
     * Sets a state object that can be used in the client-side JS connector
     * and accessible from the {@code data} field of the component's state.
     * <p>
     * Here an example of accessing the state object:
     *
     * <pre>{@code
     * connector.onStateChange = function () {
     *    var state = connector.getState();
     *    let data = state.data;
     *    ...
     * }
     * }</pre>
     * <p>
     * The state object should be a POJO.
     * <p>
     * CAUTION: {@link java.util.Date} fields serialized as strings
     * with {@link com.haulmont.cuba.web.widgets.serialization.DateJsonSerializer#DATE_FORMAT} format.
     *
     * @param state a state object to set
     */
    void setState(Object state);

    /**
     * Register a {@link Consumer} that can be called from the
     * JavaScript using the provided name. A JavaScript function with the
     * provided name will be added to the connector wrapper object (initially
     * available as <code>this</code>). Calling that JavaScript function will
     * cause the call method in the registered {@link Consumer} to be
     * invoked with the same arguments passed to the {@link JavaScriptCallbackEvent}.
     *
     * @param name     the name that should be used for client-side function
     * @param function the {@link Consumer} object that will be invoked
     *                 when the JavaScript function is called
     */
    void addFunction(String name, Consumer<JavaScriptCallbackEvent> function);

    /**
     * Invoke a named function that the connector JavaScript has added to the
     * JavaScript connector wrapper object. The arguments can be any boxed
     * primitive type, String, {@link JsonValue} or arrays of any other
     * supported type. Complex types (e.g. List, Set, Map, Connector or any
     * JavaBean type) must be explicitly serialized to a {@link JsonValue}
     * before sending.
     *
     * @param name      the name of the function
     * @param arguments function arguments
     */
    void callFunction(String name, Object... arguments);

    /**
     * @return whether the required indicator is visible
     */
    boolean isRequiredIndicatorVisible();

    /**
     * Sets whether the required indicator is visible.
     *
     * @param visible {@code true} to make the required indicator visible,
     *                {@code false} otherwise
     */
    void setRequiredIndicatorVisible(boolean visible);

    /**
     * Repaint UI representation of the component.
     */
    void repaint();

    /**
     * An event that is fired when a method is called by a client-side JavaScript function.
     */
    class JavaScriptCallbackEvent extends EventObject {

        protected JsonArray arguments;

        /**
         * Constructs a prototypical Event.
         *
         * @param source The object on which the Event initially occurred
         * @throws IllegalArgumentException if source is null
         */
        public JavaScriptCallbackEvent(JavaScriptComponent source, JsonArray arguments) {
            super(source);
            this.arguments = arguments;
        }

        @Override
        public JavaScriptComponent getSource() {
            return (JavaScriptComponent) super.getSource();
        }

        /**
         * @return a list of arguments with which the JavaScript function was called
         */
        public JsonArray getArguments() {
            return arguments;
        }
    }

    /**
     * The type of dependency.
     */
    enum DependencyType {
        STYLESHEET,
        JAVASCRIPT
    }

    class ClientDependency {
        protected String path;
        protected DependencyType type;

        public ClientDependency(String path) {
            this.path = path;
        }

        public ClientDependency(String path, DependencyType type) {
            this.path = path;
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        @Nullable
        public DependencyType getType() {
            return type;
        }
    }
}
