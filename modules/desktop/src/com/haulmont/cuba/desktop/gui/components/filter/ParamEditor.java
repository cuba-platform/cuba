/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author devyatkin
 * @version $Id$
 */
public class ParamEditor extends JPanel implements AbstractCondition.Listener {

    protected AbstractCondition<Param> condition;
    protected JComponent field;
    protected boolean applyRequired;

    public ParamEditor(final AbstractCondition<Param> condition, boolean showOperation, boolean applyRequired) {
        super(new MigLayout("insets 0 0 0 0, align left"));
        this.condition = condition;

        if (condition.getParam() != null) {
            ParamEditorComponent editComponent = condition.getParam().createEditComponent();
            editComponent.setRequired(applyRequired && condition.isRequired());

            field = editComponent.getComponent();
            if (showOperation) {
                JLabel opLab = new JLabel(condition.getOperationCaption());
                add(opLab);
                add(field);
            } else {
                add(field);
            }
        }
        condition.addListener(this);

        this.applyRequired = applyRequired;

        //for composite components
        addPropertyChangeListener("background", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateBackground();
            }
        });
    }

    private void updateBackground() {
        if (field instanceof JPanel) {
            field.setBackground(new Color(getBackground().getRed(), getBackground().getGreen(), getBackground().getBlue(), 255));
        }
    }

    @Override
    public void captionChanged() {
    }

    @Override
    public void paramChanged() {
        if (field != null) {
            remove(field);
        }
        ParamEditorComponent editComponent = condition.getParam().createEditComponent();
        editComponent.setRequired(applyRequired && condition.isRequired());

        field = editComponent.getComponent();
        updateBackground();
        add(field);
    }

    @Override
    public void requestFocus() {
        if (field instanceof JPanel) {
            field.getComponent(0).requestFocus();
        } else {
            field.requestFocus();
        }
    }
}