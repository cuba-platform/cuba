/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 24.08.2010 18:18:50
 * $Id$
 */

package com.haulmont.cuba.web.ui.jmxcontrol.inspect.operation;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebLabel;
import com.haulmont.cuba.jmxcontrol.util.AttributeHelper;

import java.util.Map;

public class OperationResultWindow extends AbstractWindow {
    private static final long serialVersionUID = 4459991776700972089L;

    public OperationResultWindow(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);

        Throwable ex = (Throwable) params.get("param$exception");
        Object result = params.get("param$result");

        BoxLayout container = (BoxLayout) getComponent("container");
        if (ex != null) {
            Label title = new WebLabel();
            title.setStyleName("h2");
            title.setValue(getMessage("operationResult.exception"));

            Label trace = new WebLabel();
            trace.setValue(ex.getMessage());

            container.add(title);
            container.add(trace);
        }
        else if (result != null) {
            Label title = new WebLabel();
            title.setStyleName("h2");
            title.setValue(getMessage("operationResult.result"));

            Label valueHolder = new WebLabel();
            com.vaadin.ui.Label vaadinLbl = (com.vaadin.ui.Label) WebComponentsHelper.unwrap(valueHolder);
            vaadinLbl.setContentMode(com.vaadin.ui.Label.CONTENT_PREFORMATTED);
            valueHolder.setValue(AttributeHelper.convertToString(result));

            container.add(title);
            container.add(valueHolder);
        }
        else {
            Label title = new WebLabel();
            title.setStyleName("h2");
            title.setValue(getMessage("operationResult.void"));

            container.add(title);
        }

        Button closeBtn = getComponent("close");
        closeBtn.setAction(new AbstractAction("close") {
            private static final long serialVersionUID = 6727220338392459698L;

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
