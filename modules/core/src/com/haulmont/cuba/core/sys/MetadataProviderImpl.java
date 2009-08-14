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

import com.haulmont.chile.core.loader.ChileMetadataLoader;
import com.haulmont.chile.core.loader.ClassMetadataLoader;
import com.haulmont.chile.core.loader.MetadataLoader;
import com.haulmont.chile.core.loader.ChileAnnotationsLoader;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.jpa.loader.JPAAnnotationsLoader;
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

import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.net.URL;
import java.net.URISyntaxException;

public class MetadataProviderImpl extends MetadataProvider
{
    private volatile Session session;
    private ViewRepository viewRepository;

    protected Session __getSession() {
        if (session == null) {
            synchronized (this) {
                if (session == null)
                    initialize();
            }
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
        Collection<String> packages;
        JPAMetadataLoader jpaMetadataLoader = new JPAMetadataLoader() {
            @Override
            protected ClassMetadataLoader createAnnotationsLoader(Session session) {
                return new JPAAnnotationsLoader(session) {
                    @Override
                    protected boolean isMetaPropertyField(Field field) {
                        final String name = field.getName();
                        return super.isMetaPropertyField(field) &&
                                !name.startsWith("pc") && !name.startsWith("__") && super.isMetaPropertyField(field);
                    }

                    protected URL normalize(URL url) throws IOException, URISyntaxException {
                        return super.normalize(ServerUtils.translateUrl(url));
                    }
                };
            }
        };
        packages = getJPAClassesPackageNames();
        loadMetadata(jpaMetadataLoader, packages);

        ChileMetadataLoader metadataLoader = new ChileMetadataLoader(session) {
            @Override
            protected ClassMetadataLoader createAnnotationsLoader(Session session) {
                return new ChileAnnotationsLoader(session) {
                    protected URL normalize(URL url) throws IOException, URISyntaxException {
                        return super.normalize(ServerUtils.translateUrl(url));
                    }
                };
            }
        };
        packages = getMetaClassesPackageNames();
        loadMetadata(metadataLoader, packages);

        for (MetaClass metaClass : session.getClasses()) {
            initMetaClass(metaClass);
        }
    }

    protected void loadMetadata(MetadataLoader loader, Collection<String> packages) {
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

    protected Collection<String> getJPAClassesPackageNames() {
        String path = "/" + PersistenceProvider.getPersistenceXmlPath();
        return getPackages(path, "persistence-unit", PersistenceProvider.getPersistenceUnitName());
    }

    protected Collection<String> getMetaClassesPackageNames() {
        String path = "/" + MetadataProvider.getMetadataXmlPath();
        return getPackages(path, "metadata-model");
    }

    protected Collection<String> getPackages(String path, String unitTag, String...unitNames) {
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
        //noinspection unchecked
        for (Element unitElem : (List<Element>) root.elements(unitTag)) {
            String name = unitElem.attributeValue("name");
            if (unitNames == null || unitNames.length == 0 || Arrays.binarySearch(unitNames, name) >= 0) {
                //noinspection unchecked
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
