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

    protected final UiControllerMeta controllerMeta;

    public UiControllerDefinition(UiControllerMeta controllerMeta) {
        this.controllerMeta = controllerMeta;
    }

    public UiControllerMeta getControllerMeta() {
        return controllerMeta;
    }

    public String getId() {
        return controllerMeta.getId();
    }

    public String getControllerClass() {
        return controllerMeta.getControllerClass();
    }

    public RouteDefinition getRouteDefinition() {
        return controllerMeta.getRouteDefinition();
    }

    @Override
    public String toString() {
        RouteDefinition routeDefinition = getRouteDefinition();

        return "UiControllerDefinition{" +
                "id='" + getId() + '\'' +
                ", controllerClass='" + getControllerClass() + '\'' +
                (routeDefinition == null
                        ? ""
                        : ", " + routeDefinition.toString()) +
                '}';
    }
}