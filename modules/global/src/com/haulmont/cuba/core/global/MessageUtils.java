/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.*;
import com.haulmont.cuba.core.entity.annotation.LocalizedValue;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;

/**
 * Utility class to obtain localized messages.
 *
 * $Id$
 */
public class MessageUtils {

    private static Log log = LogFactory.getLog(MessageUtils.class);

    /**
     * Prefix pointing that the string is actually a key in a localized messages pack.
     */
    public static final String MARK = "msg://";

    private static String messagePack;

    public static void setMessagePack(String messagePack) {
        MessageUtils.messagePack = messagePack;
    }

    /**
     * @return main message pack set for the application
     */
    public static String getMessagePack() {
        return messagePack;
    }

    /**
     * Get localized message by reference provided in the full format.
     * @param ref   reference to message in the following format: <code>msg://message_pack/message_id</code>
     * @return      localized message or input string itself if it doesn't begin with <code>msg://</code>
     */
    public static String loadString(String ref) {
        return loadString(null, ref);
    }

    /**
     * Get localized message by reference provided in full or brief format.
     * @param messagesPack  messages pack to use if the second parameter is in brief format
     * @param ref           reference to message in the following format:
     * <ul>
     * <li>Full: <code>msg://message_pack/message_id</code>
     * <li>Brief: <code>msg://message_id</code>, in this case the first parameter is taken into account
     * </ul>
     * @return localized message or input string itself if it doesn't begin with <code>msg://</code>
     */
    public static String loadString(@Nullable String messagesPack, String ref) {
        if (ref.startsWith(MARK)) {
            String path = ref.substring(6);
            final String[] strings = path.split("/");
            if (strings.length == 1 && messagesPack != null) {
                ref = MessageProvider.getMessage(messagesPack, strings[0]);
            } else if (strings.length == 2) {
                ref = MessageProvider.getMessage(strings[0], strings[1]);
            } else {
                throw new UnsupportedOperationException("Unsupported resource string format: " + ref);
            }
        }
        return ref;
    }

    /**
     * @return a localized name of an entity. Messages pack must be located in the same package as entity.
     */
    public static String getEntityCaption(MetaClass metaClass) {
        String className = metaClass.getJavaClass().getName();
        int i = className.lastIndexOf('.');
        if (i > -1)
            className = className.substring(i + 1);

        return MessageProvider.getMessage(metaClass.getJavaClass(), className);
    }

    /**
     * Get localized name of an entity property. Messages pack must be located in the same package as entity.
     * @param metaClass     MetaClass containing the property
     * @param propertyName  property's name
     * @return              localized name
     */
    public static String getPropertyCaption(MetaClass metaClass, String propertyName) {
        Class<?> ownClass = metaClass.getJavaClass();
        String className = ownClass.getName();
        int i = className.lastIndexOf('.');
        if (i > -1)
            className = className.substring(i + 1);

        String key = className + "." + propertyName;
        String message = MessageProvider.getMessage(ownClass, key);
        if (!message.equals(key)) return message;

        MetaPropertyPath propertyPath = metaClass.getPropertyPath(propertyName);
        return getPropertyCaption(propertyPath.getMetaProperty());
    }

    /**
     * Get localized name of an entity property. Messages pack must be located in the same package as entity.
     * @param property  MetaProperty
     * @return          localized name
     */
    public static String getPropertyCaption(MetaProperty property) {
        Class<?> declaringClass = property.getDeclaringClass();
        if (declaringClass == null)
            return property.getName();

        String className = declaringClass.getName();
        int i = className.lastIndexOf('.');
        if (i > -1)
            className = className.substring(i + 1);

        return MessageProvider.getMessage(declaringClass, className + "." + property.getName());
    }

    /**
     * Checks whether a localized name of the property exists.
     * @param property  MetaProperty
     * @return          true if {@link #getPropertyCaption(com.haulmont.chile.core.model.MetaProperty)} returns a
     * string which has no dots inside or the first part befor a dot is not equal to the declaring class
     */
    public static boolean hasPropertyCaption(MetaProperty property) {
        Class<?> declaringClass = property.getDeclaringClass();
        if (declaringClass == null)
            return false;

        String caption = getPropertyCaption(property);
        int i = caption.indexOf('.');
        if (i > 0 && declaringClass.getSimpleName().equals(caption.substring(0, i)))
            return false;
        else
            return true;
    }

    /**
     * Get message reference of an entity property.
     * Messages pack part of the reference corresponds to the entity's package.
     * @param metaClass     MetaClass containing the property
     * @param propertyName  property's name
     * @return              message key in the form <code>msg://message_pack/message_id</code>
     */
    public static String getMessageRef(MetaClass metaClass, String propertyName) {
        MetaProperty property = metaClass.getProperty(propertyName);
        if (property == null) {
            throw new RuntimeException("Property " + propertyName + " is wrong for metaclass " + metaClass);
        }
        return getMessageRef(property);
    }

    /**
     * Get message reference of an entity property.
     * Messages pack part of the reference corresponds to the entity's package.
     * @param property  MetaProperty
     * @return          message key in the form <code>msg://message_pack/message_id</code>
     */
    public static String getMessageRef(MetaProperty property) {
        Class<?> declaringClass = property.getDeclaringClass();
        if (declaringClass == null)
            return MARK + property.getName();

        String className = declaringClass.getName();
        String packageName= "";
        int i = className.lastIndexOf('.');
        if (i > -1) {
            packageName = className.substring(0, i);
            className = className.substring(i + 1);
        }

        return MARK + packageName + "/" + className + "." + property.getName();
    }

    /**
     * Get localized value of an attribute based on {@link LocalizedValue} annotation.
     * @param attribute attribute name
     * @param instance  entity instance
     * @return          localized value or the value itself, if the value is null or the message pack
     * can not be inferred
     */
    public static String getLocValue(String attribute, Instance instance) {
        String value = instance.getValue(attribute);
        if (value == null)
            return null;

        String mp = inferMessagePack(attribute, instance);
        if (mp == null)
            return value;
        else
            return MessageProvider.getMessage(mp, value);
    }

    /**
     * Returns message pack inferred from {@link LocalizedValue} annotation.
     * @param attribute attribute name
     * @param instance  entity instance
     * @return          inferred message pack or null
     */
    public static String inferMessagePack(String attribute, Instance instance) {
        MetaClass metaClass = instance.getMetaClass();
        MetaProperty property = metaClass.getProperty(attribute);
        LocalizedValue annotation = property.getAnnotatedElement().getAnnotation(LocalizedValue.class);
        if (annotation != null) {
            if (!StringUtils.isBlank(annotation.messagePack()))
                return annotation.messagePack();
            else if (!StringUtils.isBlank(annotation.messagePackExpr())) {
                try {
                    return instance.getValueEx(annotation.messagePackExpr());
                } catch (Exception e) {
                    log.error("Unable to infer message pack from expression: " + annotation.messagePackExpr(), e);
                }
            }
        }
        return null;
    }

    /**
     * Formats a value according to the property type.
     * @param value     object to format
     * @param property  metadata
     * @return          formatted value as string, or null if value is null
     */
    public static String format(Object value, MetaProperty property) {
        if (value == null)
            return null;

        Range range = property.getRange();
        if (range.isDatatype()) {
            Datatype datatype = range.asDatatype();
            return datatype.format(value, UserSessionProvider.getLocale());
        } else if (range.isEnum()) {
            return MessageProvider.getMessage((Enum) value);
        } else {
            if (value instanceof Instance)
                return ((Instance) value).getInstanceName();
            else
                return value.toString();
        }
    }
}
