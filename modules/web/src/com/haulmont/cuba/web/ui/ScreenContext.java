/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 15.12.2008 15:13:59
 *
 * $Id$
 */
package com.haulmont.cuba.web.ui;

import com.itmill.toolkit.ui.TabSheet;
import com.itmill.toolkit.ui.AbstractLayout;
import com.haulmont.cuba.web.App;

public class ScreenContext
{
    private AbstractLayout layout;
    private ScreenTitlePane titlePane;

    public ScreenContext(AbstractLayout layout, ScreenTitlePane titlePane) {
        this.layout = layout;
        this.titlePane = titlePane;
    }

    public String getTabCaption() {
        TabSheet tabSheet = App.getInstance().getAppWindow().getTabSheet();
        return tabSheet.getTabCaption(layout);
    }

    public void setTabCaption(String caption) {
        TabSheet tabSheet = App.getInstance().getAppWindow().getTabSheet();
        tabSheet.setTabCaption(layout, caption);
    }

    public String getScreenCaption() {
        return titlePane.getCurrentCaption();
    }

    public void setScreenCaption(String caption) {
        titlePane.removeCaption();
        titlePane.addCaption(caption);
    }
}
