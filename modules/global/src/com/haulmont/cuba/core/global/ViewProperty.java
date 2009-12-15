/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.12.2008 11:12:23
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import java.io.Serializable;

/**
 * Defines one property in a view
 */
public class ViewProperty implements Serializable
{
    private static final long serialVersionUID = 4098678639930287203L;

    private String name;

    private View view;

    private boolean lazy;

    public ViewProperty(String name, View view) {
        this(name, view, false);
    }

    public ViewProperty(String name, View view, boolean lazy) {
        this.name = name;
        this.view = view;
        this.lazy = lazy;
    }

    public String getName() {
        return name;
    }

    public View getView() {
        return view;
    }

    public boolean isLazy() {
        return lazy;
    }

    public String toString() {
        return name;
    }
}
