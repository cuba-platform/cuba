/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.split.CubaHorizontalSplitPanelState;
import com.vaadin.ui.HorizontalSplitPanel;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaHorizontalSplitPanel extends HorizontalSplitPanel {

    @Override
    protected CubaHorizontalSplitPanelState getState() {
        return (CubaHorizontalSplitPanelState) super.getState();
    }

    @Override
    protected CubaHorizontalSplitPanelState getState(boolean markAsDirty) {
        return (CubaHorizontalSplitPanelState) super.getState(markAsDirty);
    }

    public boolean isDockable() {
        return getState(false).dockable;
    }

    public void setDockable(boolean usePinButton) {
        if (isDockable() != usePinButton) {
            getState().dockable = usePinButton;
        }
    }

    public String getDefaultPosition() {
        return getState(false).defaultPosition;
    }

    /**
     * Set default position for dock mode
     *
     * @param defaultPosition default position
     */
    public void setDefaultPosition(String defaultPosition) {
        getState().defaultPosition = defaultPosition;
    }
}