/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.sys.javacl;

import junit.framework.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Date;

public class JavaClassLoaderTest {
    @Test
    public void testDependencies() throws Exception {
        System.out.println(new File(".").getAbsolutePath());

        JavaClassLoader javaClassLoader = new JavaClassLoader(null, "./test-data/javacl-sources/", "") {
            @Override
            protected Date getCurrentTimestamp() {
                return new Date();
            }
        };

        Class<?> class1 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test.SimpleClass");
        System.out.println("Class loaded");
        modifyFile("./test-data/javacl-sources/com/haulmont/cuba/core/sys/javacl/test/SimpleClass.java");
        System.out.println("SimpleClass modified");
        Class<?> class2 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test.SimpleClass");
        Assert.assertNotSame(class1, class2);
        Assert.assertEquals(javaClassLoader.compiled.size(), 3);
        System.out.println("Class reloaded");

        System.out.println("No changes");
        Class<?> class3 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test.SimpleClass");
        Assert.assertEquals(class2, class3);
        System.out.println("Class reloaded, same class received");

        modifyFile("./test-data/javacl-sources/com/haulmont/cuba/core/sys/javacl/test/pack1/SimpleClass1.java");
        System.out.println("SimpleClass1 modified");

        Class<?> class4 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test.SimpleClass");
        Assert.assertNotSame(class3, class4);
        System.out.println("Class reloaded");
    }

    @Test
    public void testDependent() throws Exception {
        JavaClassLoader javaClassLoader = new JavaClassLoader(null, "./test-data/javacl-sources/", "") {
            @Override
            protected Date getCurrentTimestamp() {
                return new Date();
            }
        };

        Class<?> simpleClass4 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test.pack4.SimpleClass4");
        System.out.println("SimpleClass4 loaded " + simpleClass4.hashCode());
        Class<?> simpleClass = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test.SimpleClass");
        System.out.println("SimpleClass loaded " + simpleClass.hashCode());

        modifyFile("./test-data/javacl-sources/com/haulmont/cuba/core/sys/javacl/test/SimpleClass.java");

        Class<?> simpleClass_2 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test.SimpleClass");
        System.out.println("SimpleClass loaded " + simpleClass_2.hashCode());
        Class<?> simpleClass4_2 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test.pack4.SimpleClass4");
        System.out.println("SimpleClass4 loaded " + simpleClass4_2.hashCode());

        Assert.assertNotSame(simpleClass, simpleClass_2);
        Assert.assertNotSame(simpleClass4, simpleClass4_2);

        Class<?> simpleClass_3 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test.SimpleClass");
        System.out.println("SimpleClass loaded " + simpleClass_3.hashCode());
        Class<?> simpleClass4_3 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test.pack4.SimpleClass4");
        System.out.println("SimpleClass4 loaded " + simpleClass4_3.hashCode());

        Assert.assertEquals(simpleClass_2, simpleClass_3);
        Assert.assertEquals(simpleClass4_2, simpleClass4_3);
    }

    @Test
    public void testLinkageError() throws Exception {
        JavaClassLoader javaClassLoader = new JavaClassLoader(null, "./test-data/javacl-sources/", "") {
            @Override
            protected Date getCurrentTimestamp() {
                return new Date();
            }
        };

        Class<?> class1 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test.SimpleClass");
        System.out.println("Class loaded " + class1.hashCode());

        Object o = class1.newInstance();
        System.out.println(o.toString());

        modifyFile("./test-data/javacl-sources/com/haulmont/cuba/core/sys/javacl/test/pack2/SimpleClass2.java");

        Class<?> class2 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test.SimpleClass");
        System.out.println("Class loaded " + class2.hashCode());

        o = class2.newInstance();
        System.out.println(o.toString());
    }

    private void modifyFile(String path) {
        File file = new File(path);
        file.setLastModified(System.currentTimeMillis() + 10);
    }


}
