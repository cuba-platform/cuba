/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
