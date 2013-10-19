/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.jmxcontrol.inspect.operation;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.web.jmx.entity.AttributeHelper;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebLabel;

import javax.inject.Inject;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Map;

/**
 * @author budarov
 * @version $Id$
 */
public class OperationResultWindow extends AbstractWindow {

    @Inject
    protected Label resultLabel;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams().setResizable(true);
        getDialogParams().setWidth(800);
        getDialogParams().setHeight(600);

        Throwable ex = (Throwable) params.get("exception");
        Object result = params.get("result");

        ScrollBoxLayout container = (ScrollBoxLayout) getComponent("container");
        if (ex != null) {
            if (ex instanceof UndeclaredThrowableException)
                ex = ex.getCause();

            String msg;
            if (ex != null) {
                msg = ex.getClass().getName() + ": \n" + ex.getMessage();
            } else {
                msg = "";
            }

            Label trace = new WebLabel();
            trace.setValue(msg);

            resultLabel.setValue(getMessage("operationResult.exception"));
            container.add(trace);
        } else if (result != null) {
            Label valueHolder = new WebLabel();
            com.vaadin.ui.Label vaadinLbl = (com.vaadin.ui.Label) WebComponentsHelper.unwrap(valueHolder);
            vaadinLbl.setContentMode(com.vaadin.ui.Label.CONTENT_PREFORMATTED);
            valueHolder.setValue(AttributeHelper.convertToString(result));

            resultLabel.setValue(getMessage("operationResult.result"));
            container.add(valueHolder);
        } else {
            resultLabel.setValue(getMessage("operationResult.void"));
        }

        Button closeBtn = getComponent("close");
        closeBtn.setAction(new AbstractAction("close") {
            @Override
            public void actionPerform(Component component) {
                close("");
            }

            @Override
            public String getCaption() {
                return getMessage("close");
            }
        });
    }
}