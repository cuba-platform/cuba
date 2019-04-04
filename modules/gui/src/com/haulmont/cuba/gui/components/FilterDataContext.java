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
import com.haulmont.cuba.gui.model.DataLoader;
import com.haulmont.cuba.security.global.UserSession;

import java.util.*;

public class FilterDataContext {

    protected List<DataLoaderRegistration> collectionLoaderRegistrations;
    protected Frame frame;

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);

    protected static class DataLoaderRegistration {
        protected DataLoader loader;
        protected ParameterInfo[] parameters;

        public DataLoaderRegistration(DataLoader loader, ParameterInfo[] parameters) {
            this.loader = loader;
            this.parameters = parameters;
        }

        public DataLoader getLoader() {
            return loader;
        }

        public ParameterInfo[] getParameters() {
            return parameters;
        }
    }

    public FilterDataContext(Frame frame) {
        this.frame = frame;
    }

    public void registerCollectionLoader(DataLoader loader) {
        if (collectionLoaderRegistrations == null) {
            collectionLoaderRegistrations = new ArrayList<>();
        }
        ParameterInfo[] parameters = ParametersHelper.parseQuery(loader.getQuery(), null);
        prepareLoaderQuery(loader, parameters);
        collectionLoaderRegistrations.add(new DataLoaderRegistration(loader, parameters));
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
                            loader.setParameter(info.getFlatName(), e.getValue());
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
