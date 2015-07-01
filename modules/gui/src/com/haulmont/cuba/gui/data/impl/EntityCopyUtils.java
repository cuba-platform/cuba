/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.IllegalEntityStateException;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EntityCopyUtils {

    public static Entity copyCompositions(Entity source) {
        Preconditions.checkNotNullArgument(source, "source is null");

        Entity dest;
        try {
            dest = source.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        copyCompositions(source, dest);

        return dest;
    }

    public static void copyCompositions(Entity source, Entity dest) {
        Preconditions.checkNotNullArgument(source, "source is null");
        Preconditions.checkNotNullArgument(dest, "dest is null");

        for (MetaProperty srcProperty : source.getMetaClass().getProperties()) {
            String name = srcProperty.getName();
            MetaProperty dstProperty = dest.getMetaClass().getProperty(name);
            if (dstProperty != null && !dstProperty.isReadOnly()) {
                try {
                    Object value = source.getValue(name);

                    if (value != null && srcProperty.getRange().getCardinality().isMany()
                            && srcProperty.getType() == MetaProperty.Type.COMPOSITION) {
                        //noinspection unchecked
                        Collection<Entity> srcCollection = (Collection) value;
                        Collection<Entity> dstCollection;
                        if (value instanceof List)
                            dstCollection = new ArrayList<>();
                        else
                            dstCollection = new LinkedHashSet<>();
                        for (Entity item : srcCollection) {
                            Entity copy = copyCompositions(item);
                            dstCollection.add(copy);
                        }
                        dest.setValue(name, dstCollection);

                    } else {
                        dest.setValue(name, source.getValue(name));
                    }
                } catch (RuntimeException e) {
                    Throwable cause = ExceptionUtils.getRootCause(e);
                    if (cause == null)
                        cause = e;
                    // ignore exception on copy for not loaded fields
                    if (!(cause instanceof IllegalEntityStateException))
                        throw e;
                }
            }
        }
        if (source instanceof BaseGenericIdEntity && dest instanceof BaseGenericIdEntity) {
            ((BaseGenericIdEntity) dest).__detached(((BaseGenericIdEntity) source).__detached());
            ((BaseGenericIdEntity) dest).__new(((BaseGenericIdEntity) source).__new());
        }
    }

    public static void copyCompositionsBack(Entity source, Entity dest) {
        Preconditions.checkNotNullArgument(source, "source is null");
        Preconditions.checkNotNullArgument(dest, "dest is null");

        for (MetaProperty srcProperty : source.getMetaClass().getProperties()) {
            String name = srcProperty.getName();
            MetaProperty dstProperty = dest.getMetaClass().getProperty(name);
            if (dstProperty != null && !dstProperty.isReadOnly()) {
                try {
                    Object value = source.getValue(name);

                    if (value != null && srcProperty.getRange().getCardinality().isMany()
                            && srcProperty.getType() == MetaProperty.Type.COMPOSITION) {
                        ((AbstractInstance) dest).setValue(name, source.getValue(name), false);
                    } else {
                        dest.setValue(name, source.getValue(name));
                    }
                } catch (RuntimeException e) {
                    Throwable cause = ExceptionUtils.getRootCause(e);
                    if (cause == null)
                        cause = e;
                    // ignore exception on copy for not loaded fields
                    if (!(cause instanceof IllegalEntityStateException))
                        throw e;
                }
            }
        }
    }
}
