/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.loader;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author abramov
 * @version $Id$
 */
public class ChileMetadataLoader implements MetadataLoader {

    protected Session session;
    protected MetaClassLoader metaClassLoader;

    public ChileMetadataLoader(Session session) {
        this.session = session;
        metaClassLoader = createMetaClassLoader(session);
	}

    protected MetaClassLoader createMetaClassLoader(Session session) {
        return new ChileAnnotationsLoader(session);
    }

    public void loadModel(String modelName, List<String> classNames) {
        metaClassLoader.loadPackage(modelName, classNames);
    }

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

        Collection<MetaClass> missingDescendants = new HashSet<>(1);

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
