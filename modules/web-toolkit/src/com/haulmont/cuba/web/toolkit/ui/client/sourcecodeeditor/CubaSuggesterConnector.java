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

package com.haulmont.cuba.web.toolkit.ui.client.sourcecodeeditor;

import com.google.gwt.core.client.GWT;
import com.vaadin.shared.ui.Connect;
import org.vaadin.aceeditor.SuggestionExtension;
import org.vaadin.aceeditor.client.SuggestPopup;
import org.vaadin.aceeditor.client.SuggesterConnector;

@SuppressWarnings("serial")
@Connect(SuggestionExtension.class)
public class CubaSuggesterConnector extends SuggesterConnector {

    @Override
    protected SuggestPopup createSuggestionPopup() {
        SuggestPopup sp = GWT.create(CubaSuggestPopup.class);
        sp.setOwner(widget);
        updatePopupPosition(sp);
        sp.setSuggestionSelectedListener(this);
        sp.show();
        return sp;
    }
}