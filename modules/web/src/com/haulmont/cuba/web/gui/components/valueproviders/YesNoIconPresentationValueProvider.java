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

package com.haulmont.cuba.web.gui.components.valueproviders;

import com.vaadin.data.ValueProvider;
import org.apache.commons.lang3.BooleanUtils;

public class YesNoIconPresentationValueProvider implements ValueProvider<Boolean, String> {

    protected static final String BASE_STYLE = "boolean-value";

    private final String trueString;

    private final String falseString;

    public YesNoIconPresentationValueProvider() {
        this(Boolean.TRUE.toString(), Boolean.FALSE.toString());
    }

    public YesNoIconPresentationValueProvider(String trueString, String falseString) {
        this.trueString = trueString;
        this.falseString = falseString;
    }

    @Override
    public String apply(Boolean value) {
        if (BooleanUtils.isTrue(value)) {
            return getHtmlString(getTrueString());
        } else {
            return getHtmlString(getFalseString());
        }
    }

    public String getTrueString() {
        return trueString;
    }

    public String getFalseString() {
        return falseString;
    }

    protected String getHtmlString(String value) {
        return "<div class=\"" + BASE_STYLE + " " + BASE_STYLE + "-" + value + "\"/>";
    }
}
