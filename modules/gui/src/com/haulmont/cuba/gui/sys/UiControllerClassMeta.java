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
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UiControllerClassMeta implements UiControllerMeta {

    private static final Logger log = LoggerFactory.getLogger(UiControllerClassMeta.class);

    protected final Class<? extends FrameOwner> screenClass;

    public UiControllerClassMeta(Class<? extends FrameOwner> screenClass) {
        this.screenClass = screenClass;
    }

    @Override
    public String getId() {
        return getControllerId(screenClass);
    }

    @Override
    public String getControllerClass() {
        return screenClass.getName();
    }

    @Override
    public RouteDefinition getRouteDefinition() {
        return getControllerRouteDefinition(screenClass);
    }

    @Override
    public Map<String, Object> getAnnotationAttributes(String annotationName) {
        return getControllerAnnotationAttributes(annotationName, screenClass);
    }

    protected String getControllerId(Class<? extends FrameOwner> screenClass) {
        UiController uiController = screenClass.getAnnotation(UiController.class);

        String idAttr = null;
        String valueAttr = null;
        if (uiController != null) {
            idAttr = uiController.id();
            valueAttr = uiController.value();
        }

        return UiDescriptorUtils.getInferredScreenId(idAttr, valueAttr, screenClass.getName());
    }

    protected RouteDefinition getControllerRouteDefinition(Class<? extends FrameOwner> screenClass) {
        Route route = screenClass.getAnnotation(Route.class);

        if (route == null) {
            route = traverseForRoute(screenClass);
        }

        RouteDefinition routeDefinition = null;

        if (route != null) {
            String pathAttr = route.path();
            String parentPrefixAttr = route.parentPrefix();
            boolean rootRoute = route.root();

            routeDefinition = new RouteDefinition(pathAttr, parentPrefixAttr, rootRoute);
        }

        return routeDefinition;
    }

    @Nullable
    protected Route traverseForRoute(Class screenClass) {
        //noinspection unchecked
        Class<? extends FrameOwner> superClass = screenClass.getSuperclass();
        if (Screen.class.getName().equals(superClass.getName())) {
            return null;
        }

        Route route = superClass.getAnnotation(Route.class);

        return route != null
                ? route
                : traverseForRoute(superClass);
    }

    protected Map<String, Object> getControllerAnnotationAttributes(String annotationName, Class<?> screenClass) {
        for (Annotation annotation : screenClass.getAnnotations()) {
            Class<? extends Annotation> annotationClass = annotation.getClass();
            if (!annotationClass.getName().equals(annotationName)) {
                continue;
            }

            Map<String, Object> annotationAttributes = new HashMap<>();
            for (Method method : annotationClass.getDeclaredMethods()) {
                try {
                    annotationAttributes.put(method.getName(), method.invoke(annotation));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.warn("Failed to get '{}#{}' property value for class '{}'",
                            annotationClass.getName(), method.getName(), screenClass.getName(), e);
                }
            }
            return annotationAttributes;
        }
        return Collections.emptyMap();
    }

}
