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

package com.haulmont.cuba.core.entity.annotation;

import org.apache.commons.lang.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a field which is marked by this annotation contains currency value
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@MetaAnnotation
public @interface CurrencyValue {

    /**
     * Currency name: USD, GBP, EUR, $ or another currency sign.
     */
    String currency() default StringUtils.EMPTY;

    /**
     * Default currency label position.
     */
    CurrencyLabelPosition labelPosition() default CurrencyLabelPosition.RIGHT;
}
