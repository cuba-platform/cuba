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

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.sys.EventsImpl;
import com.haulmont.cuba.gui.events.UiEvent;
import com.haulmont.cuba.web.AppUI;
import org.springframework.context.ApplicationEvent;

public class WebEvents extends EventsImpl {
    @Override
    public void publish(ApplicationEvent event) {
        // check if we have active UI
        if (event instanceof UiEvent) {
            AppUI ui = AppUI.getCurrent();
            if (ui != null) {
                ui.getUiEventsMulticaster().multicastEvent(event);
            } else {
                throw new IllegalStateException("UiEvent cannot be sent since there is no active UI instance");
            }
        } else {
            super.publish(event);
        }
    }
}