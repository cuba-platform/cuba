/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.validators;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class ScriptValidator implements Field.Validator {

    private String script;
    protected String message;
    protected String messagesPack;
    private String scriptPath;
    private boolean innerScript;
    private Map<String, Object> params;
    protected Messages messages = AppBeans.get(Messages.class);

    public ScriptValidator(Element element, String messagesPack) {
        this.script = element.getText();
        innerScript = StringUtils.isNotBlank(script);
        if (!innerScript) {
            scriptPath = element.attributeValue("script");
        }
        message = element.attributeValue("message");
        this.messagesPack = messagesPack;
    }

    public ScriptValidator(String scriptPath, String message, String messagesPack) {
        this.message = message;
        this.messagesPack = messagesPack;
        this.scriptPath = scriptPath;
    }

    public ScriptValidator(String scriptPath, String message, String messagesPack, Map<String, Object> params) {
        this.scriptPath = scriptPath;
        this.message = message;
        this.messagesPack = messagesPack;
        this.params = params;
    }

    @Override
    public void validate(Object value) throws ValidationException {
        Boolean isValid = false;
        if (params == null) {
              params = new HashMap<>();
              params.put("value", value);
        } else {
            params.put("value", value);
        }
        if (innerScript) {
            isValid = AppBeans.get(Scripting.class).evaluateGroovy(script, params);
        } else if (scriptPath != null) {
            isValid = AppBeans.get(Scripting.class).runGroovyScript(scriptPath, params);
        }
        if (!isValid) {
            String msg = message != null ? messages.getTools().loadString(messagesPack, message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value));
        }
    }
}