/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
