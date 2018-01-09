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

package com.haulmont.restapi.controllers;

import com.haulmont.cuba.core.global.RowLevelSecurityException;
import com.haulmont.cuba.core.global.validation.CustomValidationException;
import com.haulmont.cuba.core.global.validation.MethodParametersValidationException;
import com.haulmont.cuba.core.global.validation.MethodResultValidationException;
import com.haulmont.restapi.exception.ConstraintViolationInfo;
import com.haulmont.restapi.exception.ErrorInfo;
import com.haulmont.restapi.exception.RestAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.*;

@ControllerAdvice("com.haulmont.restapi.controllers")
public class RestControllerExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(RestControllerExceptionHandler.class);

    protected static final Class[] serializableInvalidValueTypes = new Class[]{
            String.class, Date.class, Number.class, Enum.class, UUID.class
    };

    @ExceptionHandler(RestAPIException.class)
    @ResponseBody
    public ResponseEntity<ErrorInfo> handleRestAPIException(RestAPIException e) {
        if (e.getCause() == null) {
            log.info("RestAPIException: {}, {}", e.getMessage(), e.getDetails());
        } else {
            log.error("RestAPIException: {}, {}", e.getMessage(), e.getDetails(), e.getCause());
        }
        ErrorInfo errorInfo = new ErrorInfo(e.getMessage(), e.getDetails());
        return new ResponseEntity<>(errorInfo, e.getHttpStatus());
    }

    @ExceptionHandler(MethodResultValidationException.class)
    @ResponseBody
    public ResponseEntity<ErrorInfo> handleMethodResultValidationException(MethodResultValidationException e) {
        log.error("MethodResultValidationException in service", e);
        ErrorInfo errorInfo = new ErrorInfo("Server error", "");
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodParametersValidationException.class)
    @ResponseBody
    public ResponseEntity<List<ConstraintViolationInfo>> handleMethodParametersViolation(MethodParametersValidationException e) {
        log.debug("MethodParametersValidationException: {}, violations:\n{}", e.getMessage(), e.getConstraintViolations());

        List<ConstraintViolationInfo> violationInfos = getConstraintViolationInfos(e.getConstraintViolations());

        return new ResponseEntity<>(violationInfos, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<List<ConstraintViolationInfo>> handleConstraintViolation(ConstraintViolationException e) {
        log.debug("ConstraintViolationException: {}, violations:\n{}", e.getMessage(), e.getConstraintViolations());

        List<ConstraintViolationInfo> violationInfos = getConstraintViolationInfos(e.getConstraintViolations());

        return new ResponseEntity<>(violationInfos, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomValidationException.class)
    @ResponseBody
    public ResponseEntity<List<ConstraintViolationInfo>> handleCustomValidationException(CustomValidationException e) {
        log.debug("CustomValidationException: {}", e.getMessage());

        ConstraintViolationInfo errorInfo = new ConstraintViolationInfo();
        errorInfo.setPath("");
        errorInfo.setInvalidValue(null);
        errorInfo.setMessage(e.getLocalizedMessage());
        errorInfo.setMessageTemplate("{com.haulmont.cuba.core.global.validation.CustomValidationException}");

        return new ResponseEntity<>(Collections.singletonList(errorInfo), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<ErrorInfo> handleValidationException(ValidationException e) {
        log.error("ValidationException in service", e);
        ErrorInfo errorInfo = new ErrorInfo("Server error", "");
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RowLevelSecurityException.class)
    @ResponseBody
    public ResponseEntity<ErrorInfo> handleRowLevelSecurityException(RowLevelSecurityException e) {
        log.error("RowLevelSecurityException in service", e);
        ErrorInfo errorInfo = new ErrorInfo("Forbidden", e.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorInfo> handleException(Exception e) {
        log.error("Exception in REST controller", e);
        ErrorInfo errorInfo = new ErrorInfo("Server error", "");
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected List<ConstraintViolationInfo> getConstraintViolationInfos(Set<ConstraintViolation<?>> violations) {
        List<ConstraintViolationInfo> violationInfos = new ArrayList<>();

        for (ConstraintViolation<?> violation : violations) {
            ConstraintViolationInfo info = new ConstraintViolationInfo();

            info.setMessage(violation.getMessage());
            info.setMessageTemplate(violation.getMessageTemplate());

            Object invalidValue = violation.getInvalidValue();
            if (invalidValue != null) {
                Class<?> invalidValueClass = invalidValue.getClass();

                boolean serializable = false;
                for (Class serializableType : serializableInvalidValueTypes) {
                    //noinspection unchecked
                    if (serializableType.isAssignableFrom(invalidValueClass)) {
                        serializable = true;
                        break;
                    }
                }
                if (serializable) {
                    info.setInvalidValue(invalidValue);
                } else {
                    info.setInvalidValue(null);
                }
            }

            if (violation.getPropertyPath() != null) {
                info.setPath(violation.getPropertyPath().toString());
            }

            violationInfos.add(info);
        }
        return violationInfos;
    }
}