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

import com.haulmont.cuba.gui.Route;
import com.haulmont.cuba.gui.config.WindowConfig;

import java.util.Map;

/**
 * Interface implementations provide an information about specific controller.
 *
 * @see UiControllersConfiguration
 * @see WindowConfig
 */
public interface UiControllerMeta {

    String NAME = "cuba_UiControllerMeta";

    /**
     * @return controller id
     */
    String getId();

    /**
     * @return fully qualified controller class name
     */
    String getControllerClass();

    /**
     * @return route definition configured by the {@link Route} annotation
     */
    RouteDefinition getRouteDefinition();

    /**
     * @param annotationName fully qualified annotation class name
     *
     * @return key-value pairs of annotation properties and their values
     */
    Map<String, Object> getAnnotationAttributes(String annotationName);
}
