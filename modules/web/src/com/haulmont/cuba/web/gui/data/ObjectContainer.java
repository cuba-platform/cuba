/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */
package com.haulmont.cuba.web.gui.data;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.UI;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class ObjectContainer implements com.vaadin.data.Container, Container.ItemSetChangeNotifier {

    private Logger log = LoggerFactory.getLogger(getClass());

    // lazily initialized listeners list
    protected List<ItemSetChangeListener> itemSetChangeListeners = null;
    protected boolean ignoreListeners;

    protected static final List<String> methodsName = new ArrayList<>();
    static {
        methodsName.add("getName");
        methodsName.add("getCaption");
    }

    protected List values;

    public ObjectContainer(List values) {
        this.values = values;
    }

    @Override
    public Item getItem(Object itemId) {
        return new ObjectItem(itemId);
    }

    @Override
    public Collection getContainerPropertyIds() {
        return Collections.emptyList();
    }

    @Override
    public Collection getItemIds() {
        return values;
    }

    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class getType(Object propertyId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean containsId(Object itemId) {
        return values.contains(itemId);
    }

    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addContainerProperty(Object propertyId, Class type, Object defaultValue) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addItemSetChangeListener(ItemSetChangeListener listener) {
        if (itemSetChangeListeners == null) {
            itemSetChangeListeners = new LinkedList<>();
        }

        itemSetChangeListeners.add(listener);
    }

    @Override
    public void addListener(ItemSetChangeListener listener) {
        addItemSetChangeListener(listener);
    }

    @Override
    public void removeItemSetChangeListener(ItemSetChangeListener listener) {
        if (itemSetChangeListeners != null) {
            itemSetChangeListeners.remove(listener);

            if (itemSetChangeListeners.isEmpty()) {
                itemSetChangeListeners = null;
            }
        }
    }

    @Override
    public void removeListener(ItemSetChangeListener listener) {
        removeItemSetChangeListener(listener);
    }

    protected void fireItemSetChanged() {
        if (ignoreListeners) {
            return;
        }

        ignoreListeners = true;

        if (UI.getCurrent().getConnectorTracker().isWritingResponse()) {
            // Suppress containerItemSetChange listeners during painting, undefined behavior may be occurred
            return;
        }

        if (itemSetChangeListeners != null) {
            StaticItemSetChangeEvent event = new StaticItemSetChangeEvent(this);

            for (ItemSetChangeListener listener : itemSetChangeListeners) {
                listener.containerItemSetChange(event);
            }
        }

        ignoreListeners = false;
    }

    protected class ObjectItem implements Item {
        protected Object item;
        protected String name;

        public ObjectItem(final Object item) {
            this.item = item;

            if (item instanceof Instance)
                this.name = InstanceUtils.getInstanceName((Instance) item);
            else
                this.name = getNameFromReflection(item);
        }

        private String getNameFromReflection(Object value) {
            final Method[] methods = value.getClass().getMethods();
            if (methods != null) {
                Method method = (Method) CollectionUtils.find(Arrays.asList(methods), new Predicate() {
                    @Override
                    public boolean evaluate(Object o) {
                        Method m = (Method) o;
                        return methodsName.contains(m.getName());
                    }
                });

                if (method != null) {
                    try {
                        final Object o = method.invoke(value);
                        if (o instanceof String)
                            return (String) o;
                        else
                            return String.valueOf(o);
                    } catch (Exception e) {
                        log.error("Error invoking " + method.getName(), e);
                    }
                }
            }
            return String.valueOf(value);
        }

        @Override
        public Property getItemProperty(Object id) {
            return null;
        }

        @Override
        public Collection getItemPropertyIds() {
            return Collections.emptyList();
        }

        @Override
        public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return name == null ? item.toString() : name;
        }
    }
}