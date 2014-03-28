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

    private boolean lazy;

    public ViewProperty(String name, @Nullable View view) {
        this(name, view, false);
    }

    public ViewProperty(String name, @Nullable View view, boolean lazy) {
        this.name = name;
        this.view = view;
        this.lazy = lazy;
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
     * Lazyness of a view property means that the persistence implementation will not try to fetch this property
     * from the database by single SQL statement, but instead will fetch it later with additional SQL.
     * @return  true if the property will be lazily fetched
     */
    public boolean isLazy() {
        return lazy;
    }

    @Override
    public String toString() {
        return name;
    }
}