/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.security.entity;

/**
 * @author artamonov
 * @version $Id$
 */
public interface AssignableTarget {
    boolean isAssigned();

    String getCaption();
}