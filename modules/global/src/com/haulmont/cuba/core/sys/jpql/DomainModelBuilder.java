package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.impl.MetaModelImpl;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.core.sys.jpql.model.Entity;
import com.haulmont.cuba.core.sys.jpql.model.EntityBuilder;

import java.util.Collection;

/**
 * Author: Alexander Chevelev
 * Date: 08.11.2010
 * Time: 22:24:22
 */
public class DomainModelBuilder {
    public DomainModel produce(MetaModelImpl metaModel) {
        Collection<MetaClass> classes = metaModel.getClasses();
        return produce(classes);
    }

    public DomainModel produce(Collection<MetaClass> classes) {
        DomainModel result = new DomainModel();
        EntityBuilder builder = new EntityBuilder();
        for (MetaClass aClass : classes) {
            builder.startNewEntity(aClass);

            Collection<MetaProperty> props = aClass.getProperties();
            for (MetaProperty prop : props) {
                addProperty(builder, prop);
            }

            Entity entity = builder.produce();
            result.add(entity);
        }
        return result;
    }

    private void addProperty(EntityBuilder builder, MetaProperty prop) {
        String name = prop.getName();
        String userFriendlyName = MessageUtils.getPropertyCaption(prop);
        MetaProperty.Type type = prop.getType();
        Class<?> javaType = prop.getJavaType();
        Range range = prop.getRange();
        switch (type) {
            case AGGREGATION:
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
