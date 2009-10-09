/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 27.03.2009 16:33:44
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.MetadataHelper;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class LazyLoadingTableScreen extends WebWindow
{
    private Table table;

    private Log log = LogFactory.getLog(LazyLoadingTableScreen.class);

    private static String QUERY = "select s from core$Server s";

    protected void init(Map<String, Object> params) {
        ((ComponentContainer) component).addComponent(new TableTestComponent());
    }

    private class TableTestComponent extends CustomComponent
    {

        private TableTestComponent() {
            // main layout
            final VerticalLayout main = new VerticalLayout();
            main.setMargin(true);
            setCompositionRoot(main);

            table = new Table("Test Table");
            table.setPageLength(10);
            table.setWidth("100%");
            table.setColumnCollapsingAllowed(true);
            table.setColumnReorderingAllowed(true);

            table.setSelectable(true);
            table.setMultiSelect(false);
            table.setImmediate(true);
            table.setNullSelectionAllowed(false);

            table.setRowHeaderMode(Table.ROW_HEADER_MODE_ID);

            table.setContainerDataSource(new TableTestContainer());
            main.addComponent(table);
        }
    }

    private class TableTestContainer implements com.vaadin.data.Container.Sortable
    {
        private MetaClass metaClass = MetadataProvider.getSession().getClass(Server.class);

        private LinkedMap data = new LinkedMap();
        private Integer dataSize;

        private int chunk = 20;

        private MetaProperty sortProp;
        private boolean sortAsc;

        protected Collection<MetaProperty> properties = new ArrayList<MetaProperty>();

        private DataService ds;

        private TableTestContainer() {
            properties.addAll(metaClass.getOwnProperties());
            sortProp = metaClass.getProperty("name");
            sortAsc = true;

            ds = ServiceLocator.getDataService();
        }

        private void loadMore(boolean all) {
            LoadContext ctx = new LoadContext(Server.class);
            ctx.setQueryString(getQueryStr(false));
            ctx.getQuery().setFirstResult(data.size());
            if (!all)
                ctx.getQuery().setMaxResults(chunk);
            log.debug("loading : " + data.size() + " - " + (all ? "all" : chunk));
            List<Server> list = new ArrayList(ds.loadList(ctx));
            for (Server server : list) {
                data.put(server.getId(), server);
            }
            if (list.size() < chunk) {
                dataSize = data.size(); // all is loaded
            }
        }

        private String getQueryStr(boolean forSize) {
            QueryTransformer transformer = QueryTransformerFactory.createTransformer(QUERY, "core$Server");
            if (forSize) {
                transformer.replaceWithCount();
            } else {
                transformer.replaceOrderBy(sortProp.getName(), !sortAsc);
            }
            return transformer.getResult();

//            StringBuilder sb = new StringBuilder();
//            sb.append("select ");
//            if (forSize)
//                sb.append("count(s) ");
//            else
//                sb.append("s ");
//            sb.append("from core$Server s");
//            if (!forSize) {
//                sb.append(" order by s.").append(sortProp.getName());
//                if (!sortAsc)
//                    sb.append(" desc");
//            }
//            return sb.toString();
        }

        public int getDataSize() {
            if (dataSize == null) {
                LoadContext ctx = new LoadContext(Server.class);
                ctx.setQueryString(getQueryStr(true));
                log.debug("loading data size");
                List list = ds.loadList(ctx);
                if (list.isEmpty())
                    dataSize = 0;
                else
                    dataSize = ((Long) list.get(0)).intValue();
            }
            return dataSize;
        }

        public Item getItem(Object itemId) {
            log.debug("getItem : " + itemId);

            Server server = (Server) data.get(itemId);
            return server == null ? null : new ItemWrapper(server, MetadataHelper.toPropertyPaths(properties));
        }

        public Collection getContainerPropertyIds() {
            return properties;
        }

        public Collection getItemIds() {
            log.debug("getItemIds");
            if (!loadedAll())
                loadMore(true);
            return data.keySet();
        }

        public Property getContainerProperty(Object itemId, Object propertyId) {
            final Item item = getItem(itemId);
            return item == null ? null : item.getItemProperty(propertyId);
        }

        public Class getType(Object propertyId) {
            MetaProperty metaProperty = (MetaProperty) propertyId;
            return MetadataHelper.getTypeClass(metaProperty);
        }

        public int size() {
            log.debug("size");
            return getDataSize();
        }

        public boolean containsId(Object itemId) {
            log.debug("containsId : " + itemId);
            if (!loadedAll())
                loadMore(true);
            return data.containsKey(itemId);
        }

        public Item addItem(Object itemId) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        public Object addItem() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        public boolean removeItem(Object itemId) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        public boolean addContainerProperty(Object propertyId, Class type, Object defaultValue) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        public boolean removeAllItems() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        public void sort(Object[] propertyId, boolean[] ascending) {
            if (propertyId.length != 1)
                throw new UnsupportedOperationException("Supporting sort by one field only");
            log.debug("sort : " + propertyId[0]);
            if (!propertyId[0].equals(sortProp) || ascending[0] != sortAsc) {
                sortProp = (MetaProperty) propertyId[0];
                sortAsc = ascending[0];
                doSort();
            }
        }

        private void doSort() {
            if (loadedAll()) {
                List<Server> list = new ArrayList<Server>(data.values());
                Collections.sort(list, new EntityComparator(sortProp, sortAsc));
                data.clear();
                for (Server server : list) {
                    data.put(server.getId(), server);
                }
            } else {
                data.clear();
                loadMore(false);
            }
        }

        private boolean loadedAll() {
            return data.size() == getDataSize();
        }

        public Collection getSortableContainerPropertyIds() {
            return properties;
        }

        public Object nextItemId(Object itemId) {
            log.debug("nextItemId : " + itemId);

            Object nextId = data.nextKey(itemId);
            if (nextId == null && !loadedAll()) {
                loadMore(false);
                nextId = data.nextKey(itemId);
            }
            return nextId;
        }

        public Object prevItemId(Object itemId) {
            log.debug("prevItemId : " + itemId);

            return data.previousKey(itemId);
        }

        public Object firstItemId() {
            log.debug("firstItemId");

            if (data.isEmpty())
                loadMore(false);

            if (!data.isEmpty()) {
                return data.firstKey();
            } else {
                return null;
            }
        }

        public Object lastItemId() {
            log.debug("lastItemId");

            if (!loadedAll())
                loadMore(true);

            if (!data.isEmpty()) {
                return data.lastKey();
            } else {
                return null;
            }
        }

        public boolean isFirstId(Object itemId) {
            log.debug("isFirstId : " + itemId);
            return itemId.equals(firstItemId());
        }

        public boolean isLastId(Object itemId) {
            log.debug("isLastId : " + itemId);
            //noinspection SimplifiableConditionalExpression
            return loadedAll() ? itemId.equals(lastItemId()) : false;

        }

        public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

    }
    
    private class EntityComparator implements Comparator<Server>
    {
        private MetaProperty property;
        private boolean asc;

        private EntityComparator(MetaProperty property, boolean asc) {
            this.property = property;
            this.asc = asc;
            Class<?> javaClass = property.getJavaType();
            if (!Comparable.class.isAssignableFrom(javaClass))
                throw new UnsupportedOperationException(javaClass + " is not comparable");
        }

        public int compare(Server o1, Server o2) {
            Comparable value1 = ((Instance) o1).getValue(property.getName());
            Comparable value2 = ((Instance) o2).getValue(property.getName());
            return asc ? value1.compareTo(value2) : value2.compareTo(value1);
        }
    }
}
