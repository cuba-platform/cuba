/*
 * Copyright (c) 2008-2018 Haulmont.
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

import com.haulmont.cuba.web.widgets.client.appui.CubaUIClientRpc;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import java.util.Collection;

public class CubaUI extends UI {
    @Override
    protected void init(VaadinRequest request) {

    }

    /**
     * Check if users can interact with the component - there are no modal windows that prevent user action.
     *
     * @param component component
     * @return whether it accessible or not
     */
    public boolean isAccessibleForUser(Component component) {
        Collection<Window> windows = this.getWindows();
        if (windows.isEmpty()) {
            // there are no windows - all components are accessible
            return true;
        }

        boolean hasModalWindows = windows.stream().anyMatch(Window::isModal);
        if (!hasModalWindows) {
            // there are no modal windows - all components are accessible
            return true;
        }

        Component windowOrUI = CubaUIUtils.getWindowOrUI(component);
        if (windowOrUI == null) {
            // something went wrong
            return false;
        }

        if (windowOrUI instanceof UI) {
            // there are modal windows, component belongs to UI
            return false;
        }

        if (windowOrUI instanceof Window) {
            Window currentWindow = (Window) windowOrUI;

            if (!currentWindow.isModal()) {
                // there are modal windows, component belongs to non-modal window
                return false;
            }

            // CAUTION we cannot sort windows in UI, because they are ordered only on client side
        }

        // we cannot reliably check if access is permitted
        return true;
    }

    /**
     * INTERNAL.
     */
    public void discardAccumulatedEvents() {
        getRpcProxy(CubaUIClientRpc.class).discardAccumulatedEvents();
    }
}