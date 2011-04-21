/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.gui.components.ValueProvider;
import com.haulmont.cuba.toolkit.gwt.client.utils.VScriptHost;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ClientWidget;

import java.util.HashMap;
import java.util.Map;

/**
 * Component for evaluate custom JavaScript from server
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@ClientWidget(VScriptHost.class)
public class JavaScriptHost extends AbstractComponent {
    private static final long serialVersionUID = -136425458030091656L;

    private ValueProvider valueProvider = new ValueProvider() {
        private static final long serialVersionUID = 2379677116434150730L;
        Map<String, Object> params = new HashMap<String, Object>();

        public Map<String, Object> getValues() {
            return params;
        }

        public Map<String, Object> getParameters() {
            return params;
        }
    };

    public JavaScriptHost() {
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        for (Map.Entry<String, Object> paramEntry : valueProvider.getParameters().entrySet()) {
            target.addAttribute(paramEntry.getKey(), String.valueOf(paramEntry.getValue()));
        }
        cleanCommand();
    }

    public ValueProvider getValueProvider() {
        return valueProvider;
    }

    public void evaluateScript(String script) {
        cleanCommand();

        valueProvider.getParameters().put(VScriptHost.COMMAND_PARAM_KEY, VScriptHost.SCRIPT_COMMAND);
        valueProvider.getParameters().put(VScriptHost.SCRIPT_PARAM_KEY, script);

        requestRepaint();
    }

    public void viewDocument(String documentUrl) {
        cleanCommand();

        valueProvider.getParameters().put(VScriptHost.COMMAND_PARAM_KEY, VScriptHost.VIEW_COMMAND);
        valueProvider.getParameters().put(VScriptHost.URL_PARAM_KEY, documentUrl);

        requestRepaint();
    }

    public void getResource(String resourceUrl) {
        cleanCommand();

        valueProvider.getParameters().put(VScriptHost.COMMAND_PARAM_KEY, VScriptHost.GET_COMMAND);
        valueProvider.getParameters().put(VScriptHost.URL_PARAM_KEY, resourceUrl);

        requestRepaint();
    }

    private void cleanCommand() {
        valueProvider.getParameters().remove(VScriptHost.SCRIPT_PARAM_KEY);
        valueProvider.getParameters().remove(VScriptHost.COMMAND_PARAM_KEY);
    }
}
