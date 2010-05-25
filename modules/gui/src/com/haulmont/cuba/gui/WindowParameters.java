/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 30.03.2010 12:46:18
 *
 * $Id$
 */
package com.haulmont.cuba.gui;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WindowParameters implements Serializable {
    public static WindowParameters EMPTY = new WindowParameters(true);
    private static final long serialVersionUID = -5408074764288510987L;
    private Map<String, Serializable> params;

    public WindowParameters() {
        this(false);
    }

    private WindowParameters(boolean empty) {
        params = empty ? Collections.<String, Serializable>emptyMap() : new HashMap<String, Serializable>();
    }

    public void setParameter(String name, Serializable param) {
        params.put(name, param);
    }

    public Serializable getParameter(String name) {
        return params.get(name);
    }
}
