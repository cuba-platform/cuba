/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import groovy.lang.Closure;

import java.util.LinkedList;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class PostUpdateScripts {

    private List<Closure> updates = new LinkedList<>();

    public void add(Closure closure) {
        updates.add(closure);
    }

    public List<Closure> getUpdates() {
        return updates;
    }
}