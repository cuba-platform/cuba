/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopButton extends DesktopAbstractComponent<JButton> implements Button {

    private Action action;
    private String icon;

    public DesktopButton() {
        impl = createImplementation();
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

    protected JButton createImplementation() {
        return new JButton();
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
        impl.setVisible(action.isVisible());

        if (action.getIcon() != null) {
            setIcon(action.getIcon());
        }

        action.addOwner(this);

        action.addPropertyChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (Action.PROP_ICON.equals(evt.getPropertyName())) {
                            setIcon(DesktopButton.this.action.getIcon());
                        } else if (Action.PROP_CAPTION.equals(evt.getPropertyName())) {
                            setCaption(DesktopButton.this.action.getCaption());
                        } else if (Action.PROP_ENABLED.equals(evt.getPropertyName())) {
                            setEnabled(DesktopButton.this.action.isEnabled());
                        } else if (Action.PROP_VISIBLE.equals(evt.getPropertyName())) {
                            setVisible(DesktopButton.this.action.isVisible());
                        }
                    }
                }
        );
    }

    public String getCaption() {
        return impl.getText();
    }

    public void setCaption(String caption) {
        impl.setText(caption);
    }

    public String getDescription() {
        return impl.getToolTipText();
    }

    public void setDescription(String description) {
        impl.setToolTipText(description);
        DesktopToolTipManager.getInstance().registerTooltip(impl);
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
