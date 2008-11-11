/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.11.2008 11:26:12
 * $Id$
 */
package com.haulmont.cuba.core;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jboss.ejb3.embedded.EJB3StandaloneBootstrap;
import org.jboss.ejb3.embedded.EJB3StandaloneDeployer;
import org.jboss.mx.util.MBeanServerLocator;

import javax.management.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestContainer
{
    private static boolean started;

    private static TreeSet<String> filesByExt = new TreeSet<String>(new ExtensionComparator());

    private static TreeSet<String> filesByPrefix = new TreeSet<String>(new PrefixComparator());

    private static class ExtensionComparator implements Comparator<String>
    {
        public int compare(String s1, String s2) {
            return getWeight(s1) - getWeight(s2);
        }

        private int getWeight(String s) {
            if (s.endsWith("sar"))
                return 0;
            else if (s.endsWith("service.xml"))
                return 1;
            else if (s.endsWith("rar"))
                return 2;
            else if (s.endsWith("jar"))
                return 3;
            else if (s.endsWith("war"))
                return 4;
            else if (s.endsWith("wsr"))
                return 5;
            else if (s.endsWith("ear"))
                return 6;
            else if (s.endsWith("zip"))
                return 7;
            else
                return 10;
        }
    }

    private static class PrefixComparator implements Comparator<String>
    {
        public int compare(String s1, String s2) {
            Integer p1 = getPrefix(s1);
            Integer p2 = getPrefix(s2);
            return (p1 == null ? 0 : p1) - (p2 == null ? 0 : p2);
        }
    }

    private static Integer getPrefix(String s) {
        Pattern pattern = Pattern.compile("\\A\\d+");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            String prefix = matcher.group();
            try {
                return Integer.valueOf(prefix);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        else
            return null;
    }

    public static void addDeploymentFile(String fileName) {
        if (getPrefix(fileName) != null) {
            filesByPrefix.add(fileName);
        }
        else {
            filesByExt.add(fileName);
        }
    }

    public static void start() {
        if (started)
            return;

        EJB3StandaloneBootstrap.boot(null);
        for (String fileName : filesByExt) {
            deployFile(fileName);
        }
        for (String fileName : filesByPrefix) {
            deployFile(fileName);
        }
        started = true;
    }

    private static void deployFile(String fileName) {
        File deployDir = new File("../jboss/server/default/deploy");
        File file = new File(deployDir, fileName);
        if (!file.exists())
            throw new RuntimeException(String.format("File %s not found", file.getAbsolutePath()));
        if (fileName.endsWith("service.xml")) {
            deployServiceXml(file);
        }
        else {
            EJB3StandaloneDeployer deployer = EJB3StandaloneBootstrap.createDeployer();
            URL archive;
            try {
                archive = file.toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            deployer.getArchives().add(archive);
            try {
                deployer.create();
                deployer.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void deployServiceXml(File file) {
        // TODO KK: implement MBeans dependency
        SAXReader xmlReader = new SAXReader();
        Document doc;
        try {
            doc = xmlReader.read(file);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        MBeanServer server = MBeanServerLocator.locateJBoss();
        Element rootElem = doc.getRootElement();
        try {
            for (Element mbElem : (List<Element>) rootElem.elements("mbean")) {
                String className = mbElem.attributeValue("code");
                String name = mbElem.attributeValue("name");

                ObjectName objectName = new ObjectName(name);
                server.createMBean(className, objectName);

                boolean createExists = false, startExists = false;
                MBeanInfo beanInfo = server.getMBeanInfo(objectName);
                for (MBeanOperationInfo operationInfo : beanInfo.getOperations()) {
                    if ("create".equals(operationInfo.getName()))
                        createExists = true;
                    else if ("start".equals(operationInfo.getName()))
                        startExists = true;
                }
                if (createExists) {
                    server.invoke(objectName, "create", new Object[]{}, new String[]{});
                }
                if (startExists) {
                    server.invoke(objectName, "start", new Object[]{}, new String[]{});
                }
            }
        } catch (ReflectionException e) {
            throw new RuntimeException(e);
        } catch (InstanceAlreadyExistsException e) {
            throw new RuntimeException(e);
        } catch (MBeanException e) {
            throw new RuntimeException(e);
        } catch (NotCompliantMBeanException e) {
            throw new RuntimeException(e);
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

//    private static void setClassPath(File deployDir) {
//        String deployPath;
//        try {
//            deployPath = deployDir.getCanonicalPath();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        String cp = System.getProperty("java.class.path");
//        if (!cp.contains(deployPath + File.pathSeparator)) {
//            System.setProperty("java.class.path", deployPath + File.pathSeparator + cp);
//        }
//    }

    public static void stop() {
        if (!started)
            return;
        EJB3StandaloneBootstrap.shutdown();
        started = false;
    }
}
