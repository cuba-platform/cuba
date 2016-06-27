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

package com.haulmont.cuba.web.toolkit.ui.client.fileupload;

import com.vaadin.shared.Connector;
import com.vaadin.shared.annotations.NoLayout;
import com.vaadin.shared.ui.TabIndexState;

import java.util.Set;

public class CubaFileUploadState extends TabIndexState {

    {
        primaryStyleName = "cuba-fileupload";
    }

    @NoLayout
    public String iconAltText = null;

    @NoLayout
    public boolean multiSelect = false;

    // permitted mime types, comma separated
    @NoLayout
    public String accept = null;

    @NoLayout
    public String progressWindowCaption;

    @NoLayout
    public String cancelButtonCaption;

    @NoLayout
    public String unableToUploadFileMessage;

    @NoLayout
    public long fileSizeLimit;

    @NoLayout
    public Set<String> permittedExtensions;

    @NoLayout
    public Connector dropZone;

    @NoLayout
    public String dropZonePrompt;
}