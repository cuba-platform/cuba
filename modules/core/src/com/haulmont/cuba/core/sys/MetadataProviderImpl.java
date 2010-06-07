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

import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.loader.ChileMetadataLoader;
import com.haulmont.chile.core.loader.ClassMetadataLoader;
import com.haulmont.chile.core.loader.MetadataLoader;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.jpa.loader.JPAAnnotationsLoader;
import com.haulmont.chile.jpa.loader.JPAMetadataLoader;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.app.ClusterListener;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

public class MetadataProviderImpl extends MetadataProvider
{
    private volatile Session session;

    private volatile ViewRepository viewRepository;

    private ClusterManagerAPI clusterManager;

    private ViewDistributor viewDistributor;

    private static Log log = LogFactory.getLog(MetadataProviderImpl.class);

    public void setClusterManager(ClusterManagerAPI clusterManager) {
        this.viewDistributor = new ViewDistributor();

        this.clusterManager = clusterManager;
        this.clusterManager.addListener(View.class, viewDistributor);
    }

    protected Session __getSession() {
        if (session == null) {
            synchronized (this) {
                if (session == null)
                    initialize();
            }
        }

        return session;
    }

    protected ViewRepository __getViewRepository() {
        if (viewRepository == null) {
            synchronized (this) {
                if (viewRepository == null) {
                    viewRepository = new ViewRepository();
                    viewRepository.addListener(viewDistributor);
                }
            }
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
                };
            }
        };
        packages = getJPAClassesPackageNames();
        loadMetadata(jpaMetadataLoader, packages);

        ChileMetadataLoader metadataLoader = new ChileMetadataLoader(session);
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
        return getPackages(PersistenceProvider.getPersistentClassNames());
    }

    protected Collection<String> getMetaClassesPackageNames() {
        String path = "/" + MetadataProvider.getMetadataXmlPath();
        Collection<String> packages = new ArrayList<String>();
        getPackages(packages, path, "metadata-model");
        return packages;
    }

    protected Collection<String> getPackages(List<String> classNames) {
        List<String> packages = new ArrayList<String>();
        for (String className : classNames) {
            int i = className.lastIndexOf(".");
            if (i <= 0)
                throw new IllegalStateException("Invalid persistent class definition: " + className);
            String packageName = className.substring(0, i);
            if (!packages.contains(packageName))
                packages.add(packageName);
        }
        return packages;
    }

    protected void getPackages(Collection<String> packages, String path, String unitTag, String...unitNames) {
        if (!path.startsWith("/"))
            path = "/" + path;

        InputStream stream = MetadataProviderImpl.class.getResourceAsStream(path);
        if (stream == null)
            throw new IllegalStateException("Unable to load resource: " + path);

        Document document = Dom4j.readDocument(stream);
        Element root = document.getRootElement();

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
                    int i = className.lastIndexOf(".");
                    if (i <= 0)
                        throw new IllegalStateException("Invalid persistent class definition: " + className);
                    String packageName = className.substring(0, i);
                    if (!packages.contains(packageName))
                        packages.add(packageName);
                }
            }
        }
    }

    private void initMetaClass(MetaClass metaClass) {
        for (MetaProperty property : metaClass.getOwnProperties()) {
            initMetaProperty(metaClass, property);
        }

        Collection<MetaClass> missingDescendants = new HashSet<MetaClass>(1);

        findMissingDescendants(metaClass, missingDescendants);

        if (!missingDescendants.isEmpty()) {
            CollectionUtils.addAll(metaClass.getDescendants(), missingDescendants.iterator());

            MetaClass ancestorMetaClass = metaClass.getAncestor();
            while (ancestorMetaClass != null) {
                CollectionUtils.addAll(ancestorMetaClass.getDescendants(), missingDescendants.iterator());
                ancestorMetaClass = ancestorMetaClass.getAncestor();
            }
        }

        MetaClass ancestorMetaClass = metaClass.getAncestor();
        while (ancestorMetaClass != null) {
            metaClass.getAncestors().add(ancestorMetaClass);
            ancestorMetaClass = ancestorMetaClass.getAncestor();
        }
    }

    private void findMissingDescendants(MetaClass ancestor, Collection<MetaClass> missingDescendants) {
        Collection<MetaClass> descendants = ancestor.getDescendants();
        for (Object descendant: descendants) {
            missingDescendants.add((MetaClass) descendant);
            findMissingDescendants((MetaClass) descendant, missingDescendants);
        }
    }

    private void initMetaProperty(MetaClass metaClass, MetaProperty metaProperty) {
        if (metaProperty.getRange() == null || !metaProperty.getRange().isClass())
            return;

        AnnotatedElement annotatedElement = metaProperty.getAnnotatedElement();
        
        OnDelete onDelete = annotatedElement.getAnnotation(OnDelete.class);
        if (onDelete != null) {
            Map<String, Object> metaAnnotations = metaClass.getAnnotations();

            MetaProperty[] properties = (MetaProperty[]) metaAnnotations.get(OnDelete.class.getName());
            properties = (MetaProperty[]) ArrayUtils.add(properties, metaProperty);
            metaAnnotations.put(OnDelete.class.getName(), properties);
        }

        OnDeleteInverse onDeleteInverse = annotatedElement.getAnnotation(OnDeleteInverse.class);
        if (onDeleteInverse != null) {
            Map<String, Object> metaAnnotations = metaProperty.getRange().asClass().getAnnotations();

            MetaProperty[] properties = (MetaProperty[]) metaAnnotations.get(OnDeleteInverse.class.getName());
            properties = (MetaProperty[]) ArrayUtils.add(properties, metaProperty);
            metaAnnotations.put(OnDeleteInverse.class.getName(), properties);
        }
    }

    public class ViewDistributor implements ViewRepository.Listener, ClusterListener<View> {

        public void viewStored(View view) {
            clusterManager.send(view);
        }

        public void receive(View message) {
            MetaClass metaClass = getSession().getClass(message.getEntityClass());
            getViewRepository().storeView(metaClass, message, false);
        }

        public byte[] getState() {
            List<View> list = getViewRepository().getAll();
            if (list.size() == 0)
                return new byte[0];

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(list);
            } catch (IOException e) {
                log.error("Error serializing views", e);
                return new byte[0];
            }
            return bos.toByteArray();
        }

        public void setState(byte[] state) {
            if (state == null || state.length == 0)
                return;

            List<View> list;
            ByteArrayInputStream bis = new ByteArrayInputStream(state);
            try {
                ObjectInputStream ois = new ObjectInputStream(bis);
                list = (List<View>) ois.readObject();
            } catch (Exception e) {
                log.error("Error deserializing views", e);
                return;
            }

            for (View view : list) {
                receive(view);
            }
        }
    }
}
