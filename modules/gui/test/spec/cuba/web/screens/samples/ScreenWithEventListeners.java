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

package spec.cuba.web.screens.samples;

import com.haulmont.cuba.gui.events.UiEvent;
import com.haulmont.cuba.gui.screen.Screen;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;

public class ScreenWithEventListeners extends Screen {

    @EventListener(SomeEvent.class)
    protected void handleSomeEvent() {

    }

    @EventListener
    private void handlePrivateEvent(SomeEvent event) {

    }

    public static class SomeEvent extends ApplicationEvent implements UiEvent {
        public SomeEvent(Object source) {
            super(source);
        }
    }
}