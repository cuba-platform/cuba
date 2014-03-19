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

/**
 * @author krivopustov
 * @version $Id$
 */
public final class PrintUtils {

    private PrintUtils() {
    }

    public static String printModels(Session session) {
        StringBuilder builder = new StringBuilder();
        for (MetaModel model : session.getModels()) {
            builder.append(model.getName()).append("\n");
        }

        return builder.toString();
    }

    public static String printClassHierarchy(MetaModel model) {
        StringBuilder builder = new StringBuilder();

        Collection<MetaClass> topLevelClasses = new LinkedList<>();
        for (MetaClass metaClass : model.getClasses()) {
            if (metaClass.getAncestor() == null) {
                topLevelClasses.add(metaClass);
            }
        }

        for (MetaClass topLevelClass : topLevelClasses) {
            builder.append(printClassHierarchy(topLevelClass));
        }

        return builder.toString();
    }

    public static String printClassHierarchy(MetaClass metaClass) {
        StringBuilder builder = new StringBuilder();

        builder.append(metaClass.getName()).append("\n");
        for (MetaClass descendantClass : metaClass.getDescendants()) {
            builder.append(shift(printClassHierarchy(descendantClass)));
        }

        return builder.toString();
    }

    public static String printClass(MetaClass metaClass) {
        StringBuilder builder = new StringBuilder();

        builder.append(metaClass.getName()).append("\n");
        for (MetaProperty metaProperty : metaClass.getOwnProperties()) {
            builder.append(shift(metaProperty.getName() + ": " + metaProperty.getRange()));
        }

        return builder.toString();
    }

    private static String shift(String string) {
        StringBuilder builder = new StringBuilder();

        final String[] strings = string.split("\n");
        for (String s : strings) {
            builder.append("    ").append(s).append("\n");
        }

        return builder.toString();
    }
}