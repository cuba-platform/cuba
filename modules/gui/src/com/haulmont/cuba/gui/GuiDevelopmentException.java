/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.global.DevelopmentException;

import java.util.Map;

/**
 * @author hasanov
 * @version $Id$
 */
public class GuiDevelopmentException extends DevelopmentException {

    protected String frameId;

    public GuiDevelopmentException(String message, String frameId) {
        super(message);
        this.frameId = frameId;
    }

    public GuiDevelopmentException(String message, String frameId, String paramKey, Object paramValue) {
        super(message, paramKey, paramValue);
        this.frameId = frameId;
    }

    public GuiDevelopmentException(String message, String frameId, Map<String, Object> params) {
        super(message, params);
        this.frameId = frameId;
    }

    public String getFrameId() {
        return frameId;
    }

    @Override
    public String toString() {
        return super.toString() + (frameId != null ? ", frameId=" + frameId : "");
    }
}
