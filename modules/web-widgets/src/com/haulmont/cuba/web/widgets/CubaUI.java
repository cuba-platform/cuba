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

import com.haulmont.cuba.web.widgets.client.clientmanager.CubaUIClientRpc;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class CubaUI extends UI {
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

    public void updateSystemMessagesLocale(SystemMessages msgs) {
        Map<String, String> localeMap = new HashMap<>(8);

        localeMap.put(CubaUIClientRpc.COMMUNICATION_ERROR_CAPTION_KEY, msgs.communicationErrorCaption);
        localeMap.put(CubaUIClientRpc.COMMUNICATION_ERROR_MESSAGE_KEY, msgs.communicationErrorMessage);

        localeMap.put(CubaUIClientRpc.SESSION_EXPIRED_ERROR_CAPTION_KEY, msgs.sessionExpiredErrorCaption);
        localeMap.put(CubaUIClientRpc.SESSION_EXPIRED_ERROR_MESSAGE_KEY, msgs.sessionExpiredErrorMessage);

        localeMap.put(CubaUIClientRpc.AUTHORIZATION_ERROR_CAPTION_KEY, msgs.authorizationErrorCaption);
        localeMap.put(CubaUIClientRpc.AUTHORIZATION_ERROR_MESSAGE_KEY, msgs.authorizationErrorMessage);

        getRpcProxy(CubaUIClientRpc.class).updateSystemMessagesLocale(localeMap);
    }

    public static class SystemMessages {
        public String communicationErrorCaption;
        public String communicationErrorMessage;

        public String authorizationErrorCaption;
        public String authorizationErrorMessage;

        public String sessionExpiredErrorCaption;
        public String sessionExpiredErrorMessage;
    }
}