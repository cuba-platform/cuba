/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

/**
 * Interface to be implemented by optimistically locked entities.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface Versioned {

    String[] PROPERTIES = {"version"};

    Integer getVersion();
}
