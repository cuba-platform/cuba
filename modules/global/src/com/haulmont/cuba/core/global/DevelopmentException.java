/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import java.util.Map;

/**
 * @author hasanov
 * @version $Id$
 */
@SupportedByClient
public class DevelopmentException extends RuntimeException {

    protected Map<String, Object> info;
    protected String frameId;

    public DevelopmentException(String message) {
        super(message);
    }

    public DevelopmentException(String message, String frameId) {
        super(message);
        this.frameId = frameId;
    }

    public DevelopmentException(String message, Map<String, Object> info) {
        super(message);
        this.info = info;
    }

    public DevelopmentException(String message, String frameId, Map<String, Object> info) {
        super(message);
        this.frameId = frameId;
        this.info = info;
    }

    public String getFrameId() {
        return frameId;
    }

    public void setFrameId(String frameId) {
        this.frameId = frameId;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }
}
