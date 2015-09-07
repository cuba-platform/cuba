/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.common.compatibility;

import com.haulmont.chile.core.common.ValueListener;
import com.haulmont.chile.core.model.Instance;

/**
 * @author artamonov
 * @version $Id$
 */
@Deprecated
public class InstancePropertyChangeListenerWrapper implements Instance.PropertyChangeListener {

    protected final ValueListener listener;

    public InstancePropertyChangeListenerWrapper(ValueListener listener) {
        this.listener = listener;
    }

    @Override
    public void propertyChanged(Instance.PropertyChangeEvent e) {
        listener.propertyChanged(e.getItem(), e.getProperty(), e.getPrevValue(), e.getValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        InstancePropertyChangeListenerWrapper that = (InstancePropertyChangeListenerWrapper) obj;

        return this.listener.equals(that.listener);
    }

    @Override
    public int hashCode() {
        return listener.hashCode();
    }
}