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

package com.haulmont.cuba.web.widgets;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.haulmont.cuba.web.widgets.client.javascriptcomponent.CubaJavaScriptComponentState;
import com.haulmont.cuba.web.widgets.serialization.DateJsonSerializer;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.Dependency;
import com.vaadin.ui.HasDependencies;
import com.vaadin.ui.JavaScriptFunction;
import elemental.json.Json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CubaJavaScriptComponent<T> extends AbstractJavaScriptComponent implements HasDependencies {

    protected final static Gson sharedGson;

    static {
        // GSON is thread safe so we can use shared GSON instance
        sharedGson = createSharedGsonBuilder().create();
    }

    protected static GsonBuilder createSharedGsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                Expose expose = f.getAnnotation(Expose.class);
                return expose != null && !expose.serialize();
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });

        setDefaultProperties(builder);
        return builder;
    }

    protected static void setDefaultProperties(GsonBuilder builder) {
        builder.registerTypeHierarchyAdapter(Date.class, new DateJsonSerializer());
    }

    protected List<ClientDependency> dependencies;
    protected T stateData;

    protected Gson gson;
    protected boolean dirty = false;

    @Override
    protected CubaJavaScriptComponentState getState() {
        return (CubaJavaScriptComponentState) super.getState();
    }

    @Override
    protected CubaJavaScriptComponentState getState(boolean markAsDirty) {
        return (CubaJavaScriptComponentState) super.getState(markAsDirty);
    }

    @Override
    public List<ClientDependency> getDependencies() {
        return dependencies != null ? dependencies : Collections.emptyList();
    }

    public void setDependencies(List<ClientDependency> dependencies) {
        this.dependencies = dependencies;
    }

    public void addDependency(String path, Dependency.Type type) {
        if (dependencies == null) {
            dependencies = new ArrayList<>();
        }

        dependencies.add(new ClientDependency(path, type));
    }

    public void addDependencies(String... dependencies) {
        if (this.dependencies == null) {
            this.dependencies = new ArrayList<>();
        }

        for (String path : dependencies) {
            this.dependencies.add(new ClientDependency(path));
        }
    }

    public String getInitFunctionName() {
        return getState(false).initFunctionName;
    }

    public void setInitFunctionName(String initFunctionName) {
        if (!Objects.equals(getInitFunctionName(), initFunctionName)) {
            getState().initFunctionName = initFunctionName;
        }
    }

    public T getStateData() {
        return stateData;
    }

    public void setStateData(T data) {
        this.stateData = data;
        forceStateChange();
    }

    @Override
    public void addFunction(String functionName, JavaScriptFunction function) {
        super.addFunction(functionName, function);
    }

    @Override
    public void callFunction(String name, Object... arguments) {
        super.callFunction(name, arguments);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return getState().requiredIndicatorVisible;
    }

    @Override
    public void setRequiredIndicatorVisible(boolean visible) {
        if (getState(false).requiredIndicatorVisible != visible) {
            getState().requiredIndicatorVisible = visible;
        }
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        if (initial || dirty) {
            if (stateData != null) {
                String json = getStateSerializer().toJson(stateData);
                getState().data = Json.parse(json);
            } else {
                getState().data = null;
            }

            dirty = false;
        }
    }

    public Gson getStateSerializer() {
        return gson != null ? gson : sharedGson;
    }

    public void setStateSerializer(Gson serializer) {
        this.gson = serializer;
    }

    public void forceStateChange() {
        this.dirty = true;
        markAsDirty();
    }
}
