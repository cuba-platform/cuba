/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
