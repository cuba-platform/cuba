/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.GroupBox;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.Collection;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopGroupBox
    extends DesktopVBox
    implements GroupBox
{
    private String caption;

    @Override
    public boolean isCollapsable() {
        return false;
    }

    @Override
    public void setCollapsable(boolean collapsable) {
    }

    @Override
    public boolean isExpanded() {
        return true;
    }

    @Override
    public void setExpanded(boolean expanded) {
    }

    @Override
    public void addListener(ExpandListener listener) {
    }

    @Override
    public void removeListener(ExpandListener listener) {
    }

    @Override
    public void addListener(CollapseListener listener) {
    }

    @Override
    public void removeListener(CollapseListener listener) {
    }

    @Override
    public void addAction(Action action) {
    }

    @Override
    public void removeAction(Action action) {
    }

    @Override
    public Collection<Action> getActions() {
        return null;
    }

    @Override
    public Action getAction(String id) {
        return null;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
        TitledBorder titledBorder = BorderFactory.createTitledBorder(caption);
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitlePosition(TitledBorder.TOP);
        titledBorder.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(isLayoutDebugEnabled() ? java.awt.Color.BLUE : java.awt.Color.gray),
                        BorderFactory.createEmptyBorder(0,5,5,5)
                )
        );
        titledBorder.setTitleFont(UIManager.getLookAndFeelDefaults().getFont("Panel.font"));
        impl.setBorder(titledBorder);
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {
    }

    @Override
    public void expandLayout(boolean expandLayout) {
    }
}
