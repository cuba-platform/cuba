/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui;

import com.haulmont.cuba.gui.components.IFrame;

import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WindowContextImpl extends FrameContextImpl implements WindowContext {

    private WindowManager.OpenType openType;

    public WindowContextImpl(IFrame window, WindowManager.OpenType openType, Map<String, Object> params) {
        super(window, params);
        this.openType = openType;
    }

    @Override
    public WindowManager.OpenType getOpenType() {
        return openType;
    }
}
