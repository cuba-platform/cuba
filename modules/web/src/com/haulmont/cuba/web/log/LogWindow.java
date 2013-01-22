/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.log;

import com.haulmont.cuba.core.app.ServerInfoService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.toolkit.ui.ScrollablePanel;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class LogWindow extends Window {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public LogWindow() {
        super(AppBeans.get(Messages.class).getMessage(LogWindow.class, "logWindow.caption"));
        setHeight("80%");
        setWidth("80%");
        initUI();
    }

    private void initUI() {
        addActionHandler(new com.vaadin.event.Action.Handler() {
            private ShortcutAction shortcut = new ShortcutAction("escapeAction", ShortcutAction.KeyCode.ESCAPE, null);

            @Override
            public com.vaadin.event.Action[] getActions(Object target, Object sender) {
                return new Action[]{shortcut};
            }

            @Override
            public void handleAction(com.vaadin.event.Action action, Object sender, Object target) {
                if (ObjectUtils.equals(action, shortcut))
                    close();
            }
        });

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setSizeFull();
        setContent(layout);

        ScrollablePanel scrollablePanel = new ScrollablePanel();
        scrollablePanel.setSizeFull();
        scrollablePanel.getContent().setSizeUndefined();

        final Label label = new Label();
        label.setContentMode(Label.CONTENT_XHTML);
        label.setValue(writeLog());
        label.setSizeUndefined();

        scrollablePanel.addComponent(label);

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.setHeight(SIZE_UNDEFINED, 0);

        Button refreshBtn = new Button(AppBeans.get(Messages.class).getMessage(getClass(), "logWindow.refreshBtn"),
                new Button.ClickListener() {
                    @Override
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
        addComponent(scrollablePanel);

        layout.setExpandRatio(scrollablePanel, 1.0f);
    }

    private String getVersionString() {
        ServerInfoService service = AppBeans.get(ServerInfoService.NAME);
        String releaseNumber = service.getReleaseNumber();
        String releaseTimestamp = service.getReleaseTimestamp();
        return AppBeans.get(Messages.class).formatMessage(
                getClass(), "logWindow.versionString", releaseNumber, releaseTimestamp);
    }

    private String writeLog() {
        List<LogItem> items = App.getInstance().getAppLog().getItems();
        StringBuilder sb = new StringBuilder();
        for (LogItem item : items) {
            sb.append("<b>");
            sb.append(DateFormatUtils.format(item.getTimestamp(), DATE_FORMAT));
            sb.append(" ");
            sb.append(item.getLevel().name());
            sb.append("</b>");
            sb.append(StringEscapeUtils.escapeHtml(item.getMessage()));
            if (item.getStacktrace() != null) {
                sb.append(" ");
                sb.append(StringUtils.replace(StringEscapeUtils.escapeHtml(item.getStacktrace()), "\n", "<br/>"));
            }
            sb.append("<br/>");
        }
        return sb.toString();
    }
}