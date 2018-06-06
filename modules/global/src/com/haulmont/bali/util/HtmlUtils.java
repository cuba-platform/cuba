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

package com.haulmont.bali.util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

public final class HtmlUtils {

    public HtmlUtils() {
    }

    /**
     * Converts string with content to html string.
     *
     * @param text to be converted
     * @return Converted string.
     */
    public static String convertToHtml(String text) {
        String html = StringEscapeUtils.escapeHtml4(text);
        html = StringUtils.replace(html, "\n", "<br/>");
        html = StringUtils.replace(html, " ", "&nbsp;");
        html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        return html;
    }
}