/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
