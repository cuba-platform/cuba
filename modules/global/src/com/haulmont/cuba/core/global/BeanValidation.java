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

import org.hibernate.validator.cfg.ConstraintMapping;

import javax.annotation.Nullable;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.Locale;

/**
 * Infrastructure interface for validation with JSR303 rules.
 */
public interface BeanValidation {
    String NAME = "cuba_BeanValidation";

    /**
     * Get default validator for current locale if there is current UserSession or with default locale.
     *
     * @return validator
     */
    Validator getValidator();

    /**
     * Get validator with custom constraint mapping and current locale if there is current UserSession or with default locale.
     *
     * @param constraintMapping constraint mapping
     * @return validator
     */
    Validator getValidator(ConstraintMapping constraintMapping);

    /**
     * Get validator with custom constraint mapping and additional validation options.
     *
     * @param constraintMapping constraint mapping
     * @param options           options
     * @return validator
     */
    Validator getValidator(@Nullable ConstraintMapping constraintMapping, ValidationOptions options);

    class ValidationOptions implements Serializable {
        protected Boolean failFast;
        protected Locale locale;

        public @Nullable Boolean getFailFast() {
            return failFast;
        }

        public void setFailFast(Boolean failFast) {
            this.failFast = failFast;
        }

        public @Nullable Locale getLocale() {
            return locale;
        }

        public void setLocale(Locale locale) {
            this.locale = locale;
        }
    }
}