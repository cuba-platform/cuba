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

package com.haulmont.cuba.client.testsupport;

import com.haulmont.cuba.core.global.BeanValidation;
import org.hibernate.validator.cfg.ConstraintMapping;

import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.*;
import java.util.Collections;
import java.util.Set;

public class TestBeanValidation implements BeanValidation {
    @Override
    public Validator getValidator() {
        return new ValidatorStub();
    }

    @Override
    public Validator getValidator(ConstraintMapping constraintMapping) {
        return new ValidatorStub();
    }

    @Override
    public Validator getValidator(@Nullable ConstraintMapping constraintMapping, ValidationOptions options) {
        return new ValidatorStub();
    }

    public static class ValidatorStub implements Validator {
        @Override
        public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>[] groups) {
            return Collections.emptySet();
        }

        @Override
        public <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>[] groups) {
            return Collections.emptySet();
        }

        @Override
        public <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>[] groups) {
            return Collections.emptySet();
        }

        @Override
        public BeanDescriptor getConstraintsForClass(Class<?> clazz) {
            return new BeanDescriptorStub();
        }

        @Override
        public <T> T unwrap(Class<T> type) {
            return null;
        }

        @Override
        public ExecutableValidator forExecutables() {
            return null;
        }
    }

    public static class BeanDescriptorStub implements BeanDescriptor {

        @Override
        public boolean isBeanConstrained() {
            return false;
        }

        @Override
        public PropertyDescriptor getConstraintsForProperty(String propertyName) {
            return null;
        }

        @Override
        public Set<PropertyDescriptor> getConstrainedProperties() {
            return null;
        }

        @Override
        public MethodDescriptor getConstraintsForMethod(String methodName, Class<?>[] parameterTypes) {
            return null;
        }

        @Override
        public Set<MethodDescriptor> getConstrainedMethods(MethodType methodType, MethodType... methodTypes) {
            return null;
        }

        @Override
        public ConstructorDescriptor getConstraintsForConstructor(Class<?>[] parameterTypes) {
            return null;
        }

        @Override
        public Set<ConstructorDescriptor> getConstrainedConstructors() {
            return null;
        }

        @Override
        public boolean hasConstraints() {
            return false;
        }

        @Override
        public Class<?> getElementClass() {
            return null;
        }

        @Override
        public Set<ConstraintDescriptor<?>> getConstraintDescriptors() {
            return null;
        }

        @Override
        public ConstraintFinder findConstraints() {
            return null;
        }
    }
}