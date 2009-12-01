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
import com.haulmont.cuba.core.entity.Versioned;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.filter.QueryFilter;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.xml.ParametersHelper;
import com.haulmont.cuba.gui.xml.ParameterInfo;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;

public abstract class AbstractCollectionDatasource<T extends Entity<K>, K>
    extends
        DatasourceImpl<T>
    implements
        CollectionDatasource<T, K>
{
    protected String query;
    private QueryFilter filter;
    protected int maxResults;
    protected ParameterInfo[] queryParameters;
    protected boolean softDeletion;

    public AbstractCollectionDatasource(DsContext dsContext, DataService dataservice, String id, MetaClass metaClass, String viewName) {
        super(dsContext, dataservice, id, metaClass, viewName);
        this.softDeletion = true;
    }

    @Override
    public synchronized void setItem(T item) {
        if (State.VALID.equals(state)) {
            Object prevItem = this.item;

            final MetaClass metaClass = getMetaClass();
            final Class javaClass = metaClass.getJavaClass();

            if (!ObjectUtils.equals(prevItem, item) || 
                    (Versioned.class.isAssignableFrom(javaClass)) &&
                            !ObjectUtils.equals(
                                    prevItem == null ? null : ((Versioned) prevItem).getVersion(),
                                    item == null ? null : ((Versioned) item).getVersion()))
            {
                if (item instanceof Instance) {
                    final MetaClass aClass = ((Instance) item).getMetaClass();
                    if (!aClass.equals(this.metaClass) && !this.metaClass.getDescendants().contains(aClass)) {
                        throw new IllegalStateException(String.format("Invalid item metaClass"));
                    }
                }
                this.item = item;

                forceItemChanged(prevItem);
            }
        }
    }

    public String getQuery() {
        return query;
    }

    public QueryFilter getQueryFilter() {
        return filter;
    }

    public synchronized void setQuery(String query) {
        setQuery(query, null);
    }

    public void setQueryFilter(QueryFilter filter) {
        setQuery(getQuery(), filter);
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public void setQuery(String query, QueryFilter filter) {
        if (ObjectUtils.equals(this.query, query) && ObjectUtils.equals(this.filter, filter))
            return;

        this.query = query;
        this.filter = filter;

        queryParameters = ParametersHelper.parseQuery(query, filter);

        for (ParameterInfo info : queryParameters) {
            final ParameterInfo.Type type = info.getType();
            if (ParameterInfo.Type.DATASOURCE.equals(type)) {
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
                    dsContext.registerDependency(this, ds, property);
                } else {
                    ((DsContextImplementation) dsContext).addLazyTask(new DsContextImplementation.LazyTask() {
                        public void execute(DsContext context) {
                            final String[] strings = path.split("\\.");
                            String source = strings[0];

                            final Datasource ds = dsContext.get(source);
                            if (ds != null) {
                                dsContext.registerDependency(AbstractCollectionDatasource.this, ds, property);
                            }
                        }
                    });
                }
            }
        }
    }

    protected Map<String, Object> getQueryParameters(Map<String, Object> params) {
        final Map<String, Object> map = new HashMap<String, Object>();
        for (ParameterInfo info : queryParameters) {
            String name = info.getFlatName();

            final String path = info.getPath();
            final String[] elements = path.split("\\.");
            switch (info.getType()) {
                case DATASOURCE: {
                    final Datasource datasource = dsContext.get(elements[0]);
                    if (datasource == null)
                        throw new IllegalStateException("Datasource '" + elements[0] + "' not found in dsContext");
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
                    Object value =
                            dsContext.getWindowContext() == null ?
                                    null : dsContext.getWindowContext().getParameterValue(path);
                    if (value instanceof String && info.isCaseInsensitive()) {
                        value = makeCaseInsensitive((String) value);
                    }
                    map.put(name, value);
                    break;
                }
                case COMPONENT: {
                    Object value =
                            dsContext.getWindowContext() == null ?
                                    null : dsContext.getWindowContext().getValue(path);
                    if (value instanceof String && info.isCaseInsensitive()) {
                        value = makeCaseInsensitive((String) value);
                    }
                    map.put(name, value);
                    break;
                }
                case SESSION: {
                    Object value;
                    UserSession us = UserSessionClient.getUserSession();
                    if ("userId".equals(path))
                        value = us.getSubstitutedUser() != null ? us.getSubstitutedUser().getId() : us.getUser().getId();
                    else
                        value = us.getAttribute(path);

                    if (value instanceof String && info.isCaseInsensitive()) {
                        value = makeCaseInsensitive((String) value);
                    }
                    map.put(name, value);
                    break;
                }
                case CUSTOM: {
                    Object value = params.get(info.getPath());
                    if (value instanceof String && info.isCaseInsensitive()) {
                        value = makeCaseInsensitive((String) value);
                    }
                    map.put(name, value);
                    break;
                }
                default: {
                    throw new UnsupportedOperationException("Unsupported parameter type: " + info.getType());
                }
            }
        }

        return map;
    }

    private String makeCaseInsensitive(String value) {
        StringBuilder sb = new StringBuilder();
        sb.append(ParametersHelper.CASE_INSENSITIVE_MARKER);
        if (!value.startsWith("%"))
            sb.append("%");
        sb.append(value);
        if (!value.endsWith("%"))
            sb.append("%");
        return sb.toString();
    }

    protected String getJPQLQuery(Map<String, Object> parameterValues) {
        String query;
        if (filter == null)
            query = this.query;
        else
            query = filter.processQuery(this.query, parameterValues);

        for (ParameterInfo info : queryParameters) {
            final String paramName = info.getName();
            final String jpaParamName = info.getFlatName();

            query = query.replaceAll(paramName.replaceAll("\\$", "\\\\\\$"), jpaParamName);

            Object value = parameterValues.get(paramName);
            if (value != null) {
                parameterValues.put(jpaParamName, value);
            }
        }
        query = query.replace(":" + ParametersHelper.CASE_INSENSITIVE_MARKER, ":");

        query = com.haulmont.cuba.core.app.TemplateHelper.processTemplate(query, parameterValues);

        return query;
    }

    protected void forceCollectionChanged(CollectionDatasourceListener.Operation operation) {
        for (DatasourceListener dsListener : new ArrayList<DatasourceListener>(dsListeners)) {
            if (dsListener instanceof CollectionDatasourceListener) {
                ((CollectionDatasourceListener) dsListener).collectionChanged(this, operation);
            }
        }
    }

    protected Map<String, Object> getTemplateParams(Map<String, Object> customParams) {

        Map<String, Object> templateParams = new HashMap<String, Object>();

        String compPerfix = ParameterInfo.Type.COMPONENT.getPrefix() + "$";
        for (ParameterInfo info : queryParameters) {
            if (ParameterInfo.Type.COMPONENT.equals(info.getType())) {
                Object value = dsContext.getWindowContext() == null ?
                        null : dsContext.getWindowContext().getValue(info.getPath());
                templateParams.put(compPerfix + info.getPath(), value);
            }
        }

        String customPerfix = ParameterInfo.Type.CUSTOM.getPrefix() + "$";
        for (Map.Entry<String, Object> entry : customParams.entrySet()) {
            templateParams.put(customPerfix + entry.getKey(), entry.getValue());
        }

        String paramPrefix = ParameterInfo.Type.PARAM.getPrefix() + "$";
        WindowContext windowContext = dsContext.getWindowContext();
        if (windowContext != null) {
            for (String name : windowContext.getParameterNames()) {
                templateParams.put(paramPrefix + name, windowContext.getParameterValue(name));
            }
        }

        UserSession userSession = UserSessionClient.getUserSession();
        String sessionPrefix = ParameterInfo.Type.SESSION.getPrefix() + "$";
        templateParams.put(sessionPrefix + "userId", userSession.getUser().getId());
        for (String name : userSession.getAttributeNames()) {
            templateParams.put(sessionPrefix + name, userSession.getAttribute(name));
        }

        return templateParams;
    }

    public boolean isSoftDeletion() {
        return softDeletion;
    }

    public void setSoftDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
    }
}
