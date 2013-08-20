/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.log;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hasanov
 * @version $Id$
 */
public class DevelopmentExceptionWindow extends Window {

    protected VerticalLayout mainLayout;
    protected Panel stackTraceScrollablePanel;
    protected Button showStackTraceButton;
    protected boolean isStackTraceVisible = false;

    protected final String showStackTraceMessage;

    protected int stackTracePanelPosition;

    public DevelopmentExceptionWindow(Throwable throwable) {
        super("Exception");

        setWidth(750, Unit.PIXELS);
        center();

        final Messages messages = AppBeans.get(Messages.class);
        final WindowConfig windowConfig = AppBeans.get(WindowConfig.class);

        StringBuilder rootCauseMessage = new StringBuilder(messages.getMessage(getClass(), "exceptionDialog.message"));
        StringBuilder stackTrace = new StringBuilder();
        rootCauseMessage.append(" ").append(throwable.getMessage());

        String htmlMessage = StringEscapeUtils.escapeHtml(ExceptionUtils.getStackTrace(throwable));
        htmlMessage = StringUtils.replace(htmlMessage, "\n", "<br/>");
        htmlMessage = StringUtils.replace(htmlMessage, " ", "&nbsp;");
        htmlMessage = StringUtils.replace(htmlMessage, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");

        stackTrace.append(htmlMessage);
        stackTrace.append("<br/>");

        DevelopmentException exception = (DevelopmentException) throwable;

        Map<String, Object> tableMap = new HashMap<>();

        try {
            if (exception.getFrameId() != null) {
                tableMap.put("Frame Id", exception.getFrameId());
                tableMap.put("Screen Descriptor", windowConfig.getWindowInfo(exception.getFrameId()).getTemplate());
            }
        } catch (NoSuchScreenException ex) {
            tableMap.put("Screen Descriptor", "Not found screen xml for: " + exception.getFrameId());
        }
        if (exception.getInfo() != null) {
            tableMap.putAll(exception.getInfo());
        }
        mainLayout = new VerticalLayout();
        Label rootCauseMessageLabel = new Label(rootCauseMessage.toString());
        Label infoLabel = new Label(messages.getMessage(getClass(), "exceptionDialog.addInfo"));
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setSpacing(true);

        Button closeButton = new Button(messages.getMessage(getClass(), "exceptionDialog.closeBtn"));
        closeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                DevelopmentExceptionWindow.this.close();
            }
        });
        buttonsLayout.addComponent(closeButton);

        showStackTraceMessage = messages.getMessage(getClass(), "exceptionDialog.showStackTrace");
        showStackTraceButton = new Button(showStackTraceMessage + " >>");
        showStackTraceButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                setStackTraceVisible(!isStackTraceVisible);
            }
        });

        Table infoTable = null;
        if (!tableMap.isEmpty()) {
            infoTable = new Table();
            infoTable.addContainerProperty(messages.getMessage(getClass(), "exceptionDialog.key"), String.class, null);
            infoTable.addContainerProperty(messages.getMessage(getClass(), "exceptionDialog.value"), Object.class, null);
            infoTable.setHeight(150, Unit.PIXELS);
            infoTable.setWidth(100, Unit.PERCENTAGE);
            int itemId = 0;
            for (Map.Entry<String, Object> entry : tableMap.entrySet()) {
                infoTable.addItem(new Object[]{entry.getKey(), entry.getValue()}, itemId++);
            }
        }

        stackTraceScrollablePanel = new Panel();
        stackTraceScrollablePanel.setStyleName("cuba-log-panel");
        stackTraceScrollablePanel.setHeight("100%");
        VerticalLayout scrollContent = new VerticalLayout();
        scrollContent.setSizeUndefined();
        stackTraceScrollablePanel.setContent(scrollContent);

        final Label stackTraceLabel = new Label();
        stackTraceLabel.setContentMode(ContentMode.HTML);
        stackTraceLabel.setValue(stackTrace.toString());
        stackTraceLabel.setSizeUndefined();
        stackTraceLabel.setStyleName("cuba-log-content");
        scrollContent.addComponent(stackTraceLabel);

        mainLayout.addComponent(rootCauseMessageLabel);
        mainLayout.addComponent(infoLabel);
        if (infoTable != null) {
            mainLayout.addComponent(infoTable);
        }

        mainLayout.addComponent(showStackTraceButton);

        stackTracePanelPosition = mainLayout.getComponentCount();

        mainLayout.addComponent(buttonsLayout);
        mainLayout.setSpacing(true);

        setContent(mainLayout);
        setResizable(false);
    }

    public void setStackTraceVisible(boolean visible) {
        isStackTraceVisible = visible;

        if (visible) {
            showStackTraceButton.setCaption(showStackTraceMessage + " <<");

            mainLayout.addComponent(stackTraceScrollablePanel, stackTracePanelPosition);
            mainLayout.setExpandRatio(stackTraceScrollablePanel, 1.0f);
            mainLayout.setHeight(100, Unit.PERCENTAGE);

            setHeight(650, Unit.PIXELS);

            setResizable(true);
            center();
        } else {
            showStackTraceButton.setCaption(showStackTraceMessage + " >>");

            mainLayout.setHeight(-1, Unit.PIXELS);
            mainLayout.removeComponent(stackTraceScrollablePanel);

            setHeight(-1, Unit.PERCENTAGE);
            setWidth(750, Unit.PIXELS);

            setResizable(false);
            center();

            setWindowMode(WindowMode.NORMAL);
        }
    }
}
