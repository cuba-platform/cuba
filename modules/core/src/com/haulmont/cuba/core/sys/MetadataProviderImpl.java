/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 12:06:12
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.jpa.loader.AnnotationsMetadataLoader;
import com.haulmont.chile.jpa.loader.JPAMetadataLoader;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.ViewRepository;
import org.apache.commons.lang.ArrayUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MetadataProviderImpl extends MetadataProvider
{
    private Session session;
    private ViewRepository viewRepository;

    protected synchronized Session __getSession() {
        if (session == null) {
            initialize();
        }

        return session;
    }

    protected synchronized ViewRepository __getViewRepository() {
        if (viewRepository == null) {
            viewRepository = new ViewRepository();
        }

        return viewRepository;
    }

    private void initialize() {
        Collection<String> packages = getPackageNames();
        if (packages.size() == 0)
            throw new IllegalStateException("No packages with metadata found");

        JPAMetadataLoader loader = new JPAMetadataLoader() {
            @Override
            protected AnnotationsMetadataLoader createAnnotationsLoader(Session session) {
                return new AnnotationsMetadataLoader(session) {
                    @Override
                    protected boolean isMetaPropertyField(Field field) {
                        final String name = field.getName();
                        return !name.startsWith("pc") && !name.startsWith("__") && super.isMetaPropertyField(field);
                    }

                    protected URL normalize(URL url) throws IOException, URISyntaxException {
                        return super.normalize(ServerUtils.translateUrl(url));
                    }
                };
            }
        };
        for (String p : packages) {
            String modelName = p;
            int i = p.lastIndexOf(".");
            if (i > 0) {
                modelName = p.substring(0, i);
            }
            loader.loadPackage(modelName, p);
        }
        session = loader.getSession();

        for (MetaClass metaClass : session.getClasses()) {
            initMetaClass(metaClass);
        }
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
        List<String> packages = new ArrayList<String>();
        for (Element unitElem : (List<Element>) root.elements("persistence-unit")) {
            String name = unitElem.attributeValue("name");
            if (PersistenceProvider.getPersistenceUnitName().equals(name)) {
                for (Element classElem : ((List<Element>) unitElem.elements("class"))) {
                    String className = classElem.getText().trim();
                    int i = className.lastIndexOf(".");
                    if (i <= 0)
                        throw new IllegalStateException("Invalid persistent class definition: " + className);
                    String packageName = className.substring(0, i);
                    if (!packages.contains(packageName))
                        packages.add(packageName);
                }
            }
        }
        return packages;
    }

    private void initMetaClass(MetaClass metaClass) {
        for (MetaProperty property : metaClass.getOwnProperties()) {
            initMetaProperty(metaClass, property);
        }
    }

    private void initMetaProperty(MetaClass metaClass, MetaProperty metaProperty) {
        if (metaProperty.getRange() == null || !metaProperty.getRange().isClass())
            return;

        Field field = metaProperty.getJavaField();
        
        OnDelete onDelete = field.getAnnotation(OnDelete.class);
        if (onDelete != null) {
            Map<String, Object> metaAnnotations = metaClass.getAnnotations();

            MetaProperty[] properties = (MetaProperty[]) metaAnnotations.get(OnDelete.class.getName());
            properties = (MetaProperty[]) ArrayUtils.add(properties, metaProperty);
            metaAnnotations.put(OnDelete.class.getName(), properties);
        }

        OnDeleteInverse onDeleteInverse = field.getAnnotation(OnDeleteInverse.class);
        if (onDeleteInverse != null) {
            Map<String, Object> metaAnnotations = metaProperty.getRange().asClass().getAnnotations();

            MetaProperty[] properties = (MetaProperty[]) metaAnnotations.get(OnDeleteInverse.class.getName());
            properties = (MetaProperty[]) ArrayUtils.add(properties, metaProperty);
            metaAnnotations.put(OnDeleteInverse.class.getName(), properties);
        }
    }
}
