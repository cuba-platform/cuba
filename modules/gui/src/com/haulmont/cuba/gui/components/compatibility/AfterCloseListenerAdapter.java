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

package com.haulmont.cuba.gui.components.compatibility;

import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.screen.StandardCloseAction;
import com.haulmont.cuba.gui.screen.events.AfterCloseEvent;

import java.util.Objects;
import java.util.function.Consumer;

@Deprecated
public class AfterCloseListenerAdapter implements Consumer<AfterCloseEvent> {

    public static final String UNKNOWN_CLOSE_ACTION_ID = "unknown";

    private final Window.CloseListener closeListener;

    public AfterCloseListenerAdapter(Window.CloseListener closeListener) {
        this.closeListener = closeListener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AfterCloseListenerAdapter that = (AfterCloseListenerAdapter) o;
        return Objects.equals(closeListener, that.closeListener);
    }

    @Override
    public int hashCode() {
        return Objects.hash(closeListener);
    }

    @Override
    public void accept(AfterCloseEvent afterCloseEvent) {
        if (afterCloseEvent.getCloseAction() instanceof StandardCloseAction) {
            String actionId = ((StandardCloseAction) afterCloseEvent.getCloseAction()).getActionId();
            closeListener.windowClosed(actionId);
        } else {
            closeListener.windowClosed(UNKNOWN_CLOSE_ACTION_ID);
        }
    }
}