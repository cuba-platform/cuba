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

package com.haulmont.cuba.web.widgets.client.suggestionfield;

import com.vaadin.shared.AbstractFieldState;

import java.util.List;

public class CubaSuggestionFieldState extends AbstractFieldState {
    {
        primaryStyleName = "c-suggestionfield";
    }

    public int minSearchStringLength = 0;
    public int asyncSearchDelayMs = 300;

    public String text = "";
    public String inputPrompt = "";

    public List<String> popupStylename = null;

    public String popupWidth = "auto";

    public boolean selectFirstSuggestionOnShow = true;
}