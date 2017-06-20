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

package com.haulmont.cuba.core.controllers;

import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.sys.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Provides health check URL for the middleware block.
 */
@Controller
public class HealthCheckController {

    private Logger log = LoggerFactory.getLogger(HealthCheckController.class);

    @Inject
    private GlobalConfig config;

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public void healthCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.trace("Health check request {} from {}", request.getRequestURI(), request.getRemoteAddr());
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        if (AppContext.isReady()) {
            response.getOutputStream().print(config.getHealthCheckResponse());
        } else {
            response.getOutputStream().print("not ready");
        }
    }
}
