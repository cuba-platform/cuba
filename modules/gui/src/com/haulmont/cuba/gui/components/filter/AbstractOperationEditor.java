/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public abstract class AbstractOperationEditor<T> {

    protected T impl;
    protected AbstractCondition condition;

    public AbstractOperationEditor(AbstractCondition condition) {
        this.condition = condition;
        createEditor();
    }

    protected abstract void createEditor();

    public T getImpl() {
        return impl;
    }

}
