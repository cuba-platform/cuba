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

    boolean isResizable();
    void setResizable(boolean resizable);

    public void addResizeListener(ResizeListener resizeListener);
    public void removeResizeListener(ResizeListener resizeListener);
}