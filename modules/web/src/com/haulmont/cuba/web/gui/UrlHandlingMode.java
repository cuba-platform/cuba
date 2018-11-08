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

package com.haulmont.cuba.web.gui;

import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.navigation.UrlRouting;
import com.haulmont.cuba.web.sys.navigation.History;
import com.haulmont.cuba.web.sys.navigation.UrlChangeHandler;
import com.haulmont.cuba.web.widgets.CubaHistoryControl;

/**
 * Defines how URL changes should be handled.
 */
public enum UrlHandlingMode {

    /**
     * URL changes are not handled at all.
     * <p>
     * Replacement for disabled {@link WebConfig#getAllowHandleBrowserHistoryBack()}.
     */
    NONE,
    /**
     * {@link CubaHistoryControl} is used to handle changes.
     * <p>
     * Replacement for enabled {@link WebConfig#getAllowHandleBrowserHistoryBack()}.
     */
    BACK_ONLY,
    /**
     * Changes are handled by {@link UrlRouting}, {@link History} and {@link UrlChangeHandler}.
     *
     * @see UrlRouting
     * @see History
     * @see UrlChangeHandler
     */
    URL_ROUTES
}
