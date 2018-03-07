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

package com.haulmont.cuba.web.widgets.client.textarea;

import com.haulmont.cuba.web.widgets.CubaTextArea;
import com.vaadin.client.annotations.OnStateChange;
import com.vaadin.v7.client.ui.textarea.TextAreaConnector;
import com.vaadin.shared.ui.Connect;

@Connect(CubaTextArea.class)
public class CubaTextAreaConnector extends TextAreaConnector {

    @Override
    public CubaTextAreaWidget getWidget() {
        return (CubaTextAreaWidget) super.getWidget();
    }

    @Override
    public CubaTextAreaState getState() {
        return (CubaTextAreaState) super.getState();
    }

    @OnStateChange("caseConversion")
    void updateCaseConversion() {
        getWidget().setCaseConversion(getState().caseConversion);
    }
}
