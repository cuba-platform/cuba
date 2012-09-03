/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 21.08.2009 14:31:30
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

public interface Formatter<T> {

    String format(T value);

}
