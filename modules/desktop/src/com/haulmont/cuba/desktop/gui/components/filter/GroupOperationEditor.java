/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractCondition;

import javax.swing.*;

/**
 * Grouping condition operation editor for the desktop-client. Actually does nothing.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class GroupOperationEditor extends OperationEditor {

    public GroupOperationEditor(AbstractCondition condition) {
        super(condition);
    }

    @Override
    protected void createEditor() {
        impl = new JLabel();
    }

}
