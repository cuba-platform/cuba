/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class PickerAction extends AbstractAction {

    protected JButton button;
    protected String text = "...";

    public PickerAction() {
        initButton();
    }

    public PickerAction(String name) {
        super(name);
        initButton();
    }

    public PickerAction(String name, Icon icon) {
        super(name, icon);
        initButton();
    }

    public PickerAction(String name, String text) {
        super(name);
        this.text = text;
        initButton();
    }

    protected void initButton() {
        button = new JButton();
        button.setPreferredSize(new Dimension(20, 20));
        button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PickerAction.this.actionPerformed(e);
                    }
                }
        );
        Icon icon = (Icon) getValue(Action.SMALL_ICON);
        if (icon == null) {
            button.setText(text);
        } else {
            button.setIcon(icon);
        }
    }

    public JButton getButton() {
        return button;
    }
}
