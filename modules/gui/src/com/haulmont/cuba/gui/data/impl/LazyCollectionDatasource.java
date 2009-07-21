/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 30.03.2009 11:50:28
 * $Id$
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.DataServiceRemote;
import com.haulmont.cuba.core.global.QueryTransformer;
import com.haulmont.cuba.core.global.QueryTransformerFactory;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.CollectionDatasourceListener;
import com.haulmont.cuba.gui.xml.ParametersHelper;
import org.apache.commons.collections.map.LinkedMap;

import java.util.*;

public class LazyCollectionDatasource<T extends Entity, K>
    extends
        AbstractCollectionDatasource<T, K>
    implements
        CollectionDatasource.Sortable<T, K>
{
    protected LinkedMap data = new LinkedMap();
    protected Integer size;

    protected Map<String, Object> params = Collections.emptyMap();
    protected Map<String, Object> parametersValues;

    protected int chunk = 20;
    private SortInfo<MetaPropertyPath>[] sortInfos;

    public LazyCollectionDatasource(
            DsContext dsContext, com.haulmont.cuba.gui.data.DataService dataservice,
                String id, MetaClass metaClass, String viewName)
    {
        super(dsContext, dataservice, id, metaClass, viewName);
    }

    public void addItem(T item) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void removeItem(T item) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void updateItem(T item) {
        if (!State.VALID.equals(state))
            throw new IllegalStateException("Invalid datasource state: " + state);

        if (data.containsKey(item.getId())) {
            data.put(item.getId(), item);
            forceCollectionChanged(CollectionDatasourceListener.Operation.REFRESH);
        }
    }

    public boolean containsItem(K itemId) {
        if (!isAllLoaded()) loadNextChunk(true);
        return data.containsKey(itemId);
    }

    @Override
    public void refresh() {
        invalidate();
    }

    public void refresh(Map<String, Object> parameters) {
        this.params = parameters;
        invalidate();
    }

    @Override
    public void invalidate() {
        size = null;
        data.clear();
    }

    public int getSize() {
        if (size == null) {
            parametersValues = getQueryParameters(params);
            for (ParametersHelper.ParameterInfo info : queryParameters) {
                if (ParametersHelper.ParameterInfo.Type.DATASOURCE.equals(info.getType())) {
                    final Object value = parametersValues.get(info.getFlatName());
                    if (value == null) return 0;
                }
            }

            String jpqlQuery = getJPQLQuery(query, getTemplateParams(parametersValues, params));

            QueryTransformer transformer = QueryTransformerFactory.createTransformer(jpqlQuery, metaClass.getName());
            transformer.replaceWithCount();
            jpqlQuery = transformer.getResult();

            final DataServiceRemote.LoadContext context =
                    new DataServiceRemote.LoadContext(metaClass);

            context.setQueryString(jpqlQuery).setParameters(parametersValues);

            final List res = dataservice.loadList(context);
            size = res.isEmpty() ? 0 : ((Long) res.get(0)).intValue();
        }

        return size;
    }

    public T getItem(K key) {
        //noinspection unchecked
        return (T) data.get(key);
    }

    public K getItemId(T item) {
        return item == null ? null : (K) item.getId();
    }

    public Collection<K> getItemIds() {
        if (!isAllLoaded()) loadNextChunk(true);
        //noinspection unchecked
        return data.keySet();
    }

    public int size() {
        return getSize();
    }

    public K nextItemId(K itemId) {
        @SuppressWarnings({"unchecked"})
        K nextId = (K) data.nextKey(itemId);
        if (nextId == null && !isAllLoaded()) {
            loadNextChunk(false);
            //noinspection unchecked
            nextId = (K) data.nextKey(itemId);
        }
        return nextId;
    }

    public K prevItemId(K itemId) {
        //noinspection unchecked
        return (K) data.previousKey(itemId);
    }

    public K firstItemId() {
        if (data.isEmpty())
            loadNextChunk(false);

        if (!data.isEmpty()) {
            //noinspection unchecked
            return (K) data.firstKey();
        } else {
            return null;
        }
    }

    public K lastItemId() {
        if (!isAllLoaded())
            loadNextChunk(true);

        if (!data.isEmpty()) {
            //noinspection unchecked
            return (K) data.lastKey();
        } else {
            return null;
        }
    }

    public boolean isFirstId(K itemId) {
        return itemId.equals(firstItemId());
    }

    public boolean isLastId(K itemId) {
        //noinspection SimplifiableConditionalExpression
        return isAllLoaded() ? itemId.equals(lastItemId()) : false;

    }

    protected boolean isAllLoaded() {
        return data.size() == getSize();
    }

    protected void loadNextChunk(boolean all) {
        String jpqlQuery = getJPQLQuery(query, getTemplateParams(parametersValues, params));

        QueryTransformer transformer = QueryTransformerFactory.createTransformer(jpqlQuery, metaClass.getName());
        if (sortInfos != null) {
            final boolean asc = Order.ASC.equals(sortInfos[0].getOrder());
            final MetaPropertyPath propertyPath = sortInfos[0].getPropertyPath();

            if (propertyPath.get().length > 1) throw new UnsupportedOperationException();

            transformer.replaceOrderBy(propertyPath.getMetaProperty().getName(), asc);
        }
        jpqlQuery = transformer.getResult();

        DataServiceRemote.LoadContext ctx = new DataServiceRemote.LoadContext(Server.class);
        ctx.setQueryString(jpqlQuery);
        ctx.getQuery().setFirstResult(data.size());
        ctx.setView(view);

        if (!all) ctx.getQuery().setMaxResults(chunk);

        List<T> res = dataservice.loadList(ctx);
        for (T t : res) {
            data.put(t.getId(), t);
        }

        if (res.size() < chunk) {
            size = data.size(); // all is loaded
        }
    }

    public void sort(SortInfo[] sortInfos) {
        if (sortInfos.length != 1)
            throw new UnsupportedOperationException("Supporting sort by one field only");

        if (!Arrays.equals(this.sortInfos, sortInfos)) {
            doSort();
            //noinspection unchecked
            this.sortInfos = sortInfos;
        }
    }

    private void doSort() {
        if (isAllLoaded()) {
            final MetaPropertyPath propertyPath = sortInfos[0].getPropertyPath();
            final boolean asc = Order.ASC.equals(sortInfos[0].getOrder());

            @SuppressWarnings({"unchecked"})
            List<T> order = new ArrayList<T>(data.values());
            Collections.sort(order, new EntityComparator<T>(propertyPath, asc));
            data.clear();
            for (T t : order) {
                data.put(t.getId(), t);
            }
        } else {
            data.clear();
            loadNextChunk(false);
        }
    }
}
