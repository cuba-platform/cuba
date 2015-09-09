/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

/**
 * @author subbotin
 * @version $Id$
 */
public interface ResizableTextArea extends TextArea, Component.HasSettings {
    String NAME = TextArea.NAME;

    /** Note: TextArea with fixed rows or cols can not be resizable */
    void setResizable(boolean resizable);
    boolean isResizable();

    @Deprecated
    void addResizeListener(com.haulmont.cuba.gui.components.ResizeListener resizeListener);
    @Deprecated
    void removeResizeListener(com.haulmont.cuba.gui.components.ResizeListener resizeListener);

    class ResizeEvent {
        private final ResizableTextArea component;
        private final String prevWidth;
        private final String width;
        private final String prevHeight;
        private final String height;

        public ResizeEvent(ResizableTextArea component, String prevWidth, String width, String prevHeight, String height) {
            this.component = component;
            this.prevWidth = prevWidth;
            this.width = width;
            this.prevHeight = prevHeight;
            this.height = height;
        }

        public ResizableTextArea getComponent() {
            return component;
        }

        public String getHeight() {
            return height;
        }

        public String getPrevHeight() {
            return prevHeight;
        }

        public String getPrevWidth() {
            return prevWidth;
        }

        public String getWidth() {
            return width;
        }
    }

    /**
     * Listener for size change events
     */
    interface ResizeListener {
        /**
         * Called by component on size change if ResizableTextArea isResizable equals true
         */
        void sizeChanged(ResizeEvent e);
    }

    void addResizeListener(ResizeListener listener);
    void removeResizeListener(ResizeListener listener);
}