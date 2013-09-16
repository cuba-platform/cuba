/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

/**
 * @author subbotin
 * @version $Id$
 */
public interface ResizableTextArea extends TextArea {

    String NAME = TextArea.NAME;

    /** Note: TextArea with fixed rows or cols can not be resizable */
    void setResizable(boolean resizable);
    boolean isResizable();

    public void addResizeListener(ResizeListener resizeListener);
    public void removeResizeListener(ResizeListener resizeListener);
}