/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.compatibility;

import com.haulmont.cuba.gui.components.ResizableTextArea;
import com.haulmont.cuba.gui.components.ResizeListener;

/**
 * @author artamonov
 * @version $Id$
 */
@Deprecated
public class ResizeListenerWrapper implements ResizableTextArea.ResizeListener {

    private final ResizeListener listener;

    public ResizeListenerWrapper(ResizeListener listener) {
        this.listener = listener;
    }

    @Override
    public void sizeChanged(ResizableTextArea.ResizeEvent e) {
        listener.onResize(e.getComponent(), e.getPrevWidth(), e.getPrevHeight(), e.getWidth(), e.getHeight());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        ResizeListenerWrapper that = (ResizeListenerWrapper) obj;

        return this.listener.equals(that.listener);
    }

    @Override
    public int hashCode() {
        return listener.hashCode();
    }
}