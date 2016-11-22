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

package com.haulmont.cuba.web.toolkit.ui.client.passwordfield;

import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ui.VPasswordField;

public class CubaPasswordFieldWidget extends VPasswordField {

    public void setAutocomplete(boolean autocomplete) {
        if (autocomplete) {
            getElement().removeAttribute("autocomplete");
        } else {
            BrowserInfo browser = BrowserInfo.get();

            if (browser.isIE()
                    || (browser.isGecko() && browser.getGeckoVersion() < 47)
                    || (browser.isChrome() && browser.getBrowserMajorVersion() < 49)) {
                getElement().setAttribute("autocomplete", "off");
            } else {
                getElement().setAttribute("autocomplete", "new-password");
            }
        }
    }
}