/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
