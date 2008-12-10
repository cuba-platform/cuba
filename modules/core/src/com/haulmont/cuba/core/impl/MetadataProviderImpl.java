/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 12:06:12
 *
 * $Id$
 */
package com.haulmont.cuba.core.impl;

import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.jpa.loader.JPAMetadataLoader;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

import org.dom4j.io.SAXReader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

public class MetadataProviderImpl extends MetadataProvider
{
    private Session session;

    protected synchronized Session __getSession() {
        if (session == null) {
            initialize();
        }
        return session;
    }

    private void initialize() {
        Collection<String> packages = getPackageNames();
        if (packages.size() == 0)
            throw new IllegalStateException("No packages with metadata found");

        JPAMetadataLoader loader = new JPAMetadataLoader();
        for (String p : packages) {
            String modelName = p;
            int i = p.lastIndexOf(".");
            if (i > 0) {
                modelName = p.substring(0, i);
            }
            loader.loadPackage(modelName, p);
        }
        session = loader.getSession();
    }

    private Collection<String> getPackageNames() {
        String path = "/" + PersistenceProvider.getPersistenceXmlPath();
        InputStream stream = MetadataProviderImpl.class.getResourceAsStream(path);
        if (stream == null)
            throw new IllegalStateException("Unable to load resource: " + path);

        SAXReader reader = new SAXReader();
        Document document;
        try {
            document = reader.read(stream);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Element root = document.getRootElement();
        Set<String> packages = new HashSet<String>();
        for (Element unitElem : (List<Element>) root.elements("persistence-unit")) {
            String name = unitElem.attributeValue("name");
            if (PersistenceProvider.getPersistenceUnitName().equals(name)) {
                for (Element classElem : ((List<Element>) unitElem.elements("class"))) {
                    String className = classElem.getText().trim();
                    int i = className.lastIndexOf(".");
                    if (i <= 0)
                        throw new IllegalStateException("Invalid persistent class definition: " + className);
                    String packageName = className.substring(0, i);
                    packages.add(packageName);
                }
            }
        }
        return packages;
    }
}
