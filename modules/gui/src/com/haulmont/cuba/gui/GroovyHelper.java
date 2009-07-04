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
import groovy.lang.Script;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;

import java.util.Map;

public class GroovyHelper {

    private static Log log = LogFactory.getLog(GroovyHelper.class);

    private static GenericKeyedObjectPool pool;

    private static GenericKeyedObjectPool getPool() {
        if (pool == null) {
            GenericKeyedObjectPool.Config poolConfig = new GenericKeyedObjectPool.Config();
            poolConfig.maxActive = -1;
            pool = new GenericKeyedObjectPool(new BaseKeyedPoolableObjectFactory() {
                public Object makeObject(Object key) throws Exception {
                    if (!(key instanceof String))
                        throw new IllegalArgumentException();
                    String text = ((String) key);

                    StringBuilder sb = new StringBuilder();
                    for (String importItem : AppConfig.getInstance().getGroovyImports()) {
                        sb.append("import ").append(importItem).append("\n");
                    }
                    sb.append(text);

                    GroovyShell shell = new GroovyShell();
                    Script script = shell.parse(sb.toString());
                    return script;
                }
            }, poolConfig);
        }
        return pool;
    }

    public static <T> T evaluate(String text, Binding binding) {
        Script script = null;
        try {
            script = (Script) getPool().borrowObject(text);
            script.setBinding(binding);
            return (T) script.run();
        } catch (Exception e) {
            try {
                getPool().invalidateObject(text, script);
            } catch (Exception e1) {
                log.warn("Error invalidating object in the pool", e1);
            }
            if (e instanceof RuntimeException)
                throw ((RuntimeException) e);
            else
                throw new RuntimeException(e);
        } finally {
            if (script != null)
                try {
                    getPool().returnObject(text, script);
                } catch (Exception e) {
                    log.warn("Error returning object into the pool", e);
                }
        }
    }

    public static <T> T evaluate(String text, Map<String, Object> context) {
        Binding binding = createBinding(context);
        return (T) evaluate(text, binding);
    }

    protected static Binding createBinding(Map<String, Object> map) {
        Binding binding = new Binding();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            binding.setVariable(entry.getKey(), entry.getValue());
        }

        return binding;
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
