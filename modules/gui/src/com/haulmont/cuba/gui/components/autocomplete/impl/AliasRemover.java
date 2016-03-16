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

package com.haulmont.cuba.gui.components.autocomplete.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class AliasRemover {
    protected Pattern aliasPattern = Pattern.compile("as\\s+\"?([\\w|\\d|_|\\.]+)\"?\\s*");

    public HintRequest replaceAliases(HintRequest input) {
        HintRequest result = new HintRequest();
        result.setQuery(input.getQuery());
        result.setPosition(input.getPosition());
        result.setExpectedTypes(input.getExpectedTypes());

        int indexOfFrom = input.getQuery().indexOf("from");
        Matcher matcher = aliasPattern.matcher(input.getQuery());
        String resultQuery = result.getQuery();
        while (matcher.find()) {
            String alias = matcher.group();
            int regionStart = matcher.start();
            int regionEnd = matcher.end();

            if (regionEnd <= indexOfFrom || indexOfFrom == -1) {
                if (result.getPosition() > regionEnd) {
                    result.setPosition(result.getPosition() - alias.length());
                    resultQuery = matcher.replaceFirst("");
                    matcher.reset(resultQuery);
                } else if (result.getPosition() > regionStart) {
                    result.setPosition(regionStart - 1);
                    resultQuery = matcher.replaceFirst("");
                    matcher.reset(resultQuery);
                } else {
                    resultQuery = matcher.replaceFirst("");
                    matcher.reset(resultQuery);
                }
                indexOfFrom = resultQuery.indexOf("from");
            }
        }

        result.setQuery(resultQuery);

        return result;
    }
}