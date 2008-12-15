/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.12.2008 10:01:04
 *
 * $Id$
 */
package com.haulmont.cuba.web.ui;

import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.ScreenOpenType;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.Window;

public class DemoScreen extends Screen
{
    public void init(ScreenContext context) {
        super.init(context);

        getWindow().showNotification("Opening screen", Window.Notification.TYPE_TRAY_NOTIFICATION);

        Button button = new Button("Open next");
        button.addListener(new Button.ClickListener()
        {
            public void buttonClick(Button.ClickEvent event) {
                App.getInstance().getScreenManager().openScreen(
                        ScreenOpenType.THIS_TAB, "core$Server.browse");
            }
        });
        addComponent(button);
    }

    public boolean onClose() {
        getWindow().showNotification("Closing screen", Window.Notification.TYPE_TRAY_NOTIFICATION);
        return true;
    }
}
