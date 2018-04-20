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

package com.haulmont.cuba.web.sys.events;

import com.vaadin.server.VaadinSession;
import org.springframework.context.ApplicationEvent;

/**
 * Event that is fired on the first request processing of the VaadinSession.
 * <br>
 * There is no SecurityContext available in the thread.
 */
public class WebSessionInitializedEvent extends ApplicationEvent {
    public WebSessionInitializedEvent(VaadinSession source) {
        super(source);
    }

    @Override
    public VaadinSession getSource() {
        return (VaadinSession) super.getSource();
    }

    public VaadinSession getSession() {
        return getSource();
    }
}