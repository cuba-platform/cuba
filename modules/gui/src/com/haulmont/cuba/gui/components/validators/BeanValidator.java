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

package com.haulmont.cuba.gui.components.validators;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.BeanValidation;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.validation.groups.UiComponentChecks;
import com.haulmont.cuba.gui.components.CompositeValidationException;
import com.haulmont.cuba.gui.components.DatasourceComponent;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Validator that applies JSR303 rules for {@link Field} instance using {@link BeanValidation}. <br>
 * Automatically added on {@link DatasourceComponent#setDatasource(Datasource, String)} call if property enclosing class
 * has {@link BeanValidation} constraints.
 *
 * todo use Spring injection
 */
public class BeanValidator implements Field.Validator {
    protected Class beanClass;
    protected String beanProperty;

    protected String validationErrorMessage;
    protected Class[] validationGroups;

    public BeanValidator(Class beanClass, String beanProperty) {
        this.beanClass = beanClass;
        this.beanProperty = beanProperty;
    }

    public BeanValidator(Class beanClass, String beanProperty, Class[] validationGroups) {
        this.beanClass = beanClass;
        this.beanProperty = beanProperty;
        this.validationGroups = validationGroups;
    }

    public Class[] getValidationGroups() {
        return validationGroups;
    }

    /**
     * Set custom validation groups. If not set validator uses {@link Default} and {@link UiComponentChecks} groups.
     *
     * @param validationGroups validation groups
     */
    public void setValidationGroups(Class[] validationGroups) {
        this.validationGroups = validationGroups;
    }

    public String getValidationErrorMessage() {
        return validationErrorMessage;
    }

    /**
     * Set main validation error message. Useful only for custom validation in screen controller.
     *
     * @param validationErrorMessage validation error message
     */
    public void setValidationErrorMessage(String validationErrorMessage) {
        this.validationErrorMessage = validationErrorMessage;
    }

    @Override
    public void validate(Object value) throws ValidationException {
        BeanValidation beanValidation = AppBeans.get(BeanValidation.NAME);
        Validator validator = beanValidation.getValidator();

        Class[] groups = this.validationGroups;
        if (groups == null || groups.length == 0) {
            groups = new Class[]{Default.class, UiComponentChecks.class};
        }

        @SuppressWarnings("unchecked")
        Set<ConstraintViolation> violations = validator.validateValue(beanClass, beanProperty, value, groups);

        if (!violations.isEmpty()) {
            List<CompositeValidationException.ViolationCause> causes = new ArrayList<>();
            for (ConstraintViolation violation : violations) {
                causes.add(new BeanValidationViolationCause(violation));
            }

            String validationMessage = this.validationErrorMessage;
            if (validationMessage == null) {
                validationMessage = getDefaultErrorMessage();
            }

            throw new CompositeValidationException(validationMessage, causes);
        }
    }

    public String getDefaultErrorMessage() {
        Messages messages = AppBeans.get(Messages.NAME);
        Metadata metadata = AppBeans.get(Metadata.NAME);

        MetaClass metaClass = metadata.getClass(beanClass);

        return messages.formatMainMessage("validation.defaultMsg",
                messages.getTools().getPropertyCaption(metaClass, beanProperty));
    }

    public static class BeanValidationViolationCause implements CompositeValidationException.ViolationCause {
        protected ConstraintViolation constraintViolation;

        public BeanValidationViolationCause(ConstraintViolation constraintViolation) {
            this.constraintViolation = constraintViolation;
        }

        @Override
        public String getMessage() {
            return constraintViolation.getMessage();
        }
    }
}