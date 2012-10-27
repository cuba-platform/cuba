/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

/**
 * Interface defining method for formatting a value into string.
 * <p/> Used by various UI components.
 *
 * @author gorodnov
 * @version $Id$
 */
public interface Formatter<T> {

    String format(T value);
}