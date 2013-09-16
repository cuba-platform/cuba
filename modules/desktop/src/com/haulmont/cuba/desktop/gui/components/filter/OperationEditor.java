/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.AbstractOperationEditor;

import javax.swing.*;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public abstract class OperationEditor extends AbstractOperationEditor<JComponent> {

    public OperationEditor(AbstractCondition condition) {
        super(condition);
    }
}
