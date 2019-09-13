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

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component(ViewBuilder.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ViewBuilder {

    public static final String NAME = "cuba_ViewBuilder";

    @Inject
    protected BeanLocator beanLocator;
    @Inject
    protected Metadata metadata;
    @Inject
    protected ViewRepository viewRepository;

    protected Class<? extends Entity> entityClass;
    protected MetaClass metaClass;
    protected Set<String> properties = new LinkedHashSet<>();
    protected Map<String, ViewBuilder> builders = new HashMap<>();
    protected Map<String, View> views = new HashMap<>();
    protected Map<String, FetchMode> fetchModes = new HashMap<>();
    protected boolean systemProperties;

    public static ViewBuilder of(Class<? extends Entity> entityClass) {
        return AppBeans.getPrototype(ViewBuilder.class, entityClass);
    }

    protected ViewBuilder(Class<? extends Entity> entityClass) {
        this.entityClass = entityClass;
    }

    @PostConstruct
    protected void postConstruct() {
        metaClass = metadata.getClassNN(entityClass);
    }

    public View build() {
        View view = new View(entityClass, systemProperties);
        for (String property : properties) {
            ViewBuilder builder = builders.get(property);
            if (builder == null) {
                View refView = views.get(property);
                if (refView == null) {
                    view.addProperty(property);
                } else {
                    FetchMode fetchMode = fetchModes.get(property);
                    if (fetchMode == null) {
                        view.addProperty(property, refView);
                    } else {
                        view.addProperty(property, refView, fetchMode);
                    }
                }
            } else {
                view.addProperty(property, builder.build());
            }
        }
        return view;
    }

    public ViewBuilder add(String property) {
        String[] parts = property.split("\\.");
        String propName = parts[0];
        MetaProperty metaProperty = metaClass.getPropertyNN(propName);
        properties.add(propName);
        if (metaProperty.getRange().isClass()) {
            if (!builders.containsKey(propName)) {
                Class<Entity> refClass = metaProperty.getRange().asClass().getJavaClass();
                builders.put(propName, beanLocator.getPrototype(ViewBuilder.class, refClass));
            }
        }
        if (parts.length > 1) {
            ViewBuilder nestedBuilder = builders.get(propName);
            if (nestedBuilder == null)
                throw new IllegalStateException("Builder not found for property " + propName);
            String nestedProp = Arrays.stream(parts).skip(1).collect(Collectors.joining("."));
            nestedBuilder.add(nestedProp);
        }
        return this;
    }

    public ViewBuilder add(String property, Consumer<ViewBuilder> consumer) {
        properties.add(property);
        Class<Entity> refClass = metaClass.getPropertyNN(property).getRange().asClass().getJavaClass();
        ViewBuilder builder = beanLocator.getPrototype(ViewBuilder.class, refClass);
        consumer.accept(builder);
        builders.put(property, builder);
        return this;
    }

    public ViewBuilder add(String property, String viewName) {
        properties.add(property);
        View view = viewRepository.getView(metaClass.getPropertyNN(property).getRange().asClass(), viewName);
        views.put(property, view);
        return this;
    }

    public ViewBuilder add(String property, String viewName, FetchMode fetchMode) {
        add(property, viewName);
        fetchModes.put(property, fetchMode);
        return this;
    }

    public ViewBuilder addAll(String... properties) {
        for (String property : properties) {
            add(property);
        }
        return this;
    }

    public ViewBuilder addSystem() {
        this.systemProperties = true;
        return this;
    }

    public ViewBuilder addView(View view) {
        for (ViewProperty viewProperty : view.getProperties()) {
            properties.add(viewProperty.getName());
            views.put(viewProperty.getName(), viewProperty.getView());
            fetchModes.put(viewProperty.getName(), viewProperty.getFetchMode());
        }
        return this;
    }

    public ViewBuilder addView(String viewName) {
        addView(viewRepository.getView(metaClass, viewName));
        return this;
    }
}
