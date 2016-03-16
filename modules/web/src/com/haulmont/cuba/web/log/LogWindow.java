/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web.log;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.toolkit.ui.CubaButton;
import com.haulmont.cuba.web.toolkit.ui.CubaWindow;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.List;

/**
 */
public class LogWindow extends CubaWindow {
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
        ClientConfig clientConfig = AppBeans.<Configuration>get(Configuration.NAME).getConfig(ClientConfig.class);

        String closeShortcut = clientConfig.getCloseShortcut();
        KeyCombination closeCombination = KeyCombination.create(closeShortcut);

        com.vaadin.event.ShortcutAction closeShortcutAction = new com.vaadin.event.ShortcutAction(
                "closeShortcutAction",
                closeCombination.getKey().getCode(),
                KeyCombination.Modifier.codes(closeCombination.getModifiers())
        );

        addActionHandler(new com.vaadin.event.Action.Handler() {
            @Override
            public com.vaadin.event.Action[] getActions(Object target, Object sender) {
                return new com.vaadin.event.Action[]{closeShortcutAction};
            }

            @Override
            public void handleAction(com.vaadin.event.Action action, Object sender, Object target) {
                if (ObjectUtils.equals(action, closeShortcutAction)) {
                    close();
                }
            }
        });

        VerticalLayout layout = new VerticalLayout();
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