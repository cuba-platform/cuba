/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 24.04.2009 10:00:09
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;

/**
 * Utility class to get localized messages by references defined in XML descriptors
 */
public class MessageUtils {

    /**
     * Get localized message by reference provided in full format
     * @param ref reference to message in the following format: <code>msg://message_pack/message_id</code>
     * @return localized message or input string itself if it doesn't begin with <code>msg://</code>
     */
    public static String loadString(String ref) {
        return loadString(null, ref);
    }

    /**
     * Get localized message by reference provided in full or brief format
     * @param messagesPack messages pack to use if the second parameter is in brief format
     * @param ref reference to message in the following format:
     * <ul>
     * <li>Full: <code>msg://message_pack/message_id</code>
     * <li>Brief: <code>msg://message_id</code>, in this case first parameter is taken into account
     * </ul>
     * @return localized message or input string itself if it doesn't begin with <code>msg://</code>
     */
    public static String loadString(String messagesPack, String ref) {
        if (ref.startsWith("msg://")) {
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

    public static String getEntityCaption(MetaClass metaClass) {
        String className = metaClass.getJavaClass().getName();
        int i = className.lastIndexOf('.');
        if (i > -1)
            className = className.substring(i + 1);

        return MessageProvider.getMessage(metaClass.getJavaClass(), className);
    }

    public static String getPropertyCaption(MetaClass metaClass, String propertyName) {
        MetaProperty property = metaClass.getProperty(propertyName);
        return getPropertyCaption(property);
    }

    public static String getPropertyCaption(MetaProperty property) {
        Class<?> declaringClass = property.getDeclaringClass();
        String className = declaringClass.getName();
        int i = className.lastIndexOf('.');
        if (i > -1)
            className = className.substring(i + 1);

        return MessageProvider.getMessage(declaringClass, className + "." + property.getName());
    }

    public static String getMessageRef(MetaClass metaClass, String propertyName) {
        MetaProperty property = metaClass.getProperty(propertyName);
        return getMessageRef(property);
    }

    public static String getMessageRef(MetaProperty property) {
        Class<?> declaringClass = property.getDeclaringClass();
        String className = declaringClass.getName();
        String packageName= "";
        int i = className.lastIndexOf('.');
        if (i > -1) {
            packageName = className.substring(0, i);
            className = className.substring(i + 1);
        }

        return "msg://" + packageName + "/" + className + "." + property.getName();
    }
}
