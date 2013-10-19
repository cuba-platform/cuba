/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.data;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ObjectContainer implements com.vaadin.data.Container {

    private transient Log log = LogFactory.getLog(getClass());

    private static List<String> methodsName = new ArrayList<String>();

    static {
        methodsName.add("getName");
        methodsName.add("getCaption");
    }

    private List values;

    public ObjectContainer(List values) {
        this.values = values;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        log = LogFactory.getLog(getClass());
    }

    public Item getItem(Object itemId) {
        return new ObjectItem(itemId);
    }

    public Collection getContainerPropertyIds() {
        return Collections.emptyList();
    }

    public Collection getItemIds() {
        return values;
    }

    public Property getContainerProperty(Object itemId, Object propertyId) {
        throw new UnsupportedOperationException();
    }

    public Class getType(Object propertyId) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return values.size();
    }

    public boolean containsId(Object itemId) {
        return values.contains(itemId);
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

    class ObjectItem implements Item {

        private Object item;

        private String name;

        ObjectItem(Object item) {
            this.item = item;

            final Method[] methods = this.item.getClass().getMethods();
            if (methods != null) {
                for (int i = 0; i < methods.length; i++) {
                    Method m = methods[i];
                    //TODO use annotation to get captionProperty instead of reflection
                    if (methodsName.contains(m.getName())) {
                        try {
                            final Object o = m.invoke(item);
                            if (o instanceof String) {
                                this.name = (String) o;
                                break;
                            } else {
                                this.name = o.toString();
                                break;
                            }
                        } catch (Exception e) {
                            log.error("error invoking " + m.getName(), e);
                        }
                    }
                }
            }
        }

        public Property getItemProperty(Object id) {
            throw new UnsupportedOperationException();
        }

        public Collection getItemPropertyIds() {
            return Collections.emptyList();
        }

        public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return name == null ? item.toString() : name;
        }
    }
}
