/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.03.2010 16:14:11
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.enhance.PCEnhancer;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import javax.persistence.Entity;
import java.io.*;
import java.util.*;

public class PersistenceConfigProcessor {

    private String baseDir;
    private List<String> sourceFileNames;
    private String outFileName;

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public void setSourceFiles(List<String> files) {
        sourceFileNames = files;
    }

    public void setOutputFile(String file) {
        outFileName = file;
    }

//    public static void main(String[] args) {
//        Options options = new Options();
//        options.addOption("e", false, "enhance entities");
//        options.addOption("c", false, "create persistence config");
//        options.addOption("f", true, "source persistence.xml comma-separated list");
//        options.addOption("o", true, "output persistence.xml");
//
//        CommandLineParser parser = new PosixParser();
//        CommandLine cmd = null;
//        try {
//            cmd = parser.parse( options, args);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            System.exit(-1);
//        }
//
//        PersistenceConfigProcessor processor = new PersistenceConfigProcessor();
//        if (cmd.hasOption('f')) {
//            String[] strings = cmd.getOptionValue('f').split(",");
//            processor.setSourceFiles(Arrays.asList(strings));
//        }
//
//        if (cmd.hasOption('c')) {
//            processor.create();
//        } else if (cmd.hasOption('e')) {
//            processor.enhance();
//        } else {
//            HelpFormatter formatter = new HelpFormatter();
//            formatter.printHelp("PersistenceConfigProcessor", options);
//        }
//    }
//
//    public void enhance() {
//        if (sourceFileNames == null || sourceFileNames.isEmpty())
//            throw new IllegalStateException("Source file not set");
//
//        List<String> options = new ArrayList<String>();
//
//        options.add("-properties");
//        options.add(sourceFileNames.get(0));
//
//        System.out.println("Enhancing " + sourceFileNames.get(0));
//        PCEnhancer.main(options.toArray(new String[options.size()]));
//    }

    public void create() {
        if (sourceFileNames == null || sourceFileNames.isEmpty())
            throw new IllegalStateException("Source file list not set");
        if (StringUtils.isBlank(outFileName))
            throw new IllegalStateException("Output file not set");

        Map<String, String> classes = new LinkedHashMap<String, String>();

        for (String fileName : sourceFileNames) {
            addClasses(fileName, classes);
        }

        String fileName = sourceFileNames.get(sourceFileNames.size() - 1);
        Document doc = getDocument(fileName);
        Element rootElem = doc.getRootElement();

        Element puElem = findPersistenceUnitElement(rootElem);
        if (puElem == null)
            throw new IllegalStateException("No persistence unit named 'cuba' found among multiple units inside " + fileName);

        for (Element element : new ArrayList<Element>(Dom4j.elements(puElem, "class"))) {
            puElem.remove(element);
        }

        for (String className : classes.values()) {
            puElem.addElement("class").setText(className);
        }

        File outFile = new File(outFileName);
        outFile.getParentFile().mkdirs();

        OutputStream os = null;
        try {
            os = new FileOutputStream(outFileName);
            Dom4j.writeDocument(doc, true, os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    //
                }
        }
    }

    private void addClasses(String fileName, Map<String, String> classes) {
        Document doc = getDocument(fileName);
        Element rootElem = doc.getRootElement();

        Element puElem = findPersistenceUnitElement(rootElem);
        if (puElem == null)
            throw new IllegalStateException("No persistence unit named 'cuba' found among multiple units inside " + fileName);

        for (Element element : Dom4j.elements(puElem, "class")) {
            String className = element.getText();
            Class<Object> cls = ReflectionHelper.getClass(className);
            Entity annotation = cls.getAnnotation(Entity.class);
            if (annotation != null) {
                classes.put(annotation.name(), className);
            } else {
                classes.put(className, className);
            }
        }
    }

    private Element findPersistenceUnitElement(Element rootElem) {
        List<Element> puList = Dom4j.elements(rootElem, "persistence-unit");
        if (puList.size() == 1) {
            return puList.get(0);
        } else {
            for (Element element : puList) {
                if ("cuba".equals(element.attributeValue("name"))) {
                    return element;
                }
            }
        }
        return null;
    }

    private Document getDocument(String fileName) {
        Document doc;
        if (baseDir == null) {
            Resource resource = new DefaultResourceLoader().getResource(fileName);
            InputStream stream = null;
            try {
                stream = resource.getInputStream();
                doc = Dom4j.readDocument(stream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(stream);
            }
        } else {
            if (!fileName.startsWith("/"))
                fileName = "/" + fileName;
            File file = new File(baseDir, fileName);
            if (!file.exists())
                throw new IllegalArgumentException("File not found: " + file.getAbsolutePath());

            doc = Dom4j.readDocument(file);
        }
        return doc;
    }
}
