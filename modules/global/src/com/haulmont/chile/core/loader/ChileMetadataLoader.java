/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.loader;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ChileMetadataLoader implements MetadataLoader {

    protected Session session;
    protected ClassMetadataLoader annotationsLoader;

    public ChileMetadataLoader(@Nullable Session session) {
        if (session != null) {
            this.session = session;
            annotationsLoader = createAnnotationsLoader(session);
        }
    }

    protected ClassMetadataLoader createAnnotationsLoader(Session session) {
        return new ChileAnnotationsLoader(session);
    }

    @Override
    public Session loadXml(String xml) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Session loadXml(Element xml) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Session loadXml(InputStream xml) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Session loadPackage(String modelName, String packageName) {
        return annotationsLoader.loadPackage(modelName, packageName);
    }

    @Override
    public Session loadClass(String modelName, Class<?> clazz) {
        return annotationsLoader.loadClass(modelName, clazz);
    }

    @Override
    public Session loadClass(String modelName, String className) {
        return annotationsLoader.loadClass(modelName, className);
    }

    @Override
    public Session postProcess() {
        for (MetaClass metaClass : session.getClasses()) {
            initMetaClass(metaClass);
        }

        return session;
    }

    protected void initMetaClass(MetaClass metaClass) {
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

    protected void findMissingDescendants(MetaClass ancestor, Collection<MetaClass> missingDescendants) {
        Collection<MetaClass> descendants = ancestor.getDescendants();
        for (Object descendant : descendants) {
            missingDescendants.add((MetaClass) descendant);
            findMissingDescendants((MetaClass) descendant, missingDescendants);
        }
    }

    protected void initMetaProperty(MetaClass metaClass, MetaProperty metaProperty) {
    }

    @Override
    public Session getSession() {
        return session;
    }
}