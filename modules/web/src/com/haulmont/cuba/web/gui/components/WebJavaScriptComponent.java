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

import com.haulmont.cuba.web.widgets.CubaJavaScriptComponent;
import com.vaadin.ui.Dependency;
import com.vaadin.ui.HasDependencies;
import com.vaadin.ui.JavaScriptFunction;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class WebJavaScriptComponent<T> extends WebAbstractComponent<CubaJavaScriptComponent<T>>
        implements JavaScriptComponent<T> {

    public WebJavaScriptComponent() {
        component = createComponent();
        initComponent(component);
    }

    protected CubaJavaScriptComponent<T> createComponent() {
        return new CubaJavaScriptComponent<>();
    }

    protected void initComponent(CubaJavaScriptComponent<T> component) {
    }

    @Override
    public List<ClientDependency> getDependencies() {
        List<HasDependencies.ClientDependency> vDependencies = component.getDependencies();
        if (vDependencies.isEmpty()) {
            return Collections.emptyList();
        }

        List<ClientDependency> dependencies = new ArrayList<>();
        for (HasDependencies.ClientDependency dependency : vDependencies) {
            DependencyType type = WebWrapperUtils.toDependencyType(dependency.getType());
            dependencies.add(new ClientDependency(dependency.getPath(), type));
        }

        return dependencies;
    }

    @Override
    public void setDependencies(List<ClientDependency> dependencies) {
        if (CollectionUtils.isEmpty(dependencies)) {
            return;
        }

        List<HasDependencies.ClientDependency> vDependencies = new ArrayList<>();
        for (ClientDependency dependency : dependencies) {
            Dependency.Type type = WebWrapperUtils.toVaadinDependencyType(dependency.getType());
            vDependencies.add(new HasDependencies.ClientDependency(dependency.getPath(), type));
        }

        component.setDependencies(vDependencies);
    }

    @Override
    public void addDependency(String path, DependencyType type) {
        component.addDependency(path, WebWrapperUtils.toVaadinDependencyType(type));
    }

    @Override
    public void addDependencies(String... dependencies) {
        component.addDependencies(dependencies);
    }

    @Override
    public String getInitFunctionName() {
        return component.getInitFunctionName();
    }

    @Override
    public void setInitFunctionName(String initFunctionName) {
        component.setInitFunctionName(initFunctionName);
    }

    @Override
    public T getState() {
        return component.getStateData();
    }

    @Override
    public void setState(T state) {
        component.setStateData(state);
    }

    @Override
    public void addFunction(String name, Consumer<JavaScriptCallbackEvent> function) {
        component.addFunction(name, (JavaScriptFunction) arguments -> {
            JavaScriptCallbackEvent event = new JavaScriptCallbackEvent(this, arguments);
            function.accept(event);
        });
    }

    @Override
    public void callFunction(String name, Object... arguments) {
        component.callFunction(name, arguments);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return component.isRequiredIndicatorVisible();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean visible) {
        component.setRequiredIndicatorVisible(visible);
    }

    @Override
    public void repaint() {
        component.forceStateChange();
    }
}
