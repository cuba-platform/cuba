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

    void addResizeListener(ResizeListener resizeListener);
    void removeResizeListener(ResizeListener resizeListener);
}