/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.Resources;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.InputStream;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_MetadataBuildSupport")
public class MetadataBuildSupport {

    public static final String PERSISTENCE_CONFIG = "cuba.persistenceConfig";
    public static final String METADATA_CONFIG = "cuba.metadataConfig";

    @Inject
    private Resources resources;

    /**
     * @return location of persistent entities descriptor
     */
    public String getPersistenceConfig() {
        String config = AppContext.getProperty(PERSISTENCE_CONFIG);
        if (StringUtils.isBlank(config))
            throw new IllegalStateException(PERSISTENCE_CONFIG + " application property is not defined");
        return config;
    }

    /**
     * @return location of metadata descriptor
     */
    public String getMetadataConfig() {
        String config = AppContext.getProperty(METADATA_CONFIG);
        if (StringUtils.isBlank(config))
            throw new IllegalStateException(METADATA_CONFIG + " application property is not defined");
        return config;
    }

    public Map<String, List<String>> getEntityPackages() {
        Map<String, List<String>> packages = new LinkedHashMap<>();

        loadFromMetadataConfig(packages);
        loadFromPersistenceConfig(packages);

        return packages;
    }

    protected void loadFromMetadataConfig(Map<String, List<String>> packages) {
        StrTokenizer metadataFilesTokenizer = new StrTokenizer(getMetadataConfig());
        for (String fileName : metadataFilesTokenizer.getTokenArray()) {
            Element root = readXml(fileName);
            //noinspection unchecked
            for (Element element : (List<Element>) root.elements("metadata-model")) {
                String rootPackage = element.attributeValue("root-package");
                if (StringUtils.isBlank(rootPackage))
                    throw new IllegalStateException("metadata-model/@root-package is empty in " + fileName);

                List<String> classNames = packages.get(rootPackage);
                if (classNames == null) {
                    classNames = new ArrayList<>();
                    packages.put(rootPackage, classNames);
                }
                for (Element classEl : Dom4j.elements(element, "class")) {
                    classNames.add(classEl.getText().trim());
                }
            }
        }
    }

    protected void loadFromPersistenceConfig(Map<String, List<String>> packages) {
        StrTokenizer persistenceFilestokenizer = new StrTokenizer(getPersistenceConfig());
        for (String fileName : persistenceFilestokenizer.getTokenArray()) {
            Element root = readXml(fileName);
            Element puEl = root.element("persistence-unit");
            if (puEl == null)
                throw new IllegalStateException("File " + fileName + " has no persistence-unit element");

            for (Element classEl : Dom4j.elements(puEl, "class")) {
                String className = classEl.getText().trim();
                boolean included = false;
                for (String rootPackage : packages.keySet()) {
                    if (className.startsWith(rootPackage + ".")) {
                        // check if the class is already included into a model
                        for (Map.Entry<String, List<String>> entry : packages.entrySet()) {
                            if (entry.getValue().contains(className)) {
                                throw new IllegalStateException("Class " + className
                                        + " is already included into model " + entry.getKey());
                            }
                        }
                        List<String> classNames = packages.get(rootPackage);
                        if (classNames == null) {
                            classNames = new ArrayList<>();
                            packages.put(rootPackage, classNames);
                        }
                        classNames.add(className);
                        included = true;
                        break;
                    }
                }
                if (!included)
                    throw new IllegalStateException("Can not find a model for class " + className
                            + ". The class's package must be inside of some model's root package.");
            }
        }
    }

    public Element readXml(String path) {
        InputStream stream = resources.getResourceAsStream(path);
        try {
            stream = resources.getResourceAsStream(path);
            if (stream == null)
                throw new IllegalStateException("Resource not found: " + path);
            Document document = Dom4j.readDocument(stream);
            return document.getRootElement();
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    public Map<String, Map<String, String>> getEntityAnnotations() {
        Map<String, Map<String, String>> result = new HashMap<>();

        String config = getMetadataConfig();
        StrTokenizer tokenizer = new StrTokenizer(config);
        for (String fileName : tokenizer.getTokenArray()) {
            processMetadataXmlFile(result, fileName);
        }

        return result;
    }

    protected void processMetadataXmlFile(Map<String, Map<String, String>> annotations, String path) {
        Element root = readXml(path);

        for (Element element : Dom4j.elements(root, "include")) {
            String fileName = element.attributeValue("file");
            if (!StringUtils.isBlank(fileName)) {
                processMetadataXmlFile(annotations, fileName);
            }
        }

        Element annotationsEl = root.element("annotations");
        if (annotationsEl != null) {
            for (Element entityEl : Dom4j.elements(annotationsEl, "entity")) {
                String className = entityEl.attributeValue("class");
                Map<String, String> ann = new HashMap<>();
                for (Element annotEl : Dom4j.elements(entityEl, "annotation")) {
                    ann.put(annotEl.attributeValue("name"), annotEl.attributeValue("value"));
                }
                annotations.put(className, ann);
            }
        }
    }

}
