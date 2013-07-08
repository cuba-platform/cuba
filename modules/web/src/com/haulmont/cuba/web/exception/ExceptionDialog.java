/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.web.AppUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * This dialog can be used by exception handlers to show an information about error.
 *
 * @author krivopustov
 * @version $Id$
 */
public class ExceptionDialog extends Window {
    private String message;

    public ExceptionDialog(String message) {
        super(AppBeans.get(Messages.class).getMessage(ExceptionDialog.class, "exceptionDialog.caption"));
        this.message = message;
        setModal(true);
        initUI();
    }

    private void initUI() {
        setWidth("30%");

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        setContent(layout);

        final Label label = new Label();
        label.setValue(message);
        label.setSizeFull();

        layout.addComponent(label);

        Button closeBtn = new Button(AppBeans.get(Messages.class).getMessage(ExceptionDialog.class, "exceptionDialog.closeBtn"));
        closeBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                AppUI.getCurrent().removeWindow(ExceptionDialog.this);
            }
        });
        layout.addComponent(closeBtn);
    }
}