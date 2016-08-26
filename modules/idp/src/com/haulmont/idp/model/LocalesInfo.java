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
 */

package com.haulmont.idp.model;

import java.io.Serializable;
import java.util.Map;

public class LocalesInfo implements Serializable {
    private boolean localeSelectVisible;

    private Map<String, String> locales;

    public boolean isLocaleSelectVisible() {
        return localeSelectVisible;
    }

    public void setLocaleSelectVisible(boolean localeSelectVisible) {
        this.localeSelectVisible = localeSelectVisible;
    }

    public Map<String, String> getLocales() {
        return locales;
    }

    public void setLocales(Map<String, String> locales) {
        this.locales = locales;
    }
}