/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 16.12.2008 12:01:45
 *
 * $Id$
 */
package com.haulmont.cuba.web.log;

import com.itmill.toolkit.ui.*;
import com.haulmont.cuba.web.resource.Messages;
import com.haulmont.cuba.web.App;

import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

public class LogWindow extends Window
{
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public LogWindow() {
        super(Messages.getString("logWindow.caption"));
        setHeight("80%");
        setWidth("80%");
        initUI();
    }

    private void initUI() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        setLayout(layout);

        final Label label = new Label();
        label.setContentMode(Label.CONTENT_UIDL);
        label.setValue(writeLog());
        label.setSizeFull();

        Button refreshBtn = new Button(Messages.getString("logWindow.refreshBtn"),
                new Button.ClickListener()
                {
                    public void buttonClick(Button.ClickEvent event) {
                        label.setValue(writeLog());
                    }
                }
        );

        addComponent(refreshBtn);
        addComponent(label);

    }

    private String writeLog() {
        List<LogItem> items = App.getInstance().getAppLog().getItems();
        StringBuilder sb = new StringBuilder();
        for (LogItem item : items) {
            sb.append("<b>");
            sb.append(DateFormatUtils.format(item.getTimestamp(), DATE_FORMAT));
            sb.append(" ");
            sb.append(item.getLevel().name());
            sb.append("</b> ");
            sb.append(item.getMessage());
            if (item.getThrowable() != null) {
                sb.append(" ");
                sb.append(ExceptionUtils.getStackTrace(item.getThrowable()).replace("\n", "<br>"));
            }
            sb.append("<br>");
        }
        return sb.toString();
    }
}
