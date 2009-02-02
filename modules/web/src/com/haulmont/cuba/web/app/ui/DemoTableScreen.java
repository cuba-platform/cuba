/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 29.01.2009 14:49:58
 * $Id$
 */
package com.haulmont.cuba.web.app.ui;

import com.haulmont.cuba.web.toolkit.ui.PagingTable;
import com.haulmont.cuba.web.ui.Window;
import com.itmill.toolkit.data.Item;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.data.util.BeanItem;
import com.itmill.toolkit.data.util.ObjectProperty;
import com.itmill.toolkit.ui.CustomComponent;
import com.itmill.toolkit.ui.ExpandLayout;
import com.itmill.toolkit.ui.Layout;
import com.itmill.toolkit.ui.ComponentContainer;

import java.util.*;

public class DemoTableScreen extends Window {
    public void init() {
        ((ComponentContainer) component).addComponent(new TableExample());
    }

    class TableExample extends CustomComponent {

        TableExample() {
            Layout main = new ExpandLayout();
            setCompositionRoot(main);

            List<ListBeanContainer.Value> values = new ArrayList<ListBeanContainer.Value>();
            for (int i = 0; i < 100; i++) {
                final ListBeanContainer.Value value = new ListBeanContainer.Value();
                value.setId(String.valueOf(i));
                value.setName("Name " + String.valueOf(i));
                value.setValue("Value " + String.valueOf(i));
                values.add(value);
            }

            com.itmill.toolkit.data.Container dataSource = new ListBeanContainer(values);

            PagingTable table = new PagingTable("Paging Table Example", dataSource);
            table.setPageLength(10);
            table.setHeight("300px");
            table.setWidth("700px");
            table.setItemCaptionPropertyId("id");
            table.setShowPageLengthEditor(true);
            table.setVisibleColumns(dataSource.getContainerPropertyIds().toArray());

            main.addComponent(table);
        }
    }

    static class ListBeanContainer implements com.itmill.toolkit.data.Container {

        private static Collection propertyIds = new ArrayList(3);

        static {
            propertyIds.add("id");
            propertyIds.add("name");
            propertyIds.add("value");
        }

        private List itemIds;
        private Map id2row;

        public ListBeanContainer(List items) {
            if (items == null) {
                items = new ArrayList(0);
            }
            initItems(items);
        }

        private void initItems(List items) {
            itemIds = new ArrayList(items.size());
            id2row = new HashMap(items.size());
            for (final Iterator iterator = items.iterator(); iterator.hasNext();) {
                final Object o = iterator.next();
                ListBeanContainer.ListItem item = null;
                if (o instanceof ListBeanContainer.ListItem) {
                    item = (ListBeanContainer.ListItem) o;
                } else if (o instanceof Value) {
                    item = new ListItem((Value) o);
                }
                if (item != null) {
                    final String itemId = item.getBean().getId();
                    itemIds.add(itemId);
                    id2row.put(itemId, item);
                }
            }
        }

        public Item getItem(Object itemId) {
            return (Item) id2row.get(itemId);
        }

        public Collection getContainerPropertyIds() {
            return Collections.unmodifiableCollection(propertyIds);
        }

        public Collection getItemIds() {
            return Collections.unmodifiableCollection(itemIds);
        }

        public Property getContainerProperty(
                Object itemId,
                Object propertyId
        ) {
            final ListBeanContainer.ListItem item = (ListBeanContainer.ListItem) id2row.get(itemId);
            if (item != null) {
                return item.getItemProperty(propertyId);
            }
            return new ObjectProperty("");
        }

        public Class getType(Object propertyId) {
            return String.class;
        }

        public int size() {
            return itemIds.size();
        }

        public boolean containsId(Object itemId) {
            return (getItem(itemId) != null);
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

        public static class Value {
            private String id;
            private String name;
            private String value;

            public Value() {
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class ListItem extends BeanItem {
            public ListItem(ListBeanContainer.Value value) {
                super(value, propertyIds);
            }

            public ListBeanContainer.Value getBean() {
                return (ListBeanContainer.Value) super.getBean();
            }
        }
    }
}
