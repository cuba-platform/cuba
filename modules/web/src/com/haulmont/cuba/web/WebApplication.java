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
import com.itmill.toolkit.ui.*;

public class WebApplication extends Application
{
    public void init() {
        Window mainWindow = new Window("Cuba");
        setMainWindow(mainWindow);

        mainWindow.addComponent(new Label("Hello from Cuba!"));

        LoginDialog dialog = new LoginDialog(mainWindow);
        dialog.show();
    }
}
