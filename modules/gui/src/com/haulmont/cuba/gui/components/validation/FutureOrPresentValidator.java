/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.components.validation;

import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.DateTimeTransformations;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.components.validation.time.TimeValidator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.*;
import java.util.Date;

/**
 * Validates that date or time in the future or present.
 * <p>
 * Note, types that support TimeZones can be found in {@link DateTimeTransformations#isDateTypeSupportsTimeZones(Class)}.
 * <p>
 * In order to provide your own implementation globally, create a subclass and register it in {@code web-spring.xml},
 * for example:
 * <pre>
 *    &lt;bean id="cuba_FutureOrPresentValidator" class="com.haulmont.cuba.gui.components.validation.FutureOrPresentValidator" scope="prototype"/&gt;
 *    </pre>
 * Use {@link BeanLocator} when creating the validator programmatically.
 *
 * @param <T> {@link Date}, {@link LocalDate}, {@link LocalDateTime}, {@link LocalTime}, {@link OffsetDateTime},
 *            {@link OffsetTime}
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component(FutureOrPresentValidator.NAME)
public class FutureOrPresentValidator<T> extends AbstractValidator<T> {

    public static final String NAME = "cuba_FutureOrPresentValidator";

    protected boolean checkSeconds = false;

    public FutureOrPresentValidator() {
    }

    /**
     * Constructor for custom error message.
     *
     * @param message error message
     */
    public FutureOrPresentValidator(String message) {
        this.message = message;
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.messages = messages;
    }

    /**
     * Set true if validator should also check seconds and nanos (if supported) in value. Default value is false.
     *
     * @param checkSeconds check seconds
     */
    public void setCheckSeconds(boolean checkSeconds) {
        this.checkSeconds = checkSeconds;
    }

    /**
     * @return true if seconds and nanos are checked
     */
    public boolean isCheckSeconds() {
        return checkSeconds;
    }

    @Override
    public void accept(T value) throws ValidationException {
        // consider null value is valid
        if (value == null) {
            return;
        }

        TimeValidator timeConstraint = ValidatorHelper.getTimeConstraint(value);
        if (timeConstraint == null) {
            throw new IllegalArgumentException("FutureOrPresentValidator doesn't support following type: '" + value.getClass() + "'");
        }

        timeConstraint.setCheckSeconds(checkSeconds);
        if (!timeConstraint.isFutureOrPresent()) {
            String message = getMessage();
            if (message == null) {
                message = messages.getMainMessage("validation.constraints.futureOrPresent");
            }

            throw new ValidationException(message);
        }
    }
}
