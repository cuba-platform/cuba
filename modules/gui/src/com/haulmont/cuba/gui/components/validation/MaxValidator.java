/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.components.validation;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.components.validation.numbers.NumberConstraint;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static com.haulmont.cuba.gui.components.validation.ValidatorHelper.getNumberConstraint;

/**
 * Max validator checks that value must be less than or equal to the specified maximum.
 * <p>
 * For error message it uses Groovy string and it is possible to use '$value' and '$max' keys for formatted output.
 * <p>
 * In order to provide your own implementation globally, create a subclass and register it in {@code web-spring.xml},
 * for example:
 * <pre>
 *    &lt;bean id="cuba_MaxValidator" class="com.haulmont.cuba.gui.components.validation.MaxValidator" scope="prototype"/&gt;
 *    </pre>
 * Use {@link BeanLocator} when creating the validator programmatically.
 *
 * @param <T> BigDecimal, BigInteger, Long, Integer
 */
@Component(MaxValidator.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MaxValidator<T extends Number> extends AbstractValidator<T> {

    public static final String NAME = "cuba_MaxValidator";

    protected long max;

    public MaxValidator(long max) {
        this.max = max;
    }

    /**
     * Constructor for custom error message. This message can contain '$value' and '$max' keys for formatted output.
     * <p>
     * Example: "Value '$value' should be less than or equal to '$max'".
     *
     * @param max     max value
     * @param message error message
     */
    public MaxValidator(long max, String message) {
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
    public void setMax(long max) {
        this.max = max;
    }

    /**
     * @return max value
     */
    public long getMax() {
        return max;
    }

    @Override
    public void accept(T value) throws ValidationException {
        // consider null value is valid
        if (value == null) {
            return;
        }

        NumberConstraint constraint = getNumberConstraint(value);
        if (constraint == null
                || value instanceof Double
                || value instanceof Float) {
            throw new IllegalArgumentException("MaxValidator doesn't support following type: '" + value.getClass() + "'");
        }

        if (!constraint.isMax(max)) {
            String message = getMessage();
            if (message == null) {
                message = messages.getMainMessage("validation.constraints.max");
            }

            String formattedValue = formatValue(value);
            String formattedMax = formatValue(max);
            throw new ValidationException(getTemplateErrorMessage(message, ParamsMap.of("value", formattedValue, "max", formattedMax)));
        }
    }
}
