/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.components.validators;

import com.haulmont.cuba.core.global.BeanValidation;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.HasValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Validator that applies JSR 303 rules for {@link HasValue} instance using {@link BeanValidation}. <br>
 * Automatically added on data binding if property enclosing class has {@link BeanValidation} constraints.
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component(BeanPropertyValidator.NAME)
public class BeanPropertyValidator extends AbstractBeanValidator {

    public static final String NAME = "cuba_BeanPropertyValidator";

    public BeanPropertyValidator(Class beanClass, String beanProperty) {
        super(beanClass, beanProperty);
    }

    public BeanPropertyValidator(Class beanClass, String beanProperty, Class[] validationGroups) {
        super(beanClass, beanProperty, validationGroups);
    }

    @Inject
    protected void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Inject
    protected void setBeanValidation(BeanValidation beanValidation) {
        this.beanValidation = beanValidation;
    }
}