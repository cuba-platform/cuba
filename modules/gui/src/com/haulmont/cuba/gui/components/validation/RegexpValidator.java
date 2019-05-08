/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.components.validation;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.ValidationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.regex.Pattern;

/**
 * Regexp validator checks that String value is matched with specified regular expression.
 * <p>
 * The regular expression follows the Java regular expression conventions.
 * <p>
 * For error message it uses Groovy string and it is possible to use '$value' key for formatted output.
 * <p>
 * In order to provide your own implementation globally, create a subclass and register it in {@code web-spring.xml},
 * for example:
 * <pre>
 *     &lt;bean id="cuba_RegexpValidator" class="com.haulmont.cuba.gui.components.validation.RegexpValidator" scope="prototype"/&gt;
 *     </pre>
 * Use {@link BeanLocator} when creating the validator programmatically.
 *
 * @see java.util.regex.Pattern
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component(RegexpValidator.NAME)
public class RegexpValidator extends AbstractValidator<String> {

    public static final String NAME = "cuba_RegexpValidator";

    protected Pattern pattern;

    public RegexpValidator(String regexp) {
        Preconditions.checkNotNullArgument(regexp);

        this.pattern = Pattern.compile(regexp);
    }

    /**
     * Constructor for regexp value and custom error message. This message can contain '$value' key for formatted output.
     * Example: "Invalid value '$value'".
     *
     * @param regexp  regular expression
     * @param message error message
     */
    public RegexpValidator(String regexp, String message) {
        Preconditions.checkNotNullArgument(regexp);

        this.message = message;
        this.pattern = Pattern.compile(regexp);
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Override
    public void accept(String value) throws ValidationException {
        if (value == null) {
            return;
        }

        if (!pattern.matcher((value)).matches()) {
            String message = getMessage();
            if (message == null) {
                message = messages.getMainMessage("validation.constraints.regexp");
            }

            throw new ValidationException(getTemplateErrorMessage(message, ParamsMap.of("value", value)));
        }
    }
}
