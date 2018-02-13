/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.gui.exception;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.validation.MethodParametersValidationException;
import com.haulmont.cuba.core.global.validation.MethodResultValidationException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Frame.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@Component("cuba_ServiceCallValidationExceptionHandler")
public class ServiceCallValidationExceptionHandler extends AbstractGenericExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ServiceCallValidationExceptionHandler.class);

    @Inject
    protected Messages messages;

    public ServiceCallValidationExceptionHandler() {
        super(MethodParametersValidationException.class.getName(),
                MethodResultValidationException.class.getName());
    }

    @Override
    protected void doHandle(String className, String message, @Nullable Throwable throwable, WindowManager wm) {
        if (throwable == null) {
            return;
        }

        Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) throwable).getConstraintViolations();

        log.error("Service has thrown validation exception. Violations: {}", violations, throwable);

        String msg = messages.formatMessage(ServiceCallValidationExceptionHandler.class, "serviceCallValidationViolation.message");
        wm.showNotification(msg, NotificationType.ERROR);
    }
}