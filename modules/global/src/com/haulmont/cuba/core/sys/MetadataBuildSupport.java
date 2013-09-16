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

    public static final String METADATA_CONFIG = "cuba.metadataConfig";

    @Inject
    private Resources resources;

    /**
     * Get the location of non-persistent metadata descriptor
     */
    public String getMetadataConfig() {
        String config = AppContext.getProperty(METADATA_CONFIG);
        if (StringUtils.isBlank(config))
            throw new IllegalStateException(METADATA_CONFIG + " application property is not defined");
        return config;
    }

    public List<String> getEntityPackages() {
        String config = getMetadataConfig();
        List<String> packages = new ArrayList<>();
        StrTokenizer tokenizer = new StrTokenizer(config);
        for (String fileName : tokenizer.getTokenArray()) {
            Element root = readXml(fileName);
            //noinspection unchecked
            for (Element element : (List<Element>) root.elements("metadata-model")) {
                String rootPackage = element.attributeValue("root-package");
                if (StringUtils.isBlank(rootPackage))
                    throw new IllegalStateException("metadata-model/@root-package is empty in " + fileName);

                if (!packages.contains(rootPackage))
                    packages.add(rootPackage);
            }
        }
        return packages;
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
