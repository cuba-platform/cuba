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

package com.haulmont.cuba.core.app;

import java.util.Map;

/**
 * Provides sending exception report email functionality.
 */
public interface ExceptionReportService {

    String NAME = "cuba_ExceptionReportService";

    /**
     * Send the exception report email.
     *
     * @param binding      map that contains values to bind in email template
     * @param supportEmail email address to send
     */
    void sendExceptionReport(String supportEmail, Map<String, Object> binding);
}