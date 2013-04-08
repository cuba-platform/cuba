/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.log;

import com.haulmont.cuba.web.App;

/**
 * @author krivopustov
 * @version $Id$
 */
public class LogWindowLauncher implements Runnable {

    @Override
    public void run() {
        LogWindow logWindow = new LogWindow();
        App.getInstance().getAppUI().addWindow(logWindow);
        logWindow.focus();
    }
}