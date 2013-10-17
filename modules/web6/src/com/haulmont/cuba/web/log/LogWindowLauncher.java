/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.09.2010 12:50:50
 *
 * $Id$
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
