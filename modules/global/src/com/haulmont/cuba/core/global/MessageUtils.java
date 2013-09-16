/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;

import javax.annotation.Nullable;

/**
 * DEPRECATED - use {@link MessageTools}
 */
@Deprecated
public class MessageUtils {

    /**
     * DEPRECATED - use {@link Messages#getMainMessagePack()}
     */
    @Deprecated
    public static String getMessagePack() {
        return AppBeans.get(Messages.class).getMainMessagePack();
    }

    /**
     * DEPRECATED - use {@link MessageTools#loadString(java.lang.String)}
     */
    @Deprecated
    public static String loadString(String ref) {
        return AppBeans.get(MessageTools.class).loadString(ref);
    }

    /**
     * DEPRECATED - use {@link MessageTools#loadString(java.lang.String)}
     */
    @Deprecated
    public static String loadString(@Nullable String messagesPack, String ref) {
        return AppBeans.get(MessageTools.class).loadString(messagesPack, ref);
    }

    /**
     * DEPRECATED - use {@link MessageTools#getEntityCaption(com.haulmont.chile.core.model.MetaClass)}
     */
    @Deprecated
    public static String getEntityCaption(MetaClass metaClass) {
        return AppBeans.get(MessageTools.class).getEntityCaption(metaClass);
    }

    /**
     * DEPRECATED - use {@link MessageTools#getPropertyCaption(com.haulmont.chile.core.model.MetaClass, java.lang.String)}
     */
    @Deprecated
    public static String getPropertyCaption(MetaClass metaClass, String propertyName) {
        return AppBeans.get(MessageTools.class).getPropertyCaption(metaClass, propertyName);
    }

    /**
     * DEPRECATED - use {@link MessageTools#getPropertyCaption(com.haulmont.chile.core.model.MetaClass, java.lang.String)}
     */
    @Deprecated
    public static String getPropertyCaption(MetaProperty property) {
        return AppBeans.get(MessageTools.class).getPropertyCaption(property);
    }

    /**
     * DEPRECATED - use {@link MessageTools#hasPropertyCaption(com.haulmont.chile.core.model.MetaProperty)}
     */
    @Deprecated
    public static boolean hasPropertyCaption(MetaProperty property) {
        return AppBeans.get(MessageTools.class).hasPropertyCaption(property);
    }

    /**
     * DEPRECATED - use {@link MessageTools#getMessageRef(com.haulmont.chile.core.model.MetaClass, java.lang.String)}
     */
    @Deprecated
    public static String getMessageRef(MetaClass metaClass, String propertyName) {
        return AppBeans.get(MessageTools.class).getMessageRef(metaClass, propertyName);
    }

    /**
     * DEPRECATED - use {@link MessageTools#getMessageRef(com.haulmont.chile.core.model.MetaClass, java.lang.String)}
     */
    @Deprecated
    public static String getMessageRef(MetaProperty property) {
        return AppBeans.get(MessageTools.class).getMessageRef(property);
    }

    /**
     * DEPRECATED - use {@link MessageTools#getLocValue(java.lang.String, com.haulmont.chile.core.model.Instance)}
     */
    @Deprecated
    public static String getLocValue(String attribute, Instance instance) {
        return AppBeans.get(MessageTools.class).getLocValue(attribute, instance);
    }

    /**
     * DEPRECATED - use {@link MessageTools#inferMessagePack(java.lang.String, com.haulmont.chile.core.model.Instance)}
     */
    @Deprecated
    public static String inferMessagePack(String attribute, Instance instance) {
        return AppBeans.get(MessageTools.class).inferMessagePack(attribute, instance);
    }

    /**
     * DEPRECATED - use {@link MetadataTools#format(java.lang.Object, com.haulmont.chile.core.model.MetaProperty)}
     */
    @Deprecated
    public static String format(Object value, MetaProperty property) {
        return AppBeans.get(MetadataTools.class).format(value, property);
    }
}
