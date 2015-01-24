/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.validation.ValidationAwareActionListener;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopButton extends DesktopAbstractComponent<JButton> implements Button {

    protected Action action;
    protected String icon;

    protected long responseEndTs = 0;

    public DesktopButton() {
        impl = createImplementation();
        impl.addActionListener(new ValidationAwareActionListener() {
            @Override
            public void actionPerformedAfterValidation(ActionEvent e) {
                if (action != null) {
                    if (!impl.isFocusOwner()) {
                        return;
                    }

                    if (e.getWhen() <= responseEndTs) {
                        return;
                    }

                    try {
                        action.actionPerform(DesktopButton.this);
                    } finally {
                        responseEndTs = System.currentTimeMillis();
                    }
                }
            }
        });
        DesktopComponentsHelper.adjustSize(impl);
    }

    protected JButton createImplementation() {
        return new JButton();
    }

    @Override
    public com.haulmont.cuba.gui.components.Action getAction() {
        return action;
    }

    @Override
    public void setAction(Action action) {
        this.action = action;

        String caption = action.getCaption();
        if (!StringUtils.isEmpty(caption) && StringUtils.isEmpty(impl.getText())) {
            impl.setText(caption);
        }

        String description = action.getDescription();
        if (!StringUtils.isEmpty(description) && StringUtils.isEmpty(getDescription())) {
            setDescription(description);
        }

        impl.setEnabled(action.isEnabled());
        setVisible(action.isVisible());

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
                        } else if (Action.PROP_DESCRIPTION.equals(evt.getPropertyName())) {
                            setDescription(DesktopButton.this.action.getDescription());
                        } else if (Action.PROP_ENABLED.equals(evt.getPropertyName())) {
                            setEnabled(DesktopButton.this.action.isEnabled());
                        } else if (Action.PROP_VISIBLE.equals(evt.getPropertyName())) {
                            setVisible(DesktopButton.this.action.isVisible());
                        }
                    }
                }
        );

        assignAutoDebugId();
    }

    @Override
    public String getCaption() {
        return impl.getText();
    }

    @Override
    public void setCaption(String caption) {
        impl.setText(caption);
    }

    @Override
    public String getDescription() {
        return impl.getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        impl.setToolTipText(description);
        DesktopToolTipManager.getInstance().registerTooltip(impl);
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
        if (icon != null)
            impl.setIcon(App.getInstance().getResources().getIcon(icon));
        else
            impl.setIcon(null);
    }

    @Override
    protected String getAlternativeDebugId() {
        if (StringUtils.isNotEmpty(id)) {
            return id;
        }
        if (action != null && StringUtils.isNotEmpty(action.getId())) {
            return action.getId();
        }

        return getClass().getSimpleName();
    }
}