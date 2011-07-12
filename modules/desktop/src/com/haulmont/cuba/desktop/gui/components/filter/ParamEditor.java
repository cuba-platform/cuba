/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class ParamEditor extends JPanel implements AbstractCondition.Listener {
    private AbstractCondition<Param> condition;
    private JComponent field;

    public ParamEditor(final AbstractCondition<Param> condition, boolean showOperation) {
        super(new MigLayout("insets 0 0 0 0, align center"));
        this.condition = condition;

        if (condition.getParam() != null) {
            field = condition.getParam().createEditComponent();
            if (showOperation) {
                JLabel opLab = new JLabel(condition.getOperationCaption());
                add(opLab);
                add(field);
            } else {
                add(field);
            }
        }
        condition.addListener(this);
    }

    @Override
    public void captionChanged() {

    }

    @Override
    public void paramChanged() {
        if (field != null) {
            remove(field);
        }
        field = condition.getParam().createEditComponent();
        add(field);

    }
}
