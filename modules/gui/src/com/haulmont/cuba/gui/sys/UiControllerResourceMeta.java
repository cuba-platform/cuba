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
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiController;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Map;

public class UiControllerResourceMeta implements UiControllerMeta {

    protected final MetadataReader metadataReader;
    protected final MetadataReaderFactory metadataReaderFactory;

    public UiControllerResourceMeta(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) {
        this.metadataReader = metadataReader;
        this.metadataReaderFactory = metadataReaderFactory;
    }

    @Override
    public String getId() {
        return getControllerId(metadataReader);
    }

    @Override
    public String getControllerClass() {
        return metadataReader.getClassMetadata().getClassName();
    }

    @Override
    public RouteDefinition getRouteDefinition() {
        return extractRouteDefinition(metadataReader);
    }

    @Override
    public Map<String, Object> getAnnotationAttributes(String annotationName) {
        return metadataReader.getAnnotationMetadata().getAnnotationAttributes(annotationName);
    }

    protected String getControllerId(MetadataReader metadataReader) {
        Map<String, Object> uiControllerAnn =
                metadataReader.getAnnotationMetadata().getAnnotationAttributes(UiController.class.getName());

        String idAttr = null;
        String valueAttr = null;
        if (uiControllerAnn != null) {
            idAttr = (String) uiControllerAnn.get(UiController.ID_ATTRIBUTE);
            valueAttr = (String) uiControllerAnn.get(UiController.VALUE_ATTRIBUTE);
        }

        String className = metadataReader.getClassMetadata().getClassName();
        return UiDescriptorUtils.getInferredScreenId(idAttr, valueAttr, className);
    }

    protected RouteDefinition extractRouteDefinition(MetadataReader metadataReader) {
        Map<String, Object> routeAnnotation =
                metadataReader.getAnnotationMetadata().getAnnotationAttributes(Route.class.getName());

        if (routeAnnotation == null) {
            routeAnnotation = traverseForRoute(metadataReader);
        }

        RouteDefinition routeDefinition = null;

        if (routeAnnotation != null) {
            String pathAttr = (String) routeAnnotation.get(Route.PATH_ATTRIBUTE);
            String parentPrefixAttr = (String) routeAnnotation.get(Route.PARENT_PREFIX_ATTRIBUTE);
            boolean rootRoute = (boolean) routeAnnotation.get(Route.ROOT_ATTRIBUTE);

            routeDefinition = new RouteDefinition(pathAttr, parentPrefixAttr, rootRoute);
        }

        return routeDefinition;
    }

    @Nullable
    protected Map<String, Object> traverseForRoute(MetadataReader metadataReader) {
        String superClazz = metadataReader.getClassMetadata().getSuperClassName();

        if (Screen.class.getName().equals(superClazz)
                || superClazz == null) {
            return null;
        }

        try {
            MetadataReader superReader = metadataReaderFactory.getMetadataReader(superClazz);
            Map<String, Object> routeAnnotation = superReader.getAnnotationMetadata()
                    .getAnnotationAttributes(Route.class.getName());

            return routeAnnotation != null
                    ? routeAnnotation
                    : traverseForRoute(superReader);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read class: %s" + superClazz);
        }
    }
}
