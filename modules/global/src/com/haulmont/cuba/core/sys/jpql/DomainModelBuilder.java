/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.sys.jpql.model.Entity;
import com.haulmont.cuba.core.sys.jpql.model.EntityBuilder;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author Alexander Chevelev
 * @version $Id$
 */
public class DomainModelBuilder {
    protected MetadataTools metadataTools;
    protected MessageTools messageTools;
    protected ExtendedEntities extendedEntities;

    public DomainModelBuilder(MetadataTools metadataTools, MessageTools messageTools, @Nullable ExtendedEntities extendedEntities) {
        this.metadataTools = metadataTools;
        this.messageTools = messageTools;
        this.extendedEntities = extendedEntities;
    }

    public DomainModel produce() {
        Collection<MetaClass> classes = metadataTools.getAllPersistentMetaClasses();
        DomainModel result = new DomainModel(extendedEntities);

        EntityBuilder builder = new EntityBuilder();
        for (MetaClass aClass : classes) {
            builder.startNewEntity(aClass.getName());

            Collection<MetaProperty> props = aClass.getProperties();
            for (MetaProperty prop : props) {
                if (metadataTools.isPersistent(prop))
                    addProperty(builder, prop);
            }

            Entity entity = builder.produce();
            result.add(entity);
        }
        return result;
    }

    private void addProperty(EntityBuilder builder, MetaProperty prop) {
        String name = prop.getName();
        String userFriendlyName = messageTools.getPropertyCaption(prop);
        MetaProperty.Type type = prop.getType();
        Class<?> javaType = prop.getJavaType();
        Range range = prop.getRange();
        switch (type) {
            case COMPOSITION:
            case ASSOCIATION:
                if (range.isClass()) {
                    MetaClass metaClass = range.asClass();
                    if (range.getCardinality().isMany()) {
                        builder.addCollectionReferenceAttribute(name, metaClass.getName(), userFriendlyName);
                    } else {
                        builder.addReferenceAttribute(name, metaClass.getName(), userFriendlyName);
                    }
                } else {
                    builder.addSingleValueAttribute(javaType, name, userFriendlyName);
                }
                break;
            case ENUM:
                //todo
                builder.addSingleValueAttribute(javaType, name, userFriendlyName);
                break;
            case DATATYPE:
                builder.addSingleValueAttribute(javaType, name, userFriendlyName);
                break;
        }
    }
}
