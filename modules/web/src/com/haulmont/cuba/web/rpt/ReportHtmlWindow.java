/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.05.2009 15:48:15
 *
 * $Id$
 */
package com.haulmont.cuba.web.rpt;

import com.itmill.toolkit.ui.Window;
import com.itmill.toolkit.ui.VerticalLayout;
import com.itmill.toolkit.ui.Label;

public class ReportHtmlWindow extends ReportOutputWindow
{
    public ReportHtmlWindow(String rptName, String content) {
        super(rptName);
        setName("jasperreport");
        initUI(content);
    }

    private void initUI(String content) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeUndefined();
        Label label = new Label(content);
        label.setContentMode(Label.CONTENT_XHTML);
        label.setSizeUndefined();
        layout.addComponent(label);
        addComponent(layout);
    }
}
