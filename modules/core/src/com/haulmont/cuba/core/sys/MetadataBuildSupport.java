/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.Resources;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.InputStream;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean("cuba_MetadataBuildSupport")
public class MetadataBuildSupport {

    public static final String METADATA_CONFIG = "cuba.metadataConfig";
    public static final String DEFAULT_METADATA_CONFIG = "cuba-metadata.xml";

    @Inject
    private Resources resources;

    /**
     * Get the location of non-persistent metadata descriptor
     */
    public String getMetadataConfig() {
        String xmlPath = AppContext.getProperty(METADATA_CONFIG);
        if (StringUtils.isBlank(xmlPath))
            xmlPath = DEFAULT_METADATA_CONFIG;
        return xmlPath;
    }

    public List<String> getPersistentClassNames() {
        Object emfBean = AppContext.getApplicationContext().getBean("entityManagerFactory");
        return ((EntityManagerFactoryInfo) emfBean).getPersistenceUnitInfo().getManagedClassNames();
    }

    public Collection<String> getPersistentEntitiesPackages() {
        return getPackages(getPersistentClassNames());
    }

    public Collection<String> getTransientEntitiesPackages() {
        String config = getMetadataConfig();
        Collection<String> packages = new ArrayList<String>();
        StrTokenizer tokenizer = new StrTokenizer(config);
        for (String fileName : tokenizer.getTokenArray()) {
            getPackages(packages, fileName, "metadata-model");
        }
        return packages;
    }

    private Collection<String> getPackages(List<String> classNames) {
        List<String> packages = new ArrayList<String>();
        for (String className : classNames) {
            String[] parts = className.split("\\.");
            if (parts.length < 4)
                throw new IllegalStateException("Invalid persistent class definition: " + className);
            String packageName = parts[0] + "." + parts[1] + "." + parts[2];
            if (!packages.contains(packageName))
                packages.add(packageName);
        }
        return packages;
    }

    private void getPackages(Collection<String> packages, String path, String unitTag, String...unitNames) {
        Element root = readXml(path);

        for (Element element : Dom4j.elements(root, "include")) {
            String fileName = element.attributeValue("file");
            if (!StringUtils.isBlank(fileName)) {
                getPackages(packages, fileName, unitTag);
            }
        }

        //noinspection unchecked
        for (Element unitElem : (List<Element>) root.elements(unitTag)) {
            String name = unitElem.attributeValue("name");
            if (unitNames == null || unitNames.length == 0 || Arrays.binarySearch(unitNames, name) >= 0) {
                //noinspection unchecked
                for (Element classElem : ((List<Element>) unitElem.elements("class"))) {
                    String className = classElem.getText().trim();
                    String[] parts = className.split("\\.");
                    if (parts.length < 4)
                        throw new IllegalStateException("Invalid persistent class definition: " + className);
                    String packageName = parts[0] + "." + parts[1] + "." + parts[2];
                    if (!packages.contains(packageName))
                        packages.add(packageName);
                }
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
