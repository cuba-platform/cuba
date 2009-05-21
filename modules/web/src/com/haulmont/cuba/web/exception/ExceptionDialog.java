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
import com.itmill.toolkit.ui.Label;
import com.itmill.toolkit.ui.VerticalLayout;
import com.itmill.toolkit.ui.Window;

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
    }
}
