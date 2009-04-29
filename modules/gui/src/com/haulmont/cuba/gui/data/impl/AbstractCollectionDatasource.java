/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 30.03.2009 11:52:49
 * $Id$
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.TemplateHelper;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.xml.ParametersHelper;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;

public class AbstractCollectionDatasource<T extends Entity, K>
        extends DatasourceImpl<T> {
    protected String query;
    protected ParametersHelper.ParameterInfo[] queryParameters;

    public AbstractCollectionDatasource(DsContext dsContext, DataService dataservice, String id, MetaClass metaClass, String viewName) {
        super(dsContext, dataservice, id, metaClass, viewName);
    }

    @Override
    public CommitMode getCommitMode() {
        return CommitMode.DATASTORE;
    }

    @Override
    public synchronized void setItem(T item) {
        if (State.VALID.equals(state)) {
            Object prevItem = this.item;

            if (!ObjectUtils.equals(prevItem, item)) {
                if (this.item != null) {
                    detatchListener((Instance) this.item);
                }

                if (item instanceof Instance) {
                    final MetaClass aClass = ((Instance) item).getMetaClass();
                    if (!aClass.equals(metaClass)) {
                        throw new IllegalStateException(String.format("Invalid item metaClass"));
                    }
                    attachListener((Instance) item);
                }
                this.item = item;

                forceItemChanged(prevItem);
            }
        }
    }

    public String getQuery() {
        return query;
    }

    public synchronized void setQuery(String query) {
        if (!ObjectUtils.equals(this.query, query)) {
            this.query = query;
            invalidate();

            queryParameters = ParametersHelper.parseQuery(query);
            for (ParametersHelper.ParameterInfo info : queryParameters) {
                final ParametersHelper.ParameterInfo.Type type = info.getType();
                if (ParametersHelper.ParameterInfo.Type.DATASOURCE.equals(type)) {
                    final String path = info.getPath();

                    final String[] strings = path.split("\\.");
                    String source = strings[0];

                    final String property;
                    if (strings.length > 1) {
                        final List<String> list = Arrays.asList(strings);
                        final List<String> valuePath = list.subList(1, list.size());
                        property = InstanceUtils.formatValuePath(valuePath.toArray(new String[valuePath.size()]));
                    } else {
                        property = null;
                    }

                    final Datasource ds = dsContext.get(source);
                    if (ds != null) {
                        dsContext.regirterDependency(this, ds, property);
                    } else {
                        ((DsContextImplementation) dsContext).addLazyTask(new DsContextImplementation.LazyTask() {
                            public void execute(DsContext context) {
                                final String[] strings = path.split("\\.");
                                String source = strings[0];

                                final Datasource ds = dsContext.get(source);
                                if (ds != null) {
                                    dsContext.regirterDependency(AbstractCollectionDatasource.this, ds, property);
                                }
                            }
                        });
                    }
                }
            }

        }
    }

    protected Map<String, Object> getQueryParameters(Map<String, Object> params) {
        final Map<String, Object> map = new HashMap<String, Object>();
        for (ParametersHelper.ParameterInfo info : queryParameters) {
            String name = info.getFlatName();

            final String path = info.getPath();
            final String[] elements = path.split("\\.");
            switch (info.getType()) {
                case DATASOURCE: {
                    final Datasource datasource = dsContext.get(elements[0]);
                    if (State.VALID.equals(datasource.getState())) {
                        final Entity item = datasource.getItem();
                        if (elements.length > 1) {
                            final List<String> list = Arrays.asList(elements);
                            final List<String> valuePath = list.subList(1, list.size());
                            final String propertyName = InstanceUtils.formatValuePath(valuePath.toArray(new String[valuePath.size()]));

                            map.put(name, InstanceUtils.getValueEx((Instance) item, propertyName));
                        } else {
                            map.put(name, item);
                        }
                    } else {
                        map.put(name, null);
                    }

                    break;
                }
                case PARAM: {
                    final Object value =
                            dsContext.getWindowContext() == null ?
                                    null : dsContext.getWindowContext().getParameterValue(path);
                    map.put(name, value);
                    break;
                }
                case COMPONENT: {
                    final Object value =
                            dsContext.getWindowContext() == null ?
                                    null : dsContext.getWindowContext().getValue(path);
                    map.put(name, value);
                    break;
                }
                case SESSION: {
                    final Object value;
                    if ("userId".equals(name)) 
                        value = UserSessionClient.getUserSession().getUserId();
                    else
                        value = UserSessionClient.getUserSession().getAttribute(path);

                    map.put(name, value);
                    break;
                }
                case CUSTOM: {
                    map.put(name, params.get(info.getName()));
                    break;
                }
                default: {
                    throw new UnsupportedOperationException("Unsupported parameter type: " + info.getType());
                }
            }
        }

        return map;
    }

    protected String getJPQLQuery(String query, Map<String, Object> parameterValues) {
        for (ParametersHelper.ParameterInfo info : queryParameters) {
            final String paramName = info.getName();
            final String jpaParamName = info.getFlatName();

            query = query.replaceAll(paramName.replaceAll("\\$", "\\\\\\$"), jpaParamName);
        }

        query = TemplateHelper.processTemplate(query, parameterValues);

        return query;
    }

    protected void forceCollectionChanged(CollectionDatasourceListener.CollectionOperation operation) {
        for (DatasourceListener dsListener : new ArrayList<DatasourceListener>(dsListeners)) {
            if (dsListener instanceof CollectionDatasourceListener) {
                ((CollectionDatasourceListener) dsListener).collectionChanged(this, operation);
            }
        }
    }
}
