/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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