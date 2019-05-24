/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.core.entity;


import com.haulmont.chile.core.model.Instance;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ListenersHolder {
    protected static final int PROPERTY_CHANGE_LISTENERS_INITIAL_CAPACITY = 4;

    protected Collection<WeakReference<Instance.PropertyChangeListener>> propertyChangeListeners;

    public void addPropertyChangeListener(Instance.PropertyChangeListener listener) {
        if (propertyChangeListeners == null) {
            propertyChangeListeners = new ArrayList<>(PROPERTY_CHANGE_LISTENERS_INITIAL_CAPACITY);
        }
        propertyChangeListeners.add(new WeakReference<>(listener));
    }

    public void removePropertyChangeListener(Instance.PropertyChangeListener listener) {
        if (propertyChangeListeners != null) {
            for (Iterator<WeakReference<Instance.PropertyChangeListener>> it = propertyChangeListeners.iterator(); it.hasNext(); ) {
                Instance.PropertyChangeListener iteratorListener = it.next().get();
                if (iteratorListener == null || iteratorListener.equals(listener)) {
                    it.remove();
                }
            }
        }
    }

    public void removeAllListeners() {
        if (propertyChangeListeners != null) {
            propertyChangeListeners.clear();
        }
    }

    public <T> void firePropertyChanged(Entity<T> entity, String propertyName, Object prev, Object curr) {
        if (propertyChangeListeners != null) {
            for (Object referenceObject : propertyChangeListeners.toArray()) {
                @SuppressWarnings("unchecked")
                WeakReference<Instance.PropertyChangeListener> reference = (WeakReference<Instance.PropertyChangeListener>) referenceObject;

                Instance.PropertyChangeListener listener = reference.get();
                if (listener == null) {
                    propertyChangeListeners.remove(reference);
                } else {
                    listener.propertyChanged(new Instance.PropertyChangeEvent(entity, propertyName, prev, curr));
                }
            }
        }
    }
}
