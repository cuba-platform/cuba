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

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.web.App;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.Label;
import com.itmill.toolkit.ui.VerticalLayout;
import com.itmill.toolkit.ui.Window;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.List;

public class LogWindow extends Window
{
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public LogWindow() {
        super(MessageProvider.getMessage(LogWindow.class, "logWindow.caption"));
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

        Button refreshBtn = new Button(MessageProvider.getMessage(getClass(), "logWindow.refreshBtn"),
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
