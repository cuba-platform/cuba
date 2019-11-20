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

package com.haulmont.cuba.web.widgets.client.datefield;

import com.haulmont.cuba.web.widgets.CubaInlineDateField;
import com.vaadin.client.ui.datefield.InlineDateFieldConnector;
import com.vaadin.shared.ui.Connect;

@Connect(CubaInlineDateField.class)
public class CubaInlineDateFieldConnector extends InlineDateFieldConnector {
    @Override
    public CubaDateFieldCalendarWidget getWidget() {
        return (CubaDateFieldCalendarWidget) super.getWidget();
    }
}
