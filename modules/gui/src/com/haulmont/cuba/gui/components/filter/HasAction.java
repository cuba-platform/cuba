/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

/**
 * @author devyatkin
 * @version $Id$
 */
public interface HasAction<T> {
    void doAction(T component);
}