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

package com.haulmont.cuba.gui.model;

import com.google.common.base.Strings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class working with data loaders.
 */
public class DataLoadersHelper {

    public static final Pattern PARAM_PATTERN = Pattern.compile(":([\\w$]+)");

    /**
     * Returns the loader of master entity instance.
     *
     * @param nestedContainer nested container to start from
     * @return the loader of master entity instance
     */
    @Nullable
    public static DataLoader getMasterDataLoader(Nested nestedContainer) {
        InstanceContainer masterContainer = nestedContainer.getMaster();
        while (masterContainer instanceof Nested) {
            masterContainer = ((Nested) masterContainer).getMaster();
        }

        return masterContainer instanceof HasLoader
                ? ((HasLoader) masterContainer).getLoader()
                : null;
    }

    /**
     * Extracts parameter names from the loader query text.
     */
    public static List<String> getQueryParameters(DataLoader loader) {
        List<String> parameters = new ArrayList<>();
        if (!Strings.isNullOrEmpty(loader.getQuery())) {
            Matcher matcher = PARAM_PATTERN.matcher(loader.getQuery());
            while (matcher.find()) {
                parameters.add(matcher.group(1));
            }
        }
        return parameters;
    }

    /**
     * Returns true if all query parameters found by {@link #getQueryParameters(DataLoader)} have values
     * (null is considered as a value too).
     */
    public static boolean areAllParametersSet(DataLoader loader) {
        for (String parameterName : getQueryParameters(loader)) {
            if (!loader.getParameters().containsKey(parameterName)) {
                return false;
            }
        }
        return true;
    }
}
