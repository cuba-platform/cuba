/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit;

import com.vaadin.ui.Window;

import java.lang.reflect.Method;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaWindow extends Window {

    protected static final Method BEFORE_WINDOW_CLOSE_METHOD;
    static {
        try {
            BEFORE_WINDOW_CLOSE_METHOD = PreCloseListener.class.getDeclaredMethod(
                    "beforeWindowClose", new Class[] { PreCloseEvent.class });
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error, window close method not found");
        }
    }

    public CubaWindow() {
    }

    public CubaWindow(String caption) {
        super(caption);
    }

    public static class PreCloseEvent extends Event {

        private boolean preventClose = false;

        public PreCloseEvent(CubaWindow window) {
            super(window);
        }

        @Override
        public CubaWindow getComponent() {
            return (CubaWindow) super.getComponent();
        }

        public boolean isPreventClose() {
            return preventClose;
        }

        public void setPreventClose(boolean preventClose) {
            this.preventClose = preventClose;
        }
    }

    public interface PreCloseListener {
        void beforeWindowClose(PreCloseEvent event);
    }

    public void addListener(PreCloseListener listener) {
        addListener(PreCloseEvent.class, listener, BEFORE_WINDOW_CLOSE_METHOD);
    }

    public void removeListener(PreCloseListener listener) {
        removeListener(PreCloseEvent.class, listener, BEFORE_WINDOW_CLOSE_METHOD);
    }

    @Override
    public void close() {
        PreCloseEvent event = new PreCloseEvent(this);
        fireEvent(event);

        if (!event.isPreventClose()) {
            super.close();
        }
    }

    public void dispose() {
        super.close();
    }
}