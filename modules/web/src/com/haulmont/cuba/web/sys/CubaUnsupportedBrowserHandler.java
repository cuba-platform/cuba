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

package com.haulmont.cuba.web.sys;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.web.WebConfig;
import com.vaadin.server.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class CubaUnsupportedBrowserHandler extends UnsupportedBrowserHandler {

    protected Resources resources;
    protected Messages messages;
    protected WebConfig webConfig;

    public CubaUnsupportedBrowserHandler() {
        super();

        resources = AppBeans.get(Resources.NAME);
        messages = AppBeans.get(Messages.NAME);
        webConfig = AppBeans.get(Configuration.class)
                .getConfig(WebConfig.class);
    }

    @Override
    protected void writeBrowserTooOldPage(VaadinRequest request, VaadinResponse response) throws IOException {
        try (BufferedWriter page = new BufferedWriter(new OutputStreamWriter(
                response.getOutputStream(), StandardCharsets.UTF_8))) {

            Locale locale = request.getLocale();

            ParamsMap paramsMap = ParamsMap.of()
                    .pair("captionMessage", messages.getMainMessage("unsupportedPage.captionMessage", locale))
                    .pair("descriptionMessage", messages.getMainMessage("unsupportedPage.descriptionMessage", locale))
                    .pair("browserListCaption", messages.getMainMessage("unsupportedPage.browserListCaption", locale))
                    .pair("chromeMessage", messages.getMainMessage("unsupportedPage.chromeMessage", locale))
                    .pair("firefoxMessage", messages.getMainMessage("unsupportedPage.firefoxMessage", locale))
                    .pair("safariMessage", messages.getMainMessage("unsupportedPage.safariMessage", locale))
                    .pair("operaMessage", messages.getMainMessage("unsupportedPage.operaMessage", locale))
                    .pair("edgeMessage", messages.getMainMessage("unsupportedPage.edgeMessage", locale))
                    .pair("explorerMessage", messages.getMainMessage("unsupportedPage.explorerMessage", locale));

            String template = resources.getResourceAsString(webConfig.getUnsupportedPagePath());

            String pageContent = TemplateHelper.processTemplate(template, paramsMap.create());

            page.write(pageContent);
        }
    }
}
