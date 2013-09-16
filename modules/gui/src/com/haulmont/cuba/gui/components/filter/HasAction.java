/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public interface HasAction<T> {
    void doAction(T component);
}
