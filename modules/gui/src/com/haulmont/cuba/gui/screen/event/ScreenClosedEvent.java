/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.screen.event;

import com.haulmont.cuba.gui.screen.Screen;
import org.springframework.context.ApplicationEvent;

/**
 * Application event that is sent after the screen is closed,
 * i.e. when it is removed from the application UI.
 * <p>
 * {@link ApplicationEvent} analogue of the {@link Screen.AfterCloseEvent}
 */
public class ScreenClosedEvent extends ApplicationEvent {

    /**
     * Creates a new ScreenClosedEvent.
     *
     * @param screen the screen on which the event initially occurred (never {@code null})
     */
    public ScreenClosedEvent(Screen screen) {
        super(screen);
    }

    @Override
    public Screen getSource() {
        return (Screen) super.getSource();
    }
}
