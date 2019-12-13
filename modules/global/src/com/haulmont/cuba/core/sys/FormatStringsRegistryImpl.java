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

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.chile.core.datatypes.FormatStringsRegistry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component(FormatStringsRegistry.NAME)
public class FormatStringsRegistryImpl implements FormatStringsRegistry {

    protected Map<Locale, FormatStrings> formatStringsMap = new HashMap<>();

    protected boolean useLocaleLanguageOnly = true;

    @Nullable
    @Override
    public FormatStrings getFormatStrings(Locale locale) {
        if (useLocaleLanguageOnly)
            locale = Locale.forLanguageTag(locale.getLanguage());
        return formatStringsMap.get(locale);
    }

    @Override
    public FormatStrings getFormatStringsNN(Locale locale) {
        FormatStrings format = getFormatStrings(locale);
        if (format == null) {
            throw new IllegalArgumentException("Not found format strings for locale " + locale.toLanguageTag());
        }
        return format;
    }

    @Override
    public void setFormatStrings(Locale locale, FormatStrings formatStrings) {
        formatStringsMap.put(locale, formatStrings);
        if (!StringUtils.isEmpty(locale.getCountry()) || !StringUtils.isEmpty(locale.getVariant())
                || !StringUtils.isEmpty(locale.getScript()))
            useLocaleLanguageOnly = false;
    }
}
