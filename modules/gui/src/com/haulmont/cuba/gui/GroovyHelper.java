/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 10.04.2009 11:02:14
 * $Id$
 */
package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.global.ScriptingProvider;
import groovy.lang.Binding;

import java.util.Map;

/**
 * Utility class for work with Groovy expressions.<br>
 * DEPRECATED - use ScriptingProvider directly
 */
@Deprecated
public class GroovyHelper {

    public static <T> T evaluate(String text, Binding binding) {
        return (T) ScriptingProvider.evaluateGroovy(ScriptingProvider.Layer.GUI, text, binding);
    }

    public static <T> T evaluate(String text, Map<String, Object> context) {
        return (T) ScriptingProvider.evaluateGroovy(ScriptingProvider.Layer.GUI, text, context);
    }

//    public static void main(String[] args) throws InterruptedException {
//        final String text = "Thread.sleep(2000); def res = a; return res";
//        Thread t1 = new Thread(new Runnable() {
//            public void run() {
//                Binding binding = new Binding();
//                binding.setVariable("a", 10);
//                Object r1 = evaluate(text, binding);
//                System.out.println("r1=" + r1);
//            }
//        });
//        Thread t2 = new Thread(new Runnable() {
//            public void run() {
//                Binding binding = new Binding();
//                binding.setVariable("a", 20);
//                Object r2 = evaluate(text, binding);
//                System.out.println("r2=" + r2);
//            }
//        });
//        t1.start();
//        Thread.sleep(1000);
//        t2.start();
//    }
}
