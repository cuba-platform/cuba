/*
 * Copyright (c) 2008-2020 Haulmont.
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

import com.vaadin.shared.communication.ServerRpc;

public interface CubaRichTextAreaServerRpc extends ServerRpc {

    /**
     * Sends the updated text to the server. The user action is considered sanitized
     * if the user used buttons from the rich text area toolbar.
     *
     * @param text the text in the field
     * @param lastUserActionSanitized whether the last user action is sanitized
     */
    void setText(String text, boolean lastUserActionSanitized);
}
