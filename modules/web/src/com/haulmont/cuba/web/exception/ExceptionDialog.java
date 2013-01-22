/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 20.05.2009 17:50:05
 *
 * $Id$
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button;

/**
 * This dialog can be used by exception handlers to show an information about error.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ExceptionDialog extends Window
{
    private String message;

    public ExceptionDialog(String message) {
        super(MessageProvider.getMessage(ExceptionDialog.class, "exceptionDialog.caption"));
        this.message = message;
        setModal(true);
        initUI();
    }

    private void initUI() {
        setWidth("30%");

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        setLayout(layout);

        final Label label = new Label();
        label.setValue(message);
        label.setSizeFull();

        addComponent(label);

        Button closeBtn = new Button(MessageProvider.getMessage(ExceptionDialog.class, "exceptionDialog.closeBtn"));
        closeBtn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                App.getInstance().getAppWindow().removeWindow(ExceptionDialog.this);
            }
        });
        addComponent(closeBtn);
    }
}
