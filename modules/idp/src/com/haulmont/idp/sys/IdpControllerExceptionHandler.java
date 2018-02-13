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

package com.haulmont.idp.sys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Logs all exceptions of IDP controllers and returns 500 (Internal Server Error) error page to clients.
 */
@ControllerAdvice("com.haulmont.idp.controllers")
@Component("cuba_IdpControllerExceptionHandler")
public class IdpControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(IdpControllerExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleException(Exception ex) {
        log.error("Exception in MVC controller", ex);

        return new ResponseEntity<>("IDP server error, please see server log for details",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}