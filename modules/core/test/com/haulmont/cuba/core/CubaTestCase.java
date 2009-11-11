/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.11.2008 10:29:29
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.testsupport.TestLocator;
import com.haulmont.cuba.testsupport.TestTransactionManager;
import com.haulmont.cuba.testsupport.TestDataSource;
import junit.framework.TestCase;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.jboss.ejb3.annotation.Depends;
import org.jboss.ejb3.annotation.Service;
import org.reflections.Reflections;
import org.reflections.scanners.ClassAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.AbstractConfiguration;
import org.reflections.util.FilterBuilder;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.naming.NamingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.*;
import java.lang.reflect.Method;
import java.sql.DriverManager;

public abstract class CubaTestCase extends TestCase
{
    private Log log = LogFactory.getLog(CubaTestCase.class);

    private File jbossDeployDir;

    private static final String PACKAGE_PREFIX = "com.haulmont";

    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("cuba.unitTestMode", "true");

        System.setProperty(Locator.IMPL_PROP, "com.haulmont.cuba.testsupport.TestLocator");
        System.setProperty(SecurityProvider.IMPL_PROP, "com.haulmont.cuba.core.sys.TestSecurityProvider");

        File currentDir = new File(System.getProperty("user.dir"));
        File rootDir = currentDir.getParentFile();
        File jbossDir = new File(rootDir, "jboss");
        File jbossDefaultDir = new File(jbossDir, "server/default");
        File jbossConfDir = new File(jbossDefaultDir, "conf");
        jbossDeployDir = new File(jbossDefaultDir, "deploy");
        File jbossLogDir = new File(jbossDefaultDir, "log");
        File jbossTempDir = new File(jbossDefaultDir, "tmp");
        File jbossDataDir = new File(jbossDefaultDir, "data");

        System.setProperty("jboss.server.home.dir", jbossDefaultDir.getAbsolutePath());
        System.setProperty("jboss.server.config.url", jbossConfDir.toURI().toString());
        System.setProperty("jboss.server.log.dir", jbossLogDir.getAbsolutePath());
        System.setProperty("jboss.server.temp.dir", jbossTempDir.getAbsolutePath());
        System.setProperty("jboss.server.data.dir", jbossDataDir.getAbsolutePath());

        initTxManager();
        initDataSources();

        Reflections reflections = new Reflections(
                new AbstractConfiguration() {
                    {
                        setUrls(ReflectionHelper.getUrlsForPackagePrefix(PACKAGE_PREFIX));
                        setScanners(new SubTypesScanner(), new ClassAnnotationsScanner());
                        setFilter(new FilterBuilder());
                    }
                });

        initEjb(reflections);
        initMBeans(reflections);
    }

    private void initTxManager() throws NamingException {
        Locator.getJndiContext().bind("java:/TransactionManager", new TestTransactionManager());
    }

    private void initDataSources() throws Exception {
        String[] fileNames = jbossDeployDir.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith("-ds.xml");
            }
        });
        for (String fileName : fileNames) {
            Document doc = Dom4j.readDocument(new FileInputStream(new File(jbossDeployDir, fileName)));
            Element root = doc.getRootElement();
            Element dsElem = root.element("local-tx-datasource");

            Element elem = dsElem.element("jndi-name");
            String jndiName = elem.getText();

            elem = dsElem.element("driver-class");
            String driver = elem.getText();

            elem = dsElem.element("connection-url");
            String connUrl = elem.getText();

            elem = dsElem.element("user-name");
            String user = elem.getText();

            elem = dsElem.element("password");
            String password = elem.getText();

            Class.forName(driver);

            TestDataSource ds = new TestDataSource(connUrl, user, password);
            Locator.getJndiContext().bind("java:/" + jndiName, ds);
        }
    }

    private void initEjb(Reflections reflections) throws Exception {
        Set<Class<?>> ejbClasses = reflections.getTypesAnnotatedWith(Stateless.class);
        for (Class<?> ejbClass : ejbClasses) {
            Stateless annotation = ejbClass.getAnnotation(Stateless.class);
            String jndiName = annotation.name();
            if (jndiName == null)
                jndiName = ejbClass.getSimpleName();

            boolean local = false, remote = false;
            List<Class> interfaces = ClassUtils.getAllInterfaces(ejbClass);
            for (Class intf : interfaces) {
                if (intf.isAnnotationPresent(Local.class))
                    local = true;
                if (intf.isAnnotationPresent(Remote.class))
                    remote = true;
            }

            Object ejb = ReflectionHelper.newInstance(ejbClass);
            if (local) {
                Locator.getJndiContext().bind(jndiName + "/local", ejb);
                log.info("Bound to JNDI: " + jndiName + "/local : " + ejb);
            }
            if (remote) {
                Locator.getJndiContext().bind(jndiName + "/remote", ejb);
                log.info("Bound to JNDI: " + jndiName + "/remote : " + ejb);
            }
        }
    }

    private void initMBeans(Reflections reflections) throws Exception {
        Set<Class<?>> mbeanClasses = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> mbeanClass : mbeanClasses) {
            Service annotation = mbeanClass.getAnnotation(Service.class);
            String objectName = annotation.objectName();
            if (objectName == null)
                objectName = mbeanClass.getSimpleName();

            List<String> depends = null;
            Depends dependsAnn = mbeanClass.getAnnotation(Depends.class);
            if (dependsAnn != null) {
                String[] strings = dependsAnn.value();
                depends = Arrays.asList(strings);
            }

            Object mbean = ReflectionHelper.newInstance(mbeanClass);
            TestLocator.MBeanInfo mbeanInfo = new TestLocator.MBeanInfo(mbean, depends);

            ((TestLocator) Locator.getInstance()).registerMBean(objectName, mbeanInfo);
            log.info("Registered MBean: " + objectName + " : " + mbean);
        }

        String[] fileNames = jbossDeployDir.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return isServiceXml(name);
            }
        });
        for (String fileName : fileNames) {
            Document doc = Dom4j.readDocument(new FileInputStream(new File(jbossDeployDir, fileName)));
            Element root = doc.getRootElement();
            for (Element mbElem : Dom4j.elements(root, "mbean")) {
                String className = mbElem.attributeValue("code");
                if (!className.startsWith(PACKAGE_PREFIX))
                    continue;

                String objectName = mbElem.attributeValue("name");
                List<String> depends = new ArrayList<String>();
                for (Element depElem : Dom4j.elements(mbElem, "depends")) {
                    depends.add(depElem.getText());
                }

                Class mbeanClass;
                try {
                    mbeanClass = ReflectionHelper.getClass(className);
                } catch (Exception e) {
                    log.warn(e.getMessage());
                    continue;
                }
                Object mbean = ReflectionHelper.newInstance(mbeanClass);
                TestLocator.MBeanInfo mbeanInfo = new TestLocator.MBeanInfo(mbean, depends);

                ((TestLocator) Locator.getInstance()).registerMBean(objectName, mbeanInfo);
                log.info("Registered MBean: " + objectName + " : " + mbean);
            }
        }

        startMBeans(((TestLocator) Locator.getInstance()).getMBeans());
    }


    private boolean isServiceXml(String name) {
        if (name.endsWith("-service.xml")) {
            String begin = StringUtils.substring(name, 0, 2);
            if (StringUtils.isNumeric(begin))
                return true;
        }
        return false;
    }

    private void startMBeans(final Map<String, TestLocator.MBeanInfo> mbeans) throws Exception {
        List<String> mbNames = new ArrayList<String>(mbeans.keySet());
        Collections.sort(mbNames, new DependsComparator(mbeans));
        for (String name : mbNames) {
            log.info("Starting MBean: " + name);
            Object mbean = mbeans.get(name).getMbean();
            try {
                Method method = mbean.getClass().getMethod("create");
                method.invoke(mbean);
            } catch (NoSuchMethodException e) {
                //
            }
            try {
                Method method = mbean.getClass().getMethod("start");
                method.invoke(mbean);
            } catch (NoSuchMethodException e) {
                //
            } 
        }
    }

    private static class DependsComparator implements Comparator<String> {

        private final Map<String, TestLocator.MBeanInfo> mbeans;

        public DependsComparator(Map<String, TestLocator.MBeanInfo> mbeans) {
            this.mbeans = mbeans;
        }

        public int compare(String name1, String name2) {
            List<String> depends1 = mbeans.get(name1).getDepends();
            List<String> depends2 = mbeans.get(name2).getDepends();
            if (inList(name1, depends2, new HashSet()))
                return -1;
            if (inList(name2, depends1, new HashSet()))
                return 1;
            return 0;
        }

        private boolean inList(String name, List<String> list, HashSet<String> checked) {
            if (list.contains(name))
                return true;
            else if (!list.isEmpty()) {
                for (String s : list) {
                    if (checked.contains(s))
                        continue;
                    checked.add(s);

                    TestLocator.MBeanInfo info = mbeans.get(s);
                    if (info != null) {
                        return inList(name, info.getDepends(), new HashSet());
                    }
                }
            }
            return false;
        }
    }
}
