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

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.global.filter.ParameterInfo;
import com.haulmont.cuba.core.global.filter.ParametersHelper;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.DataLoader;
import com.haulmont.cuba.security.global.UserSession;

import java.util.*;
import java.util.function.Consumer;

public class FilterDataContext {

    protected List<DataLoaderRegistration> collectionLoaderRegistrations;
    protected List<ContainerRegistration> containerRegistrations;
    protected Frame frame;

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);

    protected static class DataLoaderRegistration {
        protected Param param;
        protected DataLoader loader;
        protected ParameterInfo[] parameters;

        public DataLoaderRegistration(Param param, DataLoader loader, ParameterInfo[] parameters) {
            this.param = param;
            this.loader = loader;
            this.parameters = parameters;
        }

        public DataLoader getLoader() {
            return loader;
        }

        public ParameterInfo[] getParameters() {
            return parameters;
        }

        public Param getParam() {
            return param;
        }
    }

    protected static class ContainerRegistration {
        protected Param param;
        protected CollectionContainer container;
        protected Consumer<CollectionContainer.CollectionChangeEvent<?>> listener;

        public ContainerRegistration(Param param, CollectionContainer container, Consumer<CollectionContainer.CollectionChangeEvent<?>> listener) {
            this.param = param;
            this.container = container;
            this.listener = listener;
        }

        public CollectionContainer getContainer() {
            return container;
        }

        public Consumer<CollectionContainer.CollectionChangeEvent<?>> getListener() {
            return listener;
        }

        public Param getParam() {
            return param;
        }
    }

    public FilterDataContext(Frame frame) {
        this.frame = frame;
    }

    public void registerCollectionLoader(Param param, DataLoader loader) {
        if (collectionLoaderRegistrations == null) {
            collectionLoaderRegistrations = new ArrayList<>();
        }
        ParameterInfo[] parameters = ParametersHelper.parseQuery(loader.getQuery(), null);
        prepareLoaderQuery(loader, parameters);
        collectionLoaderRegistrations.add(new DataLoaderRegistration(param, loader, parameters));
    }

    public void registerContainerCollectionChangeListener(Param param,
                                                          CollectionContainer container,
                                                          Consumer<CollectionContainer.CollectionChangeEvent<?>> listener) {
        if (containerRegistrations == null) {
            containerRegistrations = new ArrayList<>();
        }
        containerRegistrations.add(new ContainerRegistration(param, container, listener));
    }

    public void loadAll() {
        if (collectionLoaderRegistrations != null) {
            for (DataLoaderRegistration registration : collectionLoaderRegistrations) {
                DataLoader loader = registration.getLoader();
                Map<String, Object> parameterValues = getQueryParameterValues(loader, registration.getParameters());
                for (Map.Entry<String, Object> entry : parameterValues.entrySet()) {
                    loader.setParameter(entry.getKey(), entry.getValue());
                }
                loader.load();
            }
        }
        if (containerRegistrations != null) {
            for (ContainerRegistration registration : containerRegistrations) {
                //noinspection unchecked
                registration.getContainer().addCollectionChangeListener(registration.getListener());
            }
        }
    }

    public void loadForParam(Param param) {
        if (collectionLoaderRegistrations != null) {
            collectionLoaderRegistrations.stream()
                    .filter(registration -> param.equals(registration.getParam()))
                    .findAny()
                    .ifPresent(registration -> {
                        DataLoader loader = registration.getLoader();
                        Map<String, Object> parameterValues = getQueryParameterValues(loader, registration.getParameters());
                        for (Map.Entry<String, Object> entry : parameterValues.entrySet()) {
                            loader.setParameter(entry.getKey(), entry.getValue());
                        }
                        loader.load();
                    });
        }
    }

    public void unregisterParam(Param param) {
        if (collectionLoaderRegistrations != null) {
            collectionLoaderRegistrations.removeIf(r -> Objects.equals(r.getParam(), param));
        }
        if (containerRegistrations != null) {
            containerRegistrations.removeIf(r -> Objects.equals(r.getParam(), param));
        }
    }

    protected void prepareLoaderQuery(DataLoader loader, ParameterInfo[] parameters) {
        if (parameters.length != 0) {
            String query = loader.getQuery();
            for (ParameterInfo info : parameters) {
                switch (info.getType()) {
                    case COMPONENT:
                    case SESSION:
                        query = query.replace(info.getName(), info.getFlatName());
                        break;
                    default:
                        throw new UnsupportedOperationException(String.format("Unsupported parameter type: %s", info.getType()));
                }
            }
            loader.setQuery(query);
        }
    }

    protected Map<String, Object> getQueryParameterValues(DataLoader loader, ParameterInfo[] parameters) {
        if (parameters.length != 0) {
            Map<String, Object> values = new HashMap<>();
            for (ParameterInfo info : parameters) {
                switch (info.getType()) {
                    case COMPONENT: {
                        Object value = frame.getContext().getValue(info.getPath());
                        if (value instanceof String && info.isCaseInsensitive()) {
                            value = makeCaseInsensitive((String) value);
                        }
                        frame.getContext().addValueChangeListener(info.getPath(), e -> {
                            loader.setParameter(info.getFlatName(), e.getComponent().getValue());
                            loader.load();
                        });
                        values.put(info.getFlatName(), value);
                        break;
                    }
                    case SESSION: {
                        UserSession userSession = userSessionSource.getUserSession();
                        Object value = userSession.getAttribute(info.getPath());
                        if (value instanceof String && info.isCaseInsensitive()) {
                            value = makeCaseInsensitive((String) value);
                        }
                        values.put(info.getFlatName(), value);
                        break;
                    }
                }
            }
            return values;
        }
        return Collections.emptyMap();
    }

    protected String makeCaseInsensitive(String value) {
        StringBuilder sb = new StringBuilder();
        sb.append(ParametersHelper.CASE_INSENSITIVE_MARKER);
        if (!value.startsWith("%")) {
            sb.append("%");
        }
        sb.append(value);
        if (!value.endsWith("%")) {
            sb.append("%");
        }
        return sb.toString();
    }
}
