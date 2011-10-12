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

import com.haulmont.cuba.core.app.CubaDeployerService;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.web.App;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.apache.commons.lang.ObjectUtils;
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
        addActionHandler(new com.vaadin.event.Action.Handler() {
            private ShortcutAction shortcut = new ShortcutAction("escapeAction", ShortcutAction.KeyCode.ESCAPE, null);

            public com.vaadin.event.Action[] getActions(Object target, Object sender) {
                return new Action[]{shortcut};
            }

            public void handleAction(com.vaadin.event.Action action, Object sender, Object target) {

                if (ObjectUtils.equals(action, shortcut)) {
                    close();
                }
            }
        });

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        setLayout(layout);

        final Label label = new Label();
        label.setContentMode(Label.CONTENT_XHTML);
        label.setValue(writeLog());
        label.setSizeFull();

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");

        Button refreshBtn = new Button(MessageProvider.getMessage(getClass(), "logWindow.refreshBtn"),
                new Button.ClickListener()
                {
                    public void buttonClick(Button.ClickEvent event) {
                        label.setValue(writeLog());
                    }
                }
        );

        Label versionLabel = new Label();
        versionLabel.setValue(getVersionString());

        topLayout.addComponent(refreshBtn);
        topLayout.addComponent(versionLabel);
        topLayout.setComponentAlignment(versionLabel, Alignment.MIDDLE_RIGHT);

        addComponent(topLayout);
        addComponent(label);
    }

    private String getVersionString() {
        CubaDeployerService service = ServiceLocator.lookup(CubaDeployerService.NAME);
        String releaseNumber = service.getReleaseNumber();
        String releaseTimestamp = service.getReleaseTimestamp();
        String str = MessageProvider.formatMessage(getClass(), "logWindow.versionString", releaseNumber, releaseTimestamp);
        return str;
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
            if (item.getStacktrace() != null) {
                sb.append(" ");
                sb.append(item.getStacktrace().replace("\n", "<br/>"));
            }
            sb.append("<br/>");
        }
        return sb.toString();
    }
}
