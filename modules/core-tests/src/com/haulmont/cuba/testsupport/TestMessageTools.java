/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.cuba.testsupport;

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.MessageTools;

import java.util.Locale;

public class TestMessageTools extends MessageTools {

    private Locale defaultLocale;

    public TestMessageTools(Configuration configuration) {
        super(configuration);
    }

    @Override
    public Locale getDefaultLocale() {
        if (defaultLocale != null) {
            return defaultLocale;
        }
        return super.getDefaultLocale();
    }

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

}
