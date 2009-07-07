/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 12:35:10
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import java.util.Collection;

public interface WindowContext {

    Collection<String> getParameterNames();
    <T> T getParameterValue(String property);

    <T> T getValue(String property);
    void setValue(String property, Object value);

    void addValueListener(ValueListener listener);
    void removeValueListener(ValueListener listener);
}
