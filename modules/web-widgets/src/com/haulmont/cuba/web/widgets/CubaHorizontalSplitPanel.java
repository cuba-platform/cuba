/*
 * Copyright (c) 2008-2017 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.split.CubaDockableSplitPanelServerRpc;
import com.haulmont.cuba.web.widgets.client.split.CubaHorizontalSplitPanelState;
import com.haulmont.cuba.web.widgets.client.split.SplitPanelDockMode;
import com.vaadin.ui.HorizontalSplitPanel;

public class CubaHorizontalSplitPanel extends HorizontalSplitPanel implements CubaDockableSplitPanel {

    public CubaHorizontalSplitPanel() {
        super();

        CubaDockableSplitPanelServerRpc serverRpc =
                (CubaDockableSplitPanelServerRpc) position -> getState().beforeDockPosition = position;

        registerRpc(serverRpc);
    }

    @Override
    protected CubaHorizontalSplitPanelState getState() {
        return (CubaHorizontalSplitPanelState) super.getState();
    }

    @Override
    protected CubaHorizontalSplitPanelState getState(boolean markAsDirty) {
        return (CubaHorizontalSplitPanelState) super.getState(markAsDirty);
    }

    @Override
    public boolean isDockable() {
        return getState(false).dockable;
    }

    @Override
    public void setDockable(boolean usePinButton) {
        if (isDockable() != usePinButton) {
            getState().dockable = usePinButton;
        }
    }

    @Override
    public void setDockMode(SplitPanelDockMode dockMode) {
        if (dockMode == SplitPanelDockMode.TOP || dockMode == SplitPanelDockMode.BOTTOM) {
            throw new IllegalStateException("Dock mode " + dockMode.name() + " is not available for the horizontally oriented SplitPanel.");
        }
        getState().dockMode = dockMode;
    }

    @Override
    public SplitPanelDockMode getDockMode() {
        return getState(false).dockMode;
    }

    @Override
    public String getDefaultPosition() {
        return getState(false).defaultPosition;
    }

    /**
     * Set default position for dock mode
     *
     * @param defaultPosition default position
     */
    @Override
    public void setDefaultPosition(String defaultPosition) {
        getState().defaultPosition = defaultPosition;
    }
}