/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.chile.core.loader;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.impl.MetaClassImpl;
import com.haulmont.chile.core.model.impl.MetaPropertyImpl;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesMetaProperty;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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

    @Override
    public void loadModel(String modelName, List<String> classNames) {
        metaClassLoader.loadPackage(modelName, classNames);
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
            ((MetaClassImpl) metaClass).addAncestor(ancestorMetaClass);
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
        // init inverse properties
        MetaProperty inverseProp = metaProperty.getInverse();
        if (inverseProp != null && inverseProp.getInverse() == null) {
            ((MetaPropertyImpl) inverseProp).setInverse(metaProperty);
        }
    }

    @Override
    public Session getSession() {
        return session;
    }
}