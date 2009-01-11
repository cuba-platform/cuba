/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 15:06:46
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.app.BasicService;
import com.haulmont.cuba.core.global.BasicInvocationContext;
import com.haulmont.cuba.core.global.BasicServiceRemote;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.haulmont.cuba.gui.xml.ParametersHelper;
import org.apache.commons.lang.ObjectUtils;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class CollectionDatasourceImpl<T, K> extends DatasourceImpl<T> implements CollectionDatasource<T, K> {
    private String query;
    private ParametersHelper.ParameterInfo[] queryParameters;
    private DatasourceListener parentDsListener;

    private Collection<T> collection;

    public CollectionDatasourceImpl(DsContext context, String id, MetaClass metaClass, String viewName) {
        super(context, id, metaClass, viewName);
        parentDsListener = new DatasourceListener() {
            public void currentChanged(Datasource ds, Object prevItem, Object item) {
                invalidate();
            }

            public void stateChanged(Datasource ds, State prevState, State state) {}
            public void valueChanged(Object source, String property, Object prevValue, Object value) {}
        };
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.collection = null;
    }

    @Override
    public synchronized void refresh() {
        collection = getCollection();   
    }

    public T getItem(K key) {
        if (State.VALID.equals(state)) {
            return (T) key;
        } else {
            throw new IllegalStateException("Invalid datasource state " + state);
        }
    }

    public Collection<K> getItemIds() {
        if (State.VALID.equals(state)) {
            return (Collection<K>) getCollection();
        } else {
            return Collections.emptyList();
        }
    }

    public int size() {
        if (State.VALID.equals(state)) {
            return getCollection().size();
        } else {
            return 0;
        }
    }

    public void addItem(T item) throws UnsupportedOperationException {
        getCollection().add(item);
    }

    public void removeItem(T item) throws UnsupportedOperationException {
        getCollection().remove(item);
    }

    public boolean containsItem(K itemId) {
        return getCollection().contains(itemId);
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        if (!ObjectUtils.equals(this.query, query)) {
            this.query = query;
            invalidate();

            queryParameters = ParametersHelper.parseQuery(query);
            for (ParametersHelper.ParameterInfo info : queryParameters) {
                final ParametersHelper.ParameterInfo.Type type = info.getType();
                if (ParametersHelper.ParameterInfo.Type.DATASOURCE.equals(type)) {
                    final String name = info.getName();

                    final String[] strings = name.split("\\.");
                    String source = strings[0];

                    final Datasource ds = dsContext.get(source);
                    if (ds != null) {
                        ds.addListener(parentDsListener);
                    } else {
                        // TODO create lazy task
                        throw new UnsupportedOperationException();
                    }
                }
            }

        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected Collection<T> getCollection() {
        if (query == null) {
            throw new IllegalStateException();
        } else {
            if (collection == null) {
                BasicServiceRemote service = Locator.lookupRemote(BasicService.JNDI_NAME);

                final BasicInvocationContext ctx = new BasicInvocationContext();
                ctx.setEntityClass(metaClass);

                final BasicInvocationContext.Query query = ctx.setQueryString(getJPQLQuery(this.query));
                query.setParameters(getQueryParameters());

                ctx.setView(view);

                collection = (Collection) service.loadList(ctx);
            }

            return collection;
        }
    }

    private Map<String, Object> getQueryParameters() {
        final Map<String, Object> map = new HashMap<String, Object>();
        for (ParametersHelper.ParameterInfo info : queryParameters) {
            switch (info.getType()) {
                case DATASOURCE: {
                    final Datasource datasource = dsContext.get(info.getName());
                    map.put(info.getName(), datasource.getItem());
                    break;
                }
                case CONTEXT: {
                    final Object value = dsContext.getContext().getValue(info.getName());
                    map.put(info.getName(), value);
                    break;
                }
                case COMPONENT: {
                    throw new UnsupportedOperationException();
//                    break;
                }
                default: {
                    throw new UnsupportedOperationException();
                }
            }
        }

        return map;
    }

    private String getJPQLQuery(String query) {
        for (ParametersHelper.ParameterInfo info : queryParameters) {
            final String paramInfo = "\\$\\{" + info.getType().getPrefix() + "\\:" + info.getName() + "\\}";
            final String jpqlParamInfo = ":" + info.getType().getPrefix() + "_" + info.getName();

            query = query.replaceAll(paramInfo, jpqlParamInfo);
        }

        return query;
    }
}
