/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.log;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.toolkit.ui.CubaButton;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
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
        Messages messages = AppBeans.get(Messages.NAME);
        setCaption(messages.getMessage(LogWindow.class, "logWindow.caption"));

        AppUI ui = AppUI.getCurrent();
        if (ui.isTestMode()) {
            setId(ui.getTestIdManager().getTestId("logWindow"));
            setCubaId("logWindow");
        }

        setHeight("80%");
        setWidth("80%");
        center();
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
        layout.setMargin(new MarginInfo(true, false, false, false));
        layout.setSpacing(true);
        layout.setSizeFull();
        setContent(layout);

        Panel scrollablePanel = new Panel();
        scrollablePanel.setSizeFull();
        VerticalLayout scrollContent = new VerticalLayout();
        scrollContent.setSizeUndefined();
        scrollablePanel.setContent(scrollContent);

        final Label label = new Label();
        label.setContentMode(ContentMode.HTML);
        label.setValue(writeLog());
        label.setSizeUndefined();
        label.setStyleName("cuba-log-content");

        ((Layout)scrollablePanel.getContent()).addComponent(label);

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.setHeightUndefined();

        Messages messages = AppBeans.get(Messages.NAME);
        Button refreshBtn = new CubaButton(messages.getMessage(getClass(), "logWindow.refreshBtn"),
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        label.setValue(writeLog());
                    }
                }
        );

        topLayout.addComponent(refreshBtn);

        layout.addComponent(topLayout);
        layout.addComponent(scrollablePanel);

        layout.setExpandRatio(scrollablePanel, 1.0f);
    }

    private String writeLog() {
        StringBuilder sb = new StringBuilder();
        List<LogItem> items = App.getInstance().getAppLog().getItems();
        for (LogItem item : items) {
            sb.append("<b>");
            sb.append(DateFormatUtils.format(item.getTimestamp(), DATE_FORMAT));
            sb.append(" ");
            sb.append(item.getLevel().name());
            sb.append("</b>&nbsp;");
            sb.append(StringEscapeUtils.escapeHtml(item.getMessage()));
            if (item.getStacktrace() != null) {
                sb.append(" ");

                String htmlMessage = StringEscapeUtils.escapeHtml(item.getStacktrace());
                htmlMessage = StringUtils.replace(htmlMessage, "\n", "<br/>");
                htmlMessage = StringUtils.replace(htmlMessage, " ", "&nbsp;");
                htmlMessage = StringUtils.replace(htmlMessage, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");

                sb.append(htmlMessage);
            }
            sb.append("<br/>");
        }
        return sb.toString();
    }
}