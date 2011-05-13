/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.global.MetadataProvider;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class MetadataBuildHelper {

    public static Collection<String> getPersistentEntitiesPackages() {
        return getPackages(PersistenceProvider.getPersistentClassNames());
    }

    public static Collection<String> getTransientEntitiesPackages() {
        String config = MetadataProvider.getMetadataConfig();
        Collection<String> packages = new ArrayList<String>();
        StrTokenizer tokenizer = new StrTokenizer(config);
        for (String fileName : tokenizer.getTokenArray()) {
            getPackages(packages, fileName, "metadata-model");
        }
        return packages;
    }

    private static Collection<String> getPackages(List<String> classNames) {
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

    private static void getPackages(Collection<String> packages, String path, String unitTag, String...unitNames) {
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

    public static Element readXml(String path) {
        Resource resource = new DefaultResourceLoader().getResource(path);
        InputStream stream = null;
        try {
            stream = resource.getInputStream();
            Document document = Dom4j.readDocument(stream);
            return document.getRootElement();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }
}
