/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop;

import java.util.Map;

/**
 * Desktop application local session
 *
 * @author artamonov
 * @version $Id$
 */
public class ApplicationSession {

    protected Map<String, Object> attributes;

    public ApplicationSession(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }
}