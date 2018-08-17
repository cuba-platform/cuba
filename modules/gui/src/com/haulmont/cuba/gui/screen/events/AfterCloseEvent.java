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

package com.haulmont.cuba.gui.screen.events;

import com.haulmont.cuba.gui.screen.CloseAction;
import com.haulmont.cuba.gui.screen.Screen;

import java.util.EventObject;

/**
 * JavaDoc
 */
public class AfterCloseEvent extends EventObject {

    protected final CloseAction closeAction;

    public AfterCloseEvent(Screen source, CloseAction closeAction) {
        super(source);
        this.closeAction = closeAction;
    }

    @Override
    public Screen getSource() {
        return (Screen) super.getSource();
    }

    public Screen getScreen() {
        return (Screen) super.getSource();
    }

    public CloseAction getCloseAction() {
        return closeAction;
    }
}