/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.components.validation;

import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * NotBlank validator checks that value contains at least one non-whitespace character.
 * <p>
 * In order to provide your own implementation globally, create a subclass and register it in {@code web-spring.xml},
 * for example:
 * <pre>
 *     &lt;bean id="cuba_NotBlankValidator" class="com.haulmont.cuba.gui.components.validation.NotBlankValidator" scope="prototype"/&gt;
 *     </pre>
 * Use {@link BeanLocator} when creating the validator programmatically.
 */
@Component(NotBlankValidator.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NotBlankValidator extends AbstractValidator<String> {

    public static final String NAME = "cuba_NotBlankValidator";

    public NotBlankValidator() {
    }

    /**
     * Constructor for custom error message.
     *
     * @param message error message
     */
    public NotBlankValidator(String message) {
        this.message = message;
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Override
    public void accept(String value) throws ValidationException {
        if (StringUtils.isBlank(value)) {
            String message = getMessage();
            if (message == null) {
                message = messages.getMainMessage("validation.constraints.notBlank");
            }

            throw new ValidationException(message);
        }
    }
}
