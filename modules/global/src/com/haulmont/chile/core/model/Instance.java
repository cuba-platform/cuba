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
package com.haulmont.chile.core.model;

import com.haulmont.cuba.core.global.MetadataTools;

import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * Interface to be implemented by data model objects.
 */
public interface Instance extends Serializable {

    /**
     * @return metaclass of this object. Throws exception if the metaclass is not found.
     */
    MetaClass getMetaClass();
    
    /**
     * @return Instance name as defined by {@link com.haulmont.chile.core.annotations.NamePattern}
     * or {@code toString()}.
     * @deprecated Use {@link MetadataTools#getInstanceName(com.haulmont.chile.core.model.Instance)} instead.
     */
    @Deprecated
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
     * <br>
     * An implementor should first read a current value of the attribute, and then call an appropriate setter
     * method only if the new value differs. This ensures triggering of {@link PropertyChangeListener}s only if the attribute
     * was actually changed.
     *
     * @param name  attribute name according to JavaBeans notation
     * @param value attribute value
     */
    void setValue(String name, @Nullable Object value);

    /**
     * Get an attribute value. Locates the attribute by the given path in object graph starting from this instance.
     * <br>
     * The path must consist of attribute names according to JavaBeans notation, separated by dots, e.g.
     * {@code car.driver.name}.
     *
     * @param propertyPath path to an attribute
     * @return attribute value. If any traversing attribute value is null or is not an {@link Instance}, this method
     * stops here and returns this value.
     */
    @Nullable
    <T> T getValueEx(String propertyPath);

    /**
     * Get an attribute value. Locates the attribute by the given path in object graph starting from this instance.
     * <br>
     * The path must consist of attribute names according to JavaBeans notation, separated by dots, e.g.
     * {@code car.driver.name}.
     *
     * @param propertyPath path to an attribute
     * @return attribute value. If any traversing attribute value is null or is not an {@link Instance}, this method
     * stops here and returns this value.
     */
    @Nullable
    <T> T getValueEx(BeanPropertyPath propertyPath);

    /**
     * Set an attribute value. Locates the attribute by the given path in object graph starting from this instance.
     * <br> The path must consist of attribute names according to JavaBeans notation, separated by dots, e.g.
     * {@code car.driver.name}.
     * <br> In the example above this method first gets value of {@code car.driver} attribute, and if it is not
     * null and is an {@link Instance}, sets value of {@code name} attribute in it.
     * <br> An implementor should first read a current value of the attribute, and then call an appropriate setter
     * method only if the new value differs. This ensures triggering of {@link PropertyChangeListener}s only if the attribute
     * was actually changed.
     *
     * @param propertyPath path to an attribute
     * @param value        attribute value
     */
    void setValueEx(String propertyPath, @Nullable Object value);

    /**
     * Set an attribute value. Locates the attribute by the given path in object graph starting from this instance.
     * <br> The path must consist of attribute names according to JavaBeans notation, separated by dots, e.g.
     * {@code car.driver.name}.
     * <br> In the example above this method first gets value of {@code car.driver} attribute, and if it is not
     * null and is an {@link Instance}, sets value of {@code name} attribute in it.
     * <br> An implementor should first read a current value of the attribute, and then call an appropriate setter
     * method only if the new value differs. This ensures triggering of {@link PropertyChangeListener}s only if the attribute
     * was actually changed.
     *
     * @param propertyPath path to an attribute
     * @param value        attribute value
     */
    void setValueEx(BeanPropertyPath propertyPath, Object value);

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

        public PropertyChangeEvent(Instance item, String property, @Nullable Object prevValue, @Nullable Object value) {
            this.item = item;
            this.property = property;
            this.prevValue = prevValue;
            this.value = value;
        }

        /**
         * @return property name
         */
        public String getProperty() {
            return property;
        }

        /**
         * @return data model object
         */
        public Instance getItem() {
            return item;
        }

        /**
         * @return previous value of property
         */
        @Nullable
        public Object getPrevValue() {
            return prevValue;
        }

        /**
         * @return current value of property
         */
        @Nullable
        public Object getValue() {
            return value;
        }
    }

    /**
     * Interface to track changes in data model objects.
     */
    @FunctionalInterface
    interface PropertyChangeListener {
        /**
         * Called when value of instance property changed.
         *
         * @param e event object
         */
        void propertyChanged(PropertyChangeEvent e);
    }

    /**
     * Property path descriptor.
     */
    interface BeanPropertyPath {
        /**
         * @return names of properties
         */
        String[] getPropertyNames();

        /**
         * @return first property name
         */
        String getFirstPropertyName();

        /**
         * @return true if property path represents single property name
         */
        boolean isDirectProperty();
    }
}