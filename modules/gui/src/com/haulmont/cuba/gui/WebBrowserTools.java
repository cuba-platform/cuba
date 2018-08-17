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

package com.haulmont.cuba.gui;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Utility bean to provide common functionality related to web browser.
 */
public interface WebBrowserTools {

    String NAME = "cuba_WebBrowserTools";

    /**
     * WebPages bean!
     *
     * Open a web page in browser.
     *
     * @param url    URL of the page
     * @param params optional parameters.
     *               <br>The following parameters are recognized by Web client:
     *               <ul>
     *               <li>{@code target} - String value used as the target name in a
     *               window.open call in the client. This means that special values such as
     *               "_blank", "_self", "_top", "_parent" have special meaning. If not specified, "_blank" is used.</li>
     *               <li> {@code width} - Integer value specifying the width of the browser window in pixels</li>
     *               <li> {@code height} - Integer value specifying the height of the browser window in pixels</li>
     *               <li> {@code border} - String value specifying the border style of the window of the browser window.
     *               Possible values are "DEFAULT", "MINIMAL", "NONE".</li>
     *               </ul>
     *               Desktop client doesn't support any parameters and just ignores them.
     */
    void showWebPage(String url, @Nullable Map<String, Object> params);
}