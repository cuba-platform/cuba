/*
 * Copyright (c) 2008-2019 Haulmont.
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

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.validation.CubaValidationMessagesInterpolator;
import com.haulmont.cuba.core.sys.validation.CubaValidationTimeProvider;
import com.haulmont.cuba.core.sys.validation.CubaValidationTraversableResolver;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.context.ConstraintDefinitionContext;
import org.hibernate.validator.internal.constraintvalidators.bv.time.future.*;
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.*;
import org.hibernate.validator.internal.constraintvalidators.bv.time.past.*;
import org.hibernate.validator.internal.constraintvalidators.bv.time.pastorpresent.*;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.function.Supplier;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@Component(BeanValidation.NAME)
public class BeanValidationImpl implements BeanValidation {

    public static final ValidationOptions NO_VALIDATION_OPTIONS = new ValidationOptions();

    @Inject
    protected Messages messages;
    @Inject
    protected Metadata metadata;
    @Inject
    protected TimeSource timeSource;
    @Inject
    protected UserSessionSource userSessionSource;
    @Inject
    protected EntityStates entityStates;

    protected ValidatorFactory defaultValidatorFactory;

    @PostConstruct
    public void init() {
        HibernateValidatorConfiguration configuration = getValidatorFactoryConfiguration(this::getCurrentLocale);
        defaultValidatorFactory = configuration.buildValidatorFactory();
    }

    @Override
    public Validator getValidator() {
        return defaultValidatorFactory.getValidator();
    }

    @Override
    public Validator getValidator(ConstraintMapping constraintMapping) {
        checkNotNullArgument(constraintMapping);

        return getValidator(constraintMapping, NO_VALIDATION_OPTIONS);
    }

    @Override
    public Validator getValidator(@Nullable ConstraintMapping constraintMapping, ValidationOptions options) {
        checkNotNullArgument(options);

        if (constraintMapping == null
                && options.getFailFast() == null
                && options.getLocale() != null) {

            HibernateValidatorConfiguration configuration = getValidatorFactoryConfiguration(options::getLocale);
            ValidatorFactory factory = configuration.buildValidatorFactory();
            return factory.getValidator();
        }

        Locale locale;
        if (options.getLocale() != null) {
            locale = options.getLocale();
        } else {
            locale = getCurrentLocale();
        }

        HibernateValidatorConfiguration configuration = getValidatorFactoryConfiguration(() -> locale);
        if (options.getFailFast() != null) {
            configuration.failFast(options.getFailFast());
        }
        if (constraintMapping != null) {
            configuration.addMapping(constraintMapping);
        }

        ValidatorFactory factory = configuration.buildValidatorFactory();
        return factory.getValidator();
    }

    protected HibernateValidatorConfiguration getValidatorFactoryConfiguration(Supplier<Locale> localeSupplier) {
        HibernateValidatorConfiguration configuration = Validation.byProvider(HibernateValidator.class)
                .configure()
                .clockProvider(new CubaValidationTimeProvider(timeSource))
                .traversableResolver(new CubaValidationTraversableResolver(metadata, entityStates))
                .messageInterpolator(new CubaValidationMessagesInterpolator(messages, localeSupplier));

        ConstraintMapping constraintMapping = configuration.createConstraintMapping();

        //Hibernate validators doesn't support java.sql.Date.
        //Replace standard validators for java.util.Date with support java.sql.Date
        registerPastValidators(constraintMapping.constraintDefinition(Past.class));
        registerPastOrPresentValidators(constraintMapping.constraintDefinition(PastOrPresent.class));
        registerFutureValidators(constraintMapping.constraintDefinition(Future.class));
        registerFutureOrPresentValidators(constraintMapping.constraintDefinition(FutureOrPresent.class));

        configuration.addMapping(constraintMapping);

        return configuration;
    }

    protected Locale getCurrentLocale() {
        Locale locale;
        if (userSessionSource.checkCurrentUserSession()) {
            locale = userSessionSource.getLocale();
        } else {
            locale = messages.getTools().getDefaultLocale();
        }
        return locale;
    }

    protected void registerPastValidators(ConstraintDefinitionContext<Past> context) {
        context.includeExistingValidators(false)
                .validatedBy(PastValidatorForCalendar.class)
                .validatedBy(CubaPastValidatorForDate.class)
                // Java 8 date/time API validators
                .validatedBy(PastValidatorForHijrahDate.class)
                .validatedBy(PastValidatorForInstant.class)
                .validatedBy(PastValidatorForJapaneseDate.class)
                .validatedBy(PastValidatorForLocalDate.class)
                .validatedBy(PastValidatorForLocalDateTime.class)
                .validatedBy(PastValidatorForLocalTime.class)
                .validatedBy(PastValidatorForMinguoDate.class)
                .validatedBy(PastValidatorForMonthDay.class)
                .validatedBy(PastValidatorForOffsetDateTime.class)
                .validatedBy(PastValidatorForOffsetTime.class)
                .validatedBy(PastValidatorForThaiBuddhistDate.class)
                .validatedBy(PastValidatorForYear.class)
                .validatedBy(PastValidatorForYearMonth.class)
                .validatedBy(PastValidatorForZonedDateTime.class);
    }

    protected void registerPastOrPresentValidators(ConstraintDefinitionContext<PastOrPresent> context) {
        context.includeExistingValidators(false)
                .validatedBy(PastOrPresentValidatorForCalendar.class)
                .validatedBy(CubaPastOrPresentValidatorForDate.class)
                // Java 8 date/time API validators
                .validatedBy(PastOrPresentValidatorForHijrahDate.class)
                .validatedBy(PastOrPresentValidatorForInstant.class)
                .validatedBy(PastOrPresentValidatorForJapaneseDate.class)
                .validatedBy(PastOrPresentValidatorForLocalDate.class)
                .validatedBy(PastOrPresentValidatorForLocalDateTime.class)
                .validatedBy(PastOrPresentValidatorForLocalTime.class)
                .validatedBy(PastOrPresentValidatorForMinguoDate.class)
                .validatedBy(PastOrPresentValidatorForMonthDay.class)
                .validatedBy(PastOrPresentValidatorForOffsetDateTime.class)
                .validatedBy(PastOrPresentValidatorForOffsetTime.class)
                .validatedBy(PastOrPresentValidatorForThaiBuddhistDate.class)
                .validatedBy(PastOrPresentValidatorForYear.class)
                .validatedBy(PastOrPresentValidatorForYearMonth.class)
                .validatedBy(PastOrPresentValidatorForZonedDateTime.class);
    }

    protected void registerFutureValidators(ConstraintDefinitionContext<Future> context) {
        context.includeExistingValidators(false)
                .validatedBy(FutureValidatorForCalendar.class)
                .validatedBy(CubaFutureValidatorForDate.class)
                // Java 8 date/time API validators
                .validatedBy(FutureValidatorForHijrahDate.class)
                .validatedBy(FutureValidatorForInstant.class)
                .validatedBy(FutureValidatorForJapaneseDate.class)
                .validatedBy(FutureValidatorForLocalDate.class)
                .validatedBy(FutureValidatorForLocalDateTime.class)
                .validatedBy(FutureValidatorForLocalTime.class)
                .validatedBy(FutureValidatorForMinguoDate.class)
                .validatedBy(FutureValidatorForMonthDay.class)
                .validatedBy(FutureValidatorForOffsetDateTime.class)
                .validatedBy(FutureValidatorForOffsetTime.class)
                .validatedBy(FutureValidatorForThaiBuddhistDate.class)
                .validatedBy(FutureValidatorForYear.class)
                .validatedBy(FutureValidatorForYearMonth.class)
                .validatedBy(FutureValidatorForZonedDateTime.class);
    }

    protected void registerFutureOrPresentValidators(ConstraintDefinitionContext<FutureOrPresent> context) {
        context.includeExistingValidators(false)
                .validatedBy(FutureOrPresentValidatorForCalendar.class)
                .validatedBy(CubaFutureOrPresentValidatorForDate.class)
                // Java 8 date/time API validators
                .validatedBy(FutureOrPresentValidatorForHijrahDate.class)
                .validatedBy(FutureOrPresentValidatorForInstant.class)
                .validatedBy(FutureOrPresentValidatorForJapaneseDate.class)
                .validatedBy(FutureOrPresentValidatorForLocalDate.class)
                .validatedBy(FutureOrPresentValidatorForLocalDateTime.class)
                .validatedBy(FutureOrPresentValidatorForLocalTime.class)
                .validatedBy(FutureOrPresentValidatorForMinguoDate.class)
                .validatedBy(FutureOrPresentValidatorForMonthDay.class)
                .validatedBy(FutureOrPresentValidatorForOffsetDateTime.class)
                .validatedBy(FutureOrPresentValidatorForOffsetTime.class)
                .validatedBy(FutureOrPresentValidatorForThaiBuddhistDate.class)
                .validatedBy(FutureOrPresentValidatorForYear.class)
                .validatedBy(FutureOrPresentValidatorForYearMonth.class)
                .validatedBy(FutureOrPresentValidatorForZonedDateTime.class);
    }

    protected static class CubaPastValidatorForDate extends PastValidatorForDate {
        public CubaPastValidatorForDate() {
        }

        @Override
        protected Instant getInstant(Date value) {
            if (value instanceof java.sql.Date) {
                return Instant.ofEpochMilli(value.getTime());
            } else {
                return super.getInstant(value);
            }
        }
    }

    protected static class CubaPastOrPresentValidatorForDate extends PastOrPresentValidatorForDate {
        public CubaPastOrPresentValidatorForDate() {
        }

        @Override
        protected Instant getInstant(Date value) {
            if (value instanceof java.sql.Date) {
                return Instant.ofEpochMilli(value.getTime());
            } else {
                return super.getInstant(value);
            }
        }
    }

    protected static class CubaFutureValidatorForDate extends FutureValidatorForDate {
        public CubaFutureValidatorForDate() {
        }

        @Override
        protected Instant getInstant(Date value) {
            if (value instanceof java.sql.Date) {
                return Instant.ofEpochMilli(value.getTime());
            } else {
                return super.getInstant(value);
            }
        }
    }

    protected static class CubaFutureOrPresentValidatorForDate extends FutureOrPresentValidatorForDate {
        public CubaFutureOrPresentValidatorForDate() {
        }

        @Override
        protected Instant getInstant(Date value) {
            if (value instanceof java.sql.Date) {
                return Instant.ofEpochMilli(value.getTime());
            } else {
                return super.getInstant(value);
            }
        }
    }
}