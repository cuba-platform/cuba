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

package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.jpql.model.Entity;
import com.haulmont.cuba.core.sys.jpql.model.EntityBuilder;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 */
public class DomainModelBuilder {
    protected Metadata metadata;
    protected MetadataTools metadataTools;
    protected MessageTools messageTools;
    protected ExtendedEntities extendedEntities;
    protected boolean loadCaptions;

    public DomainModelBuilder() {
        this(AppBeans.get(Metadata.NAME), AppBeans.get(MessageTools.NAME), AppBeans.get(ExtendedEntities.NAME));
    }

    public DomainModelBuilder(boolean loadCaptions) {
        this(AppBeans.get(Metadata.NAME), AppBeans.get(MessageTools.NAME), AppBeans.get(ExtendedEntities.NAME), loadCaptions);
    }

    public DomainModelBuilder(Metadata metadata, MessageTools messageTools, @Nullable ExtendedEntities extendedEntities) {
        this(metadata, messageTools, extendedEntities, true);
    }

    private DomainModelBuilder(Metadata metadata, MessageTools messageTools, @Nullable ExtendedEntities extendedEntities, boolean loadCaptions) {
        this.metadata = metadata;
        this.metadataTools = metadata.getTools();
        this.messageTools = messageTools;
        this.extendedEntities = extendedEntities;
        this.loadCaptions = loadCaptions;
    }

    public DomainModel produce() {
        Collection<MetaClass> classes = metadata.getSession().getClasses();
        DomainModel result = new DomainModel(extendedEntities);

        EntityBuilder builder = new EntityBuilder();
        for (MetaClass aClass : classes) {
            builder.startNewEntity(aClass.getName());

            Collection<MetaProperty> props = aClass.getProperties();
            for (MetaProperty prop : props) {
                if (metadataTools.isPersistent(prop))
                    addProperty(builder, aClass, prop);
            }

            Entity entity = builder.produce();
            result.add(entity);
        }
        return result;
    }

    private void addProperty(EntityBuilder builder, MetaClass metaClass, MetaProperty prop) {
        String name = prop.getName();
        String userFriendlyName = null;
        if (loadCaptions) {
            userFriendlyName = messageTools.getPropertyCaption(metaClass, prop.getName());
        }
        boolean isEmbedded = metadataTools.isEmbedded(prop);
        MetaProperty.Type type = prop.getType();
        Class<?> javaType = prop.getJavaType();
        Range range = prop.getRange();
        switch (type) {
            case COMPOSITION:
            case ASSOCIATION:
                if (range.isClass()) {
                    MetaClass rangeClass = range.asClass();
                    if (range.getCardinality().isMany()) {
                        builder.addCollectionReferenceAttribute(name, rangeClass.getName(), userFriendlyName);
                    } else {
                        builder.addReferenceAttribute(name, rangeClass.getName(), userFriendlyName, isEmbedded);
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
