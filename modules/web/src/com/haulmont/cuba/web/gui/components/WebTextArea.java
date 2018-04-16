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
 *
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.web.widgets.CubaTextArea;

public class WebTextArea<V> extends WebAbstractTextArea<CubaTextArea, V>
        implements TextArea<V> {

    public WebTextArea() {
        this.component = createTextFieldImpl();

        attachValueChangeListener(component);
    }

    //    vaadin8
//    @Override
    protected CubaTextArea createTextFieldImpl() {
        return new CubaTextArea();
    }

    @Override
    public CaseConversion getCaseConversion() {
        return CaseConversion.valueOf(component.getCaseConversion().name());
    }

    @Override
    public void setCaseConversion(CaseConversion caseConversion) {
        com.haulmont.cuba.web.widgets.CaseConversion widgetCaseConversion =
                com.haulmont.cuba.web.widgets.CaseConversion.valueOf(caseConversion.name());
        component.setCaseConversion(widgetCaseConversion);
    }
}