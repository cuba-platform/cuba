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

public class ScriptValidator implements Field.Validator {

    private String script;
    protected String message;
    protected String messagesPack;
    private String scriptPath;
    private boolean innerScript;
    private Map<String, Object> params;
    protected Messages messages = AppBeans.get(Messages.NAME);

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
        Scripting scripting = AppBeans.get(Scripting.NAME);
        if (innerScript) {
            isValid = scripting.evaluateGroovy(script, params);
        } else if (scriptPath != null) {
            isValid = scripting.runGroovyScript(scriptPath, params);
        }
        if (!isValid) {
            String msg = message != null ? messages.getTools().loadString(messagesPack, message) : "Invalid value '%s'";
            throw new ValidationException(String.format(msg, value));
        }
    }
}