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

package com.haulmont.cuba.core.config.type;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class IntegerListTypeFactory extends TypeFactory {

    private Logger log = LoggerFactory.getLogger(IntegerListTypeFactory.class);

    @Override
    public Object build(String string) {
        List<Integer> integerList = new ArrayList<>();
        if (StringUtils.isNotEmpty(string)) {
            String[] elements = string.split(" ");
            for (String element : elements) {
                if (StringUtils.isNotEmpty(element)) {
                    try {
                        Integer value = Integer.parseInt(element);
                        integerList.add(value);
                    } catch (NumberFormatException e) {
                        log.debug("Invalid integer list property: " + string);
                        return Collections.emptyList();
                    }
                }
            }
        }
        return integerList;
    }
}