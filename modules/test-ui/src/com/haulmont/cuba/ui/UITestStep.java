/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public final class UITestStep {

    private boolean success = true;
    private String name;
    private List<UITestLogMessage> logMessages = new ArrayList<UITestLogMessage>();

    public UITestStep(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<UITestLogMessage> getLogMessages() {
        return logMessages;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
