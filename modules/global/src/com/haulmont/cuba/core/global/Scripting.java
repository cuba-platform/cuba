/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import groovy.lang.Binding;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.Map;

/**
 * Central interface to provide scripting functionality.
 *
 * <p>Scripting includes ability to run dynamically compiled Java and Groovy classes, as well as loading resources
 * from classpath and conf directory.</p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface Scripting {

    String NAME = "cuba_Scripting";

    public enum Layer {
        CORE,
        GUI
    }

    <T> T evaluateGroovy(Layer layer, String text, Binding binding);

    <T> T evaluateGroovy(Layer layer, String text, Map<String, Object> context);

    <T> T runGroovyScript(String name, Binding binding);

    <T> T runGroovyScript(String name, Map<String, Object> context);

    Class loadClass(String name);

    @Nullable
    InputStream getResourceAsStream(String name);

    @Nullable
    String getResourceAsString(String name);

    void clearCache();
}
