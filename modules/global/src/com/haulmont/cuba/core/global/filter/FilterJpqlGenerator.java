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

package com.haulmont.cuba.core.global.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterJpqlGenerator extends AbstractJpqlGenerator {

    public static final String OR_PATTERN_REGEX = "\\bOR\\b";
    public static final Pattern OR_PATTERN = Pattern.compile(OR_PATTERN_REGEX, Pattern.CASE_INSENSITIVE);

    @Override
    protected String generateClauseText(Clause condition) {
        if (condition.getType() == ConditionType.CUSTOM) {
            String content = condition.getContent();
            Matcher orMatcher = OR_PATTERN.matcher(content);
            if (orMatcher.find()) {
                return "(" + content + ")";
            }
            return content;
        } else {
            return condition.getContent();
        }
    }
}