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

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.upload.CubaUploadState;
import com.vaadin.ui.Upload;
import org.apache.commons.lang.StringUtils;

@Deprecated
public class CubaUpload extends Upload implements UploadComponent {

    @Override
    protected CubaUploadState getState() {
        return (CubaUploadState) super.getState();
    }

    @Override
    protected CubaUploadState getState(boolean markAsDirty) {
        return (CubaUploadState) super.getState(markAsDirty);
    }

    @Override
    public String getAccept() {
        return getState(false).accept;
    }

    /**
     * Note: this is just a hint for browser, user may select files that do not meet this property
     *
     * @param accept mime types, comma separated
     */
    @Override
    public void setAccept(String accept) {
        if (!StringUtils.equals(accept, getAccept())) {
            getState().accept = accept;
        }
    }

    @Override
    public void setCaption(String caption) {
        setButtonCaption(caption);
    }

    @Override
    public String getCaption() {
        return getButtonCaption();
    }
}