/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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