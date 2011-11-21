/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that provides information about classes availability for different application layers.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ClassesInfo {

    private static List<Class> clientSupported = new ArrayList<Class>();

    /**
     * Register a class as available for the client layer.
     * @param aClass    class
     */
    public static synchronized void addClientSupported(Class aClass) {
        if (!clientSupported.contains(aClass))
            clientSupported.add(aClass);
    }

    /**
     * Check whether the class is available for the client layer.
     * @param aClass    class
     * @return          true if available
     */
    public static synchronized boolean isClientSupported(Class aClass) {
        return clientSupported.contains(aClass);
    }
}
