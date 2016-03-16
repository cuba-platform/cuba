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

package com.haulmont.cuba.web.toolkit.ui.client.multiupload;

import com.vaadin.shared.AbstractComponentState;

/**
 */
public class CubaMultiUploadState extends AbstractComponentState {

    public static final String BUTTON_IMAGE_KEY = "buttonImage";

    public static final String SWFUPLOAD_BOOTSTRAP_JS_KEY = "SWFUPLOAD_BOOTSTRAP_JS";
    public static final String SWFUPLOAD_FLASH_KEY = "SWFUPLOAD_FLASH";

    public String buttonCaption = "Upload";
    public int buttonWidth = 90;
    public int buttonHeight = 25;

    public int buttonTextLeft = 0;
    public int buttonTextTop = 1;

    public String buttonStyles =
            ".swfupload {" +
                "color: #1e3146;" +
                "font-size: 12px; " +
                "margin-left: 17px; " +
                "font-family: Verdana,tahoma,arial,geneva,helvetica,sans-serif,\"Trebuchet MS\";" +
            "}";

    public String buttonDisabledStyles =
            ".swfupload {" +
                "color: #c0c5cb;" +
                "font-size: 12px; " +
                "margin-left: 17px; " +
                "font-family: Verdana,tahoma,arial,geneva,helvetica,sans-serif,\"Trebuchet MS\";" +
            "}";

    public boolean buttonEnabled = true;

    public String fileTypes = "*.*";
    public String fileTypesDescription = "All files";

    public int queueSizeLimit = 100;   // 100 files
    public double fileSizeLimit = 10.0; // 10 MB
    public double queueUploadLimit = 200.0; // 100
    public String jsessionId = "";
}