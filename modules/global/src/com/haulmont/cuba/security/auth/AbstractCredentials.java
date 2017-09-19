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

package com.haulmont.cuba.security.auth;

import java.util.Locale;
import java.util.Map;

public abstract class AbstractCredentials implements LocalizedCredentials {
    private Locale locale;
    private boolean overrideLocale = true;

    private Map<String, Object> params;

    public AbstractCredentials(Locale locale, Map<String, Object> params) {
        this.locale = locale;
        this.params = params;
    }

    public AbstractCredentials() {
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public boolean isOverrideLocale() {
        return overrideLocale;
    }

    public void setOverrideLocale(boolean overrideLocale) {
        this.overrideLocale = overrideLocale;
    }
}