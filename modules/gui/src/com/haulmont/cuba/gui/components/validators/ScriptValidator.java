/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 08.12.2009 10:51:20
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.validators;

import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.core.global.MessageUtils;

import java.util.Map;
import java.util.HashMap;

import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

public class ScriptValidator implements Field.Validator {
    private String script;
    protected String message;
    protected String messagesPack;
    private String scriptPath;
    private boolean innerScript;

    private Map<String, Object> params;

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

    public void validate(Object value) throws ValidationException {
        Boolean isValid = false;
        if (params == null) {
              params = new HashMap<String, Object>();
              params.put("value", value);
        } else {
            params.put("value", value);
        }
        if (innerScript) {
            isValid = ScriptingProvider.evaluateGroovy(ScriptingProvider.Layer.GUI, script, params);
        } else if (scriptPath != null) {
            isValid = ScriptingProvider.runGroovyScript(scriptPath, params);
        }
        if (!isValid) {
            String msg = message != null ? MessageUtils.loadString(messagesPack, message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value));
        }
    }
}
