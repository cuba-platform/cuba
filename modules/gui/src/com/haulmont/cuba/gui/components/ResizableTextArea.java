/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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