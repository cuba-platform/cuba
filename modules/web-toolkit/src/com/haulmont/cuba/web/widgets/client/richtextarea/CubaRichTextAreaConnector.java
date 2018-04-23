/*
 * Copyright (c) 2008-2016 Haulmont.
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

package com.haulmont.cuba.web.widgets.client.richtextarea;

import com.haulmont.cuba.web.widgets.CubaRichTextArea;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.richtextarea.RichTextAreaConnector;
import com.vaadin.shared.ui.Connect;

@Connect(value = CubaRichTextArea.class, loadStyle = Connect.LoadStyle.LAZY)
public class CubaRichTextAreaConnector extends RichTextAreaConnector {
    @Override
    public CubaRichTextAreaWidget getWidget() {
        return (CubaRichTextAreaWidget) super.getWidget();
    }

    @Override
    public CubaRichTextAreaState getState() {
        return (CubaRichTextAreaState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("localeMap")) {
            getWidget().setLocaleMap(getState().localeMap);
        }
    }
}
