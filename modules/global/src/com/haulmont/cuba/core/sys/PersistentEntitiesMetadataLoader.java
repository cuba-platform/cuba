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

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.loader.MetaClassLoader;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.impl.SessionImpl;
import com.haulmont.chile.jpa.loader.JPAMetadataLoader;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import org.apache.commons.lang.ArrayUtils;

import org.springframework.stereotype.Component;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;

/**
 */
@Component("cuba_PersistentEntitiesMetadataLoader")
public class PersistentEntitiesMetadataLoader extends JPAMetadataLoader {

    public PersistentEntitiesMetadataLoader() {
        super(new SessionImpl());
    }

    @Override
    protected MetaClassLoader createMetaClassLoader(Session session) {
        return new CubaAnnotationsLoader(session);
    }

    @Override
    protected void initMetaProperty(MetaClass metaClass, MetaProperty metaProperty) {
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

}
