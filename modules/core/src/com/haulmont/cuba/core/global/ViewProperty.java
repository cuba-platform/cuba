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

public class ViewProperty implements Serializable
{
    private static final long serialVersionUID = 4098678639930287203L;

    private String name;

    private View view;

    public ViewProperty(String name, View view) {
        this.name = name;
        this.view = view;
    }

    public String getName() {
        return name;
    }

    public View getView() {
        return view;
    }

    public String toString() {
        return "ViewProperty{" +
                "name='" + name + '\'' +
                '}';
    }
}
