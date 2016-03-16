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

package com.haulmont.cuba.client;

import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.config.type.TypeFactory;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 */
public class UniqueConstraintViolationPatternFactory extends TypeFactory {

    @Override
    public Object build(String value) {
        Messages messages = AppBeans.get(Messages.NAME);
        Logger log = LoggerFactory.getLogger(getClass());
        PersistenceManagerService pmService = AppBeans.get(PersistenceManagerService.NAME);
        String defaultConstraintViolationPattern = pmService.getUniqueConstraintViolationPattern();
        Pattern constraintViolationPattern;

        if (StringUtils.isBlank(value)) {
            constraintViolationPattern = Pattern.compile(defaultConstraintViolationPattern);
        } else {
            try {
                constraintViolationPattern = Pattern.compile(value);
            } catch (PatternSyntaxException e) {
                constraintViolationPattern = Pattern.compile(defaultConstraintViolationPattern);
                log.warn(String.format(messages.getMainMessage("incorrectRegexp"),
                        "cuba.uniqueConstraintViolationPattern"), e);
            }
        }

        return constraintViolationPattern;
    }
}
