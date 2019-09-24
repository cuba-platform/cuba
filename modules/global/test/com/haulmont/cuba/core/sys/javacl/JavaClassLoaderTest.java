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
package com.haulmont.cuba.core.sys.javacl;

import com.haulmont.cuba.core.sys.SpringBeanLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Date;

public class JavaClassLoaderTest {
    @Test
    public void testSimple() throws Exception {
        System.out.println(new File(".").getAbsolutePath());

        JavaClassLoader javaClassLoader = new JavaClassLoader(Thread.currentThread().getContextClassLoader(), "./test-data/javacl-sources/", "", new SpringBeanLoader()) {
            @Override
            protected Date getCurrentTimestamp() {
                return new Date();
            }
        };

        Class<?> class1 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test0.Simple");
        System.out.println("Class loaded " + class1);
//        Class<?> class2 = Class.forName("com.haulmont.cuba.core.sys.javacl.test.SimpleClass");
        Class<?> class2 = Class.forName("com.haulmont.cuba.core.sys.javacl.test0.Simple", false, javaClassLoader);
        System.out.println("Class loaded " + class2);

        Assertions.assertEquals(class1, class2);
    }

    @Test
    public void testDependencies() throws Exception {
        System.out.println(new File(".").getAbsolutePath());

        JavaClassLoader javaClassLoader = new JavaClassLoader(null, "./test-data/javacl-sources/", "", new SpringBeanLoader()) {
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
        Assertions.assertNotSame(class1, class2);
        Assertions.assertEquals(javaClassLoader.compiled.size(), 4);
        System.out.println("Class reloaded");

        System.out.println("No changes");
        Class<?> class3 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test.SimpleClass");
        Assertions.assertEquals(class2, class3);
        System.out.println("Class reloaded, same class received");

        modifyFile("./test-data/javacl-sources/com/haulmont/cuba/core/sys/javacl/test/pack1/SimpleClass1.java");
        System.out.println("SimpleClass1 modified");

        Class<?> class4 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test.SimpleClass");
        Assertions.assertNotSame(class3, class4);
        System.out.println("Class reloaded");
    }

    @Test
    public void testDependent() throws Exception {
        JavaClassLoader javaClassLoader = new JavaClassLoader(null, "./test-data/javacl-sources/", "", new SpringBeanLoader()) {
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

        Assertions.assertNotSame(simpleClass, simpleClass_2);
        Assertions.assertNotSame(simpleClass4, simpleClass4_2);

        Class<?> simpleClass_3 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test.SimpleClass");
        System.out.println("SimpleClass loaded " + simpleClass_3.hashCode());
        Class<?> simpleClass4_3 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test.pack4.SimpleClass4");
        System.out.println("SimpleClass4 loaded " + simpleClass4_3.hashCode());

        Assertions.assertEquals(simpleClass_2, simpleClass_3);
        Assertions.assertEquals(simpleClass4_2, simpleClass4_3);
    }

    @Test
    public void testLinkageError() throws Exception {
        JavaClassLoader javaClassLoader = new JavaClassLoader(null, "./test-data/javacl-sources/", "", new SpringBeanLoader()) {
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

    private void modifyFile(String path) throws InterruptedException {
        Thread.sleep(1000l);
        File file = new File(path);
        boolean result = file.setLastModified(System.currentTimeMillis());
    }

    @Test
    public void testTwiceCompilation() throws Exception {
        JavaClassLoader javaClassLoader = new JavaClassLoader(null, "./test-data/javacl-sources/", "", new SpringBeanLoader()) {
            @Override
            protected Date getCurrentTimestamp() {
                return new Date();
            }
        };

        Class<?> class1 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test2.DependentClass");
        Class<?> dependencyClass1 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test2.pack1.DependencyClass");
        Class<?> dependency2Class1 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test2.pack2.Dependency2Class");
        System.out.println("Class loaded");
        Assertions.assertEquals(javaClassLoader.compiled.size(), 3);
        modifyFile("./test-data/javacl-sources/com/haulmont/cuba/core/sys/javacl/test2/DependentClass.java");
        modifyFile("./test-data/javacl-sources/com/haulmont/cuba/core/sys/javacl/test2/pack1/DependencyClass.java");
        System.out.println("DependentClass modified");
        System.out.println("DependencyClass modified");

        Class<?> class2 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test2.DependentClass");
        Class<?> dependencyClass2 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test2.pack1.DependencyClass");
        Class<?> dependency2Class2 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test2.pack2.Dependency2Class");
        Assertions.assertNotSame(class1, class2);
        Assertions.assertNotSame(dependencyClass1, dependencyClass2);
        Assertions.assertSame(dependency2Class1, dependency2Class2);
    }

    @Test
    public void testInnerClasses() throws Exception {
        JavaClassLoader javaClassLoader = new JavaClassLoader(null, "./test-data/javacl-sources/", "", new SpringBeanLoader()) {
            @Override
            protected Date getCurrentTimestamp() {
                return new Date();
            }
        };

        Class<?> class1 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test3.OuterClass");
        Class<?> innerClass1 = javaClassLoader.compiled.get("com.haulmont.cuba.core.sys.javacl.test3.OuterClass$InnerClass").clazz;
        Class<?> innerClass2 = javaClassLoader.compiled.get("com.haulmont.cuba.core.sys.javacl.test3.OuterClass$1").clazz;
        System.out.println("Class loaded");
        Assertions.assertEquals(javaClassLoader.compiled.size(), 3);
        modifyFile("./test-data/javacl-sources/com/haulmont/cuba/core/sys/javacl/test3/OuterClass.java");

        Class<?> class2 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test3.OuterClass");
        Class<?> innerClass3 = javaClassLoader.compiled.get("com.haulmont.cuba.core.sys.javacl.test3.OuterClass$InnerClass").clazz;
        Class<?> innerClass4 = javaClassLoader.compiled.get("com.haulmont.cuba.core.sys.javacl.test3.OuterClass$1").clazz;
        Assertions.assertNotSame(class1, class2);
        Assertions.assertNotSame(innerClass1, innerClass3);
        Assertions.assertNotSame(innerClass2, innerClass4);
    }

    @Test
    public void testCompanion() throws Exception {
        System.out.println(new File(".").getAbsolutePath());

        JavaClassLoader javaClassLoader = new JavaClassLoader(null, "./test-data/javacl-sources/", "", new SpringBeanLoader()) {
            @Override
            protected Date getCurrentTimestamp() {
                return new Date();
            }
        };

        Class<?> class10 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test4.pack1.MainClass");
        Class<?> interface10 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test4.pack1.MainClass$MainInterface");

        Object object10 = class10.newInstance();

        Class<?> class20 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test4.pack2.DependentClass");
        Object object20 = class20.newInstance();

        Method method = class10.getDeclaredMethod("setObject", interface10);
        method.invoke(object10, object20);

        modifyFile("test-data/javacl-sources/com/haulmont/cuba/core/sys/javacl/test4/pack1/MainClass.java");

        Class<?> class11 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test4.pack1.MainClass");
        Class<?> interface11 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test4.pack1.MainClass$MainInterface");
        Class<?> class21 = javaClassLoader.loadClass("com.haulmont.cuba.core.sys.javacl.test4.pack2.DependentClass");

        Object object11 = class11.newInstance();
        Object object21 = class21.newInstance();

        method = class11.getDeclaredMethod("setObject", interface11);
        method.invoke(object11, object21);
    }
}