/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopButton extends DesktopAbstractComponent<JButton> implements Button {

    private Action action;
    private String icon;

    public DesktopButton() {
        impl = new JButton();
        impl.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (action != null) {
                            action.actionPerform(DesktopButton.this);
                        }
                    }
                }
        );
        DesktopComponentsHelper.adjustSize(impl);
    }

    public com.haulmont.cuba.gui.components.Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;

        final String caption = action.getCaption();
        if (!StringUtils.isEmpty(caption) && StringUtils.isEmpty(impl.getText())) {
            impl.setText(caption);
        }

        impl.setEnabled(action.isEnabled());

        if (action.getIcon() != null) {
            setIcon(action.getIcon());
        }

        action.setOwner(this);
    }

    public String getCaption() {
        return impl.getText();
    }

    public void setCaption(String caption) {
        impl.setText(caption);
    }

    public String getDescription() {
        return null;
    }

    public void setDescription(String description) {
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
        if (icon != null)
            impl.setIcon(App.getInstance().getResources().getIcon(icon));
        else
            impl.setIcon(null);
    }
}
