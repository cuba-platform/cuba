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

package com.haulmont.cuba.gui.sys;

public final class UiControllerDefinition {

    private final String id;
    private final String controllerClass;
    private final RouteDefinition routeDefinition;

    public UiControllerDefinition(String id, String controllerClass) {
        this.id = id;
        this.controllerClass = controllerClass;
        this.routeDefinition = null;
    }

    public UiControllerDefinition(String id, String controllerClass, RouteDefinition routeDefinition) {
        this.id = id;
        this.controllerClass = controllerClass;
        this.routeDefinition = routeDefinition;
    }

    public String getId() {
        return id;
    }

    public String getControllerClass() {
        return controllerClass;
    }

    public RouteDefinition getRouteDefinition() {
        return routeDefinition;
    }

    @Override
    public String toString() {
        return "UiControllerDefinition{" +
                "id='" + id + '\'' +
                ", controllerClass='" + controllerClass + '\'' +
                (routeDefinition == null
                        ? ""
                        : ", " + routeDefinition.toString()) +
                '}';
    }

}