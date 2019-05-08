/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.components.validation;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.components.validation.numbers.NumberConstraint;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Locale;

import static com.haulmont.cuba.gui.components.validation.ValidatorHelper.getNumberConstraint;

/**
 * DecimalMax validator checks that value must be less than or equal to the specified maximum.
 * <p>
 * For error message it uses Groovy string and it is possible to use '$value' and '$max' keys for formatted output.
 * <p>
 * In order to provide your own implementation globally, create a subclass and register it in {@code web-spring.xml},
 * for example:
 * <pre>
 *   &lt;bean id="cuba_DecimalMaxValidator" class="com.haulmont.cuba.gui.components.validation.DecimalMaxValidator" scope="prototype"/&gt;
 *   </pre>
 * Use {@link BeanLocator} when creating the validator programmatically.
 *
 * @param <T> BigDecimal, BigInteger, Long, Integer and String that represents BigDecimal value with current locale
 */
@Component(DecimalMaxValidator.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DecimalMaxValidator<T> extends AbstractValidator<T> {

    public static final String NAME = "cuba_DecimalMaxValidator";

    protected BigDecimal max;
    protected boolean inclusive = true;

    /**
     * Constructor with default error message.
     *
     * @param max max value
     */
    public DecimalMaxValidator(BigDecimal max) {
        this.max = max;
    }

    /**
     * Constructor with custom error message. This message can contain '$value', and '$max' keys for formatted output.
     * <p>
     * Example: "Value '$value' should be less than or equal to '$max'".
     *
     * @param max     max value
     * @param message error message
     */
    public DecimalMaxValidator(BigDecimal max, String message) {
        this.max = max;
        this.message = message;
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Inject
    protected void setDatatypeRegistry(DatatypeRegistry datatypeRegistry) {
        this.datatypeRegistry = datatypeRegistry;
    }

    @Inject
    protected void setUserSessionSource(UserSessionSource userSessionSource) {
        this.userSessionSource = userSessionSource;
    }

    /**
     * Sets max value.
     *
     * @param max max value
     */
    public void setMax(BigDecimal max) {
        this.max = max;
    }

    /**
     * @return max value
     */
    public BigDecimal getMax() {
        return max;
    }

    /**
     * Sets max value and inclusive option.
     *
     * @param max       max value
     * @param inclusive inclusive option
     */
    public void setMax(BigDecimal max, boolean inclusive) {
        this.max = max;
        this.inclusive = inclusive;
    }

    /**
     * Set to true if the value must be less than or equal to the specified maximum. Default value is true.
     *
     * @param inclusive inclusive option
     */
    public void setInclusive(boolean inclusive) {
        this.inclusive = inclusive;
    }

    /**
     * @return true if the value must be less than or equal to the specified maximum
     */
    public boolean isInclusive() {
        return inclusive;
    }

    @Override
    public void accept(T value) throws ValidationException {
        // consider null value is valid
        if (value == null) {
            return;
        }

        NumberConstraint constraint = null;

        if (value instanceof Number) {
            constraint = getNumberConstraint((Number) value);
        } else if (value instanceof String) {
            try {
                Datatype datatype = Datatypes.getNN(BigDecimal.class);
                Locale locale = userSessionSource.getUserSession().getLocale();
                BigDecimal bigDecimal = (BigDecimal) datatype.parse((String) value, locale);
                if (bigDecimal == null) {
                    fireValidationException(value);
                }
                constraint = getNumberConstraint(bigDecimal);
            } catch (ParseException e) {
                throw new ValidationException(e.getLocalizedMessage());
            }
        }

        if (constraint == null
                || value instanceof Double
                || value instanceof Float) {
            throw new IllegalArgumentException("DecimalMaxValidator doesn't support following type: '" + value.getClass() + "'");
        }

        if (!constraint.isDecimalMax(max, inclusive)) {
            fireValidationException(value);
        }
    }

    protected String getDefaultMessage() {
        return inclusive ?
                messages.getMainMessage("validation.constraints.decimalMaxInclusive")
                : messages.getMainMessage("validation.constraints.decimalMax");
    }

    protected void fireValidationException(T value) {
        String message = getMessage();

        String formattedValue = formatValue(value);
        String formattedMax = formatValue(max);

        String formattedMessage = getTemplateErrorMessage(
                message == null ? getDefaultMessage() : message,
                ParamsMap.of("value", formattedValue, "max", formattedMax));

        throw new ValidationException(formattedMessage);
    }
}
