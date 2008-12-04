/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 03.12.2008 14:37:46
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import com.itmill.toolkit.Application;

public class WebApplication extends Application
{
    private Connection connection;

    public WebApplication() {
        this.connection = new Connection(this);
    }

    public void init() {
        AppWindow appWindow = getAppWindow();
        connection.addListener(appWindow);
    }

    protected AppWindow getAppWindow() {
        return new AppWindow(this);
    }

    public Connection getConnection() {
        return connection;
    }
}
