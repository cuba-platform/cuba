/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * Defines a {@link View} property. Each view property corresponds to a
 * {@link com.haulmont.chile.core.model.MetaProperty} with the same name.
 *
 * @author krivopustov
 * @version $Id$
 */
public class ViewProperty implements Serializable {

    private static final long serialVersionUID = 4098678639930287203L;

    private String name;

    private View view;

    private FetchMode fetchMode = FetchMode.AUTO;

    @Deprecated
    private boolean lazy = false;

    public ViewProperty(String name, @Nullable View view) {
        this(name, view, FetchMode.AUTO);
    }

    @Deprecated
    public ViewProperty(String name, @Nullable View view, boolean lazy) {
        this.name = name;
        this.view = view;
    }

    public ViewProperty(String name, @Nullable View view, FetchMode fetchMode) {
        this.name = name;
        this.view = view;
        this.fetchMode = fetchMode;
    }

    /**
     * @return property name that is a metaclass attribute name
     */
    public String getName() {
        return name;
    }

    /**
     * @return view of the property if the corresponding metaclass attribute is a reference
     */
    @Nullable
    public View getView() {
        return view;
    }

    /**
     * @return fetch mode if the property is a reference
     */
    public FetchMode getFetchMode() {
        return fetchMode;
    }

    /**
     * DEPRECATED since v.6
     */
    @Deprecated
    public boolean isLazy() {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}