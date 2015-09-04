/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.model;

import com.haulmont.chile.core.common.ValueListener;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.UUID;

/**
 * Interface to be implemented by data model objects.
 *
 * @author abramov
 * @version $Id$
 */
public interface Instance extends Serializable {

    /**
     * @return globally unique identifier of this instance
     */
    UUID getUuid();

    /**
     * @return metaclass of this object. Throws exception if the metaclass is not found.
     */
    MetaClass getMetaClass();
    
    /**
     * @return Instance name as defined by {@link com.haulmont.chile.core.annotations.NamePattern}
     * or <code>toString()</code>.
     */
    String getInstanceName();

    /**
     * Get an attribute value.
     * @param name  attribute name according to JavaBeans notation
     * @return      attribute value
     */
    @Nullable
    <T> T getValue(String name);

    /**
     * Set an attribute value.
     * <p/> An implementor should first read a current value of the attribute, and then call an appropriate setter
     * method only if the new value differs. This ensures triggering of {@link PropertyChangeListener}s only if the attribute
     * was actually changed.
     * @param name  attribute name according to JavaBeans notation
     * @param value attribute value
     */
    void setValue(String name, Object value);

    /**
     * Get an attribute value. Locates the atribute by the given path in object graph starting from this instance.
     * <p/> The path must consist of attribute names according to JavaBeans notation, separated by dots, e.g.
     * <code>car.driver.name</code>.
     * @param propertyPath  path to an attibute
     * @return attribute value. If any traversing attribute value is null or is not an {@link Instance}, this method
     * stops here and returns this value.
     */
    @Nullable
    <T> T getValueEx(String propertyPath);

    /**
     * Set an attribute value. Locates the atribute by the given path in object graph starting from this instance.
     * <p/> The path must consist of attribute names according to JavaBeans notation, separated by dots, e.g.
     * <code>car.driver.name</code>.
     * <p/> In the example above this method first gets value of <code>car.driver</code> attribute, and if it is not
     * null and is an {@link Instance}, sets value of <code>name</code> attribute in it. If the value returned from
     * <code>getValueEx("car.driver")</code> is null or is not an {@link Instance}, this method throws
     * {@link IllegalStateException}.
     * <p/> An implementor should first read a current value of the attribute, and then call an appropriate setter
     * method only if the new value differs. This ensures triggering of {@link PropertyChangeListener}s only if the attribute
     * was actually changed.
     * @param propertyPath  path to an attibute
     * @param value         attribute value
     */
    void setValueEx(String propertyPath, Object value);

    /**
     * Add listener to track attributes changes.
     *
     * @deprecated Use {@link #addPropertyChangeListener(PropertyChangeListener)}
     * @param listener  listener
     */
    @Deprecated
    void addListener(ValueListener listener);

    /**
     * Remove listener.
     *
     * @deprecated User {@link #removePropertyChangeListener(PropertyChangeListener)}
     * @param listener listener to remove
     */
    @Deprecated
    void removeListener(ValueListener listener);

    /**
     * Add listener to track attributes changes.
     * @param listener  listener
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Remove listener.
     * @param listener listener to remove
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Remove all {@link PropertyChangeListener}s.
     */
    void removeAllListeners();

    /**
     * Event object for {@link com.haulmont.chile.core.model.Instance.PropertyChangeListener}.
     */
    class PropertyChangeEvent {
        private final Instance item;
        private final String property;
        private final Object prevValue;
        private final Object value;

        public PropertyChangeEvent(Instance item, String property, Object prevValue, Object value) {
            this.item = item;
            this.property = property;
            this.prevValue = prevValue;
            this.value = value;
        }

        public String getProperty() {
            return property;
        }

        public Instance getItem() {
            return item;
        }

        @Nullable
        public Object getPrevValue() {
            return prevValue;
        }

        @Nullable
        public Object getValue() {
            return value;
        }
    }

    /**
     * Interface to track changes in data model objects.
     */
    interface PropertyChangeListener {
        /**
         * Called when value of instance property changed.
         *
         * @param e event object
         */
        void propertyChanged(PropertyChangeEvent e);
    }
}