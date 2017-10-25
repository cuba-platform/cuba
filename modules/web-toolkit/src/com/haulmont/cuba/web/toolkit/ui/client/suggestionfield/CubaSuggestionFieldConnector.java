/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.toolkit.ui.client.suggestionfield;

import com.haulmont.cuba.web.toolkit.ui.CubaSuggestionField;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractFieldConnector;
import com.vaadin.shared.ui.Connect;

@Connect(CubaSuggestionField.class)
public class CubaSuggestionFieldConnector extends AbstractFieldConnector {

    protected CubaSuggestionFieldServerRpc serverRpc = RpcProxy.create(CubaSuggestionFieldServerRpc.class, this);

    public CubaSuggestionFieldConnector() {
        registerRpc(CubaSuggestionFieldClientRpc.class, suggestions ->
                getWidget().showSuggestions(suggestions));
    }

    @Override
    protected void init() {
        super.init();

        CubaSuggestionFieldWidget widget = getWidget();

        widget.searchExecutor = query -> serverRpc.searchSuggestions(query);
        widget.arrowDownActionHandler = query -> serverRpc.onArrowDownKeyPressed(query);
        widget.enterActionHandler = query -> serverRpc.onEnterKeyPressed(query);
        widget.suggestionSelectedHandler = suggestion -> serverRpc.selectSuggestion(suggestion.getId());
        widget.cancelSearchHandler = () -> serverRpc.cancelSearch();
    }

    @Override
    public CubaSuggestionFieldWidget getWidget() {
        return (CubaSuggestionFieldWidget) super.getWidget();
    }

    @Override
    public CubaSuggestionFieldState getState() {
        return (CubaSuggestionFieldState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        CubaSuggestionFieldWidget widget = getWidget();
        if (stateChangeEvent.hasPropertyChanged("minSearchStringLength")) {
            widget.setMinSearchStringLength(getState().minSearchStringLength);
        }

        if (stateChangeEvent.hasPropertyChanged("asyncSearchDelayMs")) {
            widget.setAsyncSearchDelayMs(getState().asyncSearchDelayMs);
        }

        if (stateChangeEvent.hasPropertyChanged("text")) {
            widget.setValue(getState().text, false);
        }

        if (stateChangeEvent.hasPropertyChanged("inputPrompt")) {
            widget.setInputPrompt(getState().inputPrompt);
        }

        widget.setReadonly(isReadOnly());
    }
}