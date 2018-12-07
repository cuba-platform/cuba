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

package com.haulmont.cuba.web.widgets.client.javascriptcomponent;

import com.haulmont.cuba.web.widgets.CubaJavaScriptComponent;
import com.vaadin.client.JavaScriptConnectorHelper;
import com.vaadin.client.ui.HasRequiredIndicator;
import com.vaadin.client.ui.JavaScriptComponentConnector;
import com.vaadin.shared.ui.Connect;

import java.util.Collections;
import java.util.List;

@Connect(CubaJavaScriptComponent.class)
public class CubaJavaScriptComponentConnector extends JavaScriptComponentConnector implements HasRequiredIndicator {

    @Override
    protected JavaScriptConnectorHelper createJavaScriptConnectorHelper() {
        return new JavaScriptConnectorHelper(this) {
            @Override
            protected void showInitProblem(List<String> attemptedNames) {
                getWidget().showNoInitFound(attemptedNames);
            }

            @Override
            protected List<String> getPotentialInitFunctionNames() {
                return Collections.singletonList(getState().initFunctionName);
            }
        };
    }

    @Override
    public CubaJavaScriptComponentState getState() {
        return (CubaJavaScriptComponentState) super.getState();
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return getState().requiredIndicatorVisible;
    }
}
