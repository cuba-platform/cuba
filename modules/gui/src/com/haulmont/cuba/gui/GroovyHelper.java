/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 10.04.2009 11:02:14
 * $Id$
 */
package com.haulmont.cuba.gui;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.Map;

public class GroovyHelper {
    public static <T> T evaluate(String script, Map<String, Object> context) {
        Binding binding = createBinding(context);
        GroovyShell shell = new GroovyShell(binding);

        StringBuilder builder = new StringBuilder();
        for (String importItem : AppConfig.getInstance().getGroovyImports()) {
            builder.append("import ").append(importItem).append("\n");
        }
        builder.append(script);

        //noinspection unchecked
        return (T) shell.evaluate(builder.toString());
    }

    protected static Binding createBinding(Map<String, Object> map) {
        Binding binding = new Binding();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            binding.setVariable(entry.getKey(), entry.getValue());
        }

        return binding;
    }
}
