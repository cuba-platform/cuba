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

package com.haulmont.cuba.core.global.validation;

import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.ValidationException;
import javax.validation.metadata.ConstraintDescriptor;
import java.io.Serializable;

public class ServiceMethodConstraintViolation implements ConstraintViolation<Object>, Serializable {
    private Class rootBeanClass;
    private String message;
    private String messageTemplate;
    private Object invalidValue;
    private ConstraintDescriptor<?> constraintDescriptor;
    private final Object[] executableParameters;
    private final Object executableReturnValue;

    public ServiceMethodConstraintViolation(Class serviceInterface, ConstraintViolation violation) {
        this.message = violation.getMessage();
        this.messageTemplate = violation.getMessageTemplate();
        this.invalidValue = violation.getInvalidValue();
        this.constraintDescriptor = violation.getConstraintDescriptor();
        this.executableParameters = violation.getExecutableParameters();
        this.executableReturnValue = violation.getExecutableReturnValue();
        this.rootBeanClass = serviceInterface;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMessageTemplate() {
        return messageTemplate;
    }

    @Override
    public Object getRootBean() {
        return null;
    }

    @Override
    public Class getRootBeanClass() {
        return rootBeanClass;
    }

    @Override
    public Object getLeafBean() {
        return null;
    }

    @Override
    public Object[] getExecutableParameters() {
        return executableParameters;
    }

    @Override
    public Object getExecutableReturnValue() {
        return executableReturnValue;
    }

    @Override
    public Path getPropertyPath() {
        return PathImpl.createRootPath();
    }

    @Override
    public Object getInvalidValue() {
        return invalidValue;
    }

    @Override
    public ConstraintDescriptor<?> getConstraintDescriptor() {
        return constraintDescriptor;
    }

    @Override
    public Object unwrap(Class type) {
        throw new ValidationException("Unwrap is unsupported");
    }

    @Override
    public String toString() {
        return "ServiceMethodConstraintViolation(message= " + message
                + ", rootBeanClass=" + rootBeanClass.getName()
                + ", invalidValue=" + invalidValue
                + ", desc=" + constraintDescriptor + ")";
    }
}