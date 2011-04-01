/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.loader.ClassMetadataLoader;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.jpa.loader.JPAAnnotationsLoader;
import com.haulmont.chile.jpa.loader.JPAMetadataLoader;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Map;

/**
* <p>$Id$</p>
*
* @author krivopustov
*/
public class PersistentClassesMetadataLoader extends JPAMetadataLoader {

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
