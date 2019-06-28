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

package com.haulmont.cuba.gui.components.validation;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.datatypes.Datatype;
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
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Locale;

import static com.haulmont.cuba.gui.components.validation.ValidatorHelper.getNumberConstraint;

/**
 * Double validator checks that value must be greater than or equal to the specified minimum.
 * <p>
 * For error message it uses Groovy string and it is possible to use '$value' and '$min' keys for formatted output.
 * <p>
 * In order to provide your own implementation globally, create a subclass and register it in {@code web-spring.xml},
 * for example:
 * <pre>
 *   &lt;bean id="cuba_DoubleMinValidator" class="com.haulmont.cuba.gui.components.validation.DoubleMinValidator" scope="prototype"/&gt;
 *   </pre>
 * Use {@link BeanLocator} when creating the validator programmatically.
 *
 * @param <T> Double and String that represents BigDouble value with current locale
 */
@Component(DoubleMinValidator.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DoubleMinValidator<T> extends AbstractValidator<T> {

    public static final String NAME = "cuba_DoubleMinValidator";

    protected Double min;
    protected boolean inclusive = true;

    /**
     * Constructor with default error message.
     *
     * @param min min value
     */
    public DoubleMinValidator(Double min) {
        this.min = min;
    }

    /**
     * Constructor with custom error message. This message can contain '$value', and '$min' keys for formatted output.
     * <p>
     * Example: "Value '$value' should be greater than or equal to '$min'".
     *
     * @param min     min value
     * @param message error message
     */
    public DoubleMinValidator(Double min, String message) {
        this.min = min;
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
     * Sets min value.
     *
     * @param min min value
     */
    public void setMin(Double min) {
        this.min = min;
    }

    /**
     * @return min value
     */
    public Double getMin() {
        return min;
    }

    /**
     * Sets min value and inclusive option.
     *
     * @param min       min value
     * @param inclusive inclusive option
     */
    public void setMin(Double min, boolean inclusive) {
        this.min = min;
        this.inclusive = inclusive;
    }

    /**
     * Set to true if the value must be greater than or equal to the specified minimum. Default value is true.
     *
     * @param inclusive inclusive option
     */
    public void setInclusive(boolean inclusive) {
        this.inclusive = inclusive;
    }

    /**
     * @return true if the value must be greater than or equal to the specified minimum
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
                Datatype datatype = datatypeRegistry.getNN(Double.class);
                Locale locale = userSessionSource.getUserSession().getLocale();
                Double num = (Double) datatype.parse((String) value, locale);
                if (num == null) {
                    fireValidationException(value);
                }
                constraint = getNumberConstraint(num);
            } catch (ParseException e) {
                throw new ValidationException(e.getLocalizedMessage());
            }
        }

        if (constraint == null
                || value instanceof BigDecimal
                || value instanceof Float) {
            throw new IllegalArgumentException("DoubleMinValidator doesn't support following type: '" + value.getClass() + "'");
        }

        if (!constraint.isDoubleMin(min, inclusive)) {
            fireValidationException(value);
        }
    }

    protected String getDefaultMessage() {
        return inclusive ?
                messages.getMainMessage("validation.constraints.decimalMinInclusive")
                : messages.getMainMessage("validation.constraints.decimalMin");
    }

    protected void fireValidationException(T value) {
        String message = getMessage();

        String formattedValue = formatValue(value);
        String formattedMin = formatValue(min);

        String formattedMessage = getTemplateErrorMessage(
                message == null ? getDefaultMessage() : message,
                ParamsMap.of("value", formattedValue, "min", formattedMin));

        throw new ValidationException(formattedMessage);
    }
}
