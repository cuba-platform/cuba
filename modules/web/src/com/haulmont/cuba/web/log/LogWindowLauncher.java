/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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