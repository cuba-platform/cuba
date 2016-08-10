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

import com.haulmont.restapi.exception.ErrorInfo;
import com.haulmont.restapi.exception.RestAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 */
@ControllerAdvice("com.haulmont.restapi.controllers")
public class ControllerExceptionHandler {

    protected Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(RestAPIException.class)
    @ResponseBody
    public ResponseEntity<ErrorInfo> handleRestAPIException(RestAPIException e) {
        log.info("RestAPIException: {}, {}", e.getMessage(), e.getDetails());
        ErrorInfo errorInfo = new ErrorInfo(e.getMessage(), e.getDetails());
        return new ResponseEntity<>(errorInfo, e.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorInfo> handleException(Exception e) {
        ErrorInfo errorInfo = new ErrorInfo("Server error", "");
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
