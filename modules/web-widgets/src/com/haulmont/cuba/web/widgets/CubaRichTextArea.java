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

package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.richtextarea.CubaRichTextAreaServerRpc;
import com.haulmont.cuba.web.widgets.client.richtextarea.CubaRichTextAreaState;
import com.vaadin.ui.RichTextArea;
import elemental.json.Json;

import java.util.Map;

public class CubaRichTextArea extends RichTextArea {

    protected boolean lastUserActionSanitized;

    protected CubaRichTextAreaServerRpc rpc = new CubaRichTextAreaServerRpc() {
        @Override
        public void setText(String text, boolean lastUserActionSanitized) {
            setLastUserActionSanitized(lastUserActionSanitized);
            updateDiffstate("value", Json.create(text));
            if (!setValue(text, true)) {
                // The value was not updated, this could happen if the field has
                // been set to readonly on the server and the client does not
                // know about it yet. Must re-send the correct state back.
                markAsDirty();
            }
        }
    };

    public CubaRichTextArea() {
        registerRpc(rpc);
        setValue("");
    }

    @Override
    public CubaRichTextAreaState getState() {
        return (CubaRichTextAreaState) super.getState();
    }

    public void setLocaleMap(Map<String, String> localeMap) {
        getState().localeMap = localeMap;
    }

    public void setLastUserActionSanitized(boolean lastUserActionSanitized) {
        this.lastUserActionSanitized = lastUserActionSanitized;
    }

    public boolean isLastUserActionSanitized() {
        return lastUserActionSanitized;
    }
}