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

package com.haulmont.cuba.core.global;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.haulmont.cuba.core.sys.AppContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for accessing registered data source names.
 */
public class Stores {

    public static final String MAIN = "_MAIN_";

    public static final String PROP_NAME = "cuba.storeName";

    private static final Splitter SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();

    /**
     * @return true if the given name is the name of the main data store
     */
    public static boolean isMain(String name) {
        return MAIN.equals(name);
    }

    /**
     * @return the list of all data store names including main
     * @see #getAdditional()
     */
    public static List<String> getAll() {
        List<String> all = new ArrayList<>();
        all.add(MAIN);
        all.addAll(getAdditional());
        return all;
    }

    /**
     * @return the list of additional data store names registered in the {@code cuba.additionalStores} app property
     */
    public static List<String> getAdditional() {
        String dbProp = AppContext.getProperty("cuba.additionalStores");
        if (!Strings.isNullOrEmpty(dbProp))
            return SPLITTER.splitToList(dbProp);
        else
            return Collections.emptyList();
    }
}
