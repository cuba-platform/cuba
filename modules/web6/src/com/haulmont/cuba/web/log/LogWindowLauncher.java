/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.log;

import com.haulmont.cuba.web.App;

public class LogWindowLauncher implements Runnable {

    public void run() {
        LogWindow logWindow = new LogWindow();
        App.getInstance().getAppWindow().addWindow(logWindow);
        logWindow.focus();
    }
}
