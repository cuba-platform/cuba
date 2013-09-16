/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.chile.core.model.utils;

import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;

import java.util.Collection;
import java.util.LinkedList;

public class PrintUtils {
    public static String printModels(Session session) {
        StringBuffer buffer = new StringBuffer();
        for (MetaModel model : session.getModels()) {
            buffer.append(model.getName()).append("\n");
        }

        return buffer.toString();
    }

    public static String printClassHierarchy(MetaModel model) {
        StringBuffer buffer = new StringBuffer();

        Collection<MetaClass> topLevelClasses = new LinkedList<MetaClass>();
        for (MetaClass metaClass : model.getClasses()) {
            if (metaClass.getAncestor() == null) {
                topLevelClasses.add(metaClass);
            }
        }

        for (MetaClass topLevelClass : topLevelClasses) {
            buffer.append(printClassHierarchy(topLevelClass));
        }

        return buffer.toString();
    }

    public static String printClassHierarchy(MetaClass metaClass) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(metaClass.getName()).append("\n");
        for (MetaClass descendantClass : metaClass.getDescendants()) {
            buffer.append(shift(printClassHierarchy(descendantClass)));
        }

        return buffer.toString();
    }

    public static String printClass(MetaClass metaClass) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(metaClass.getName()).append("\n");
        for (MetaProperty metaProperty : metaClass.getOwnProperties()) {
            buffer.append(shift(metaProperty.getName() + ": " + metaProperty.getRange()));
        }

        return buffer.toString();
    }

    private static String shift(String string) {
        StringBuffer buffer = new StringBuffer();

        final String[] strings = string.split("\n");
        for (String s : strings) {
            buffer.append("    ").append(s).append("\n");
        }

        return buffer.toString();
    }

}
