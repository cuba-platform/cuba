/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.components.validation;

import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.ValidationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * NotNull validator checks that value is not null.
 * <p>
 * In order to provide your own implementation globally, create a subclass and register it in {@code web-spring.xml},
 * for example:
 * <pre>
 *     &lt;bean id="cuba_NotNullValidator" class="com.haulmont.cuba.gui.components.validation.NotNullValidator" scope="prototype"/&gt;
 *     </pre>
 * Use {@link BeanLocator} when creating the validator programmatically.
 *
 * @param <T> value type
 */
@Component(NotNullValidator.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NotNullValidator<T> extends AbstractValidator<T> {

    public static final String NAME = "cuba_NotNullValidator";

    public NotNullValidator() {
    }

    /**
     * Constructor for custom error message.
     *
     * @param message error message
     */
    public NotNullValidator(String message) {
        this.message = message;
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Override
    public void accept(T value) throws ValidationException {
        if (value == null) {
            String message = getMessage();
            if (message == null) {
                message = messages.getMainMessage("validation.constraints.notNull");
            }

            throw new ValidationException(message);
        }
    }
}
