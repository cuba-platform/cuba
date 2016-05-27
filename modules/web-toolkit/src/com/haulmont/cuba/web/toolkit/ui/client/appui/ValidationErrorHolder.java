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
 *
 */

package com.haulmont.cuba.web.toolkit.ui.client.appui;

public class ValidationErrorHolder {

    private static final long VALIDATION_ERROR_TIME_GAP_MS = 150;

    private static long lastValidationErrorTs = 0;

    public static void onValidationError() {
        ValidationErrorHolder.lastValidationErrorTs = System.currentTimeMillis();
    }

    public static boolean hasValidationErrors() {
        return System.currentTimeMillis() - lastValidationErrorTs < VALIDATION_ERROR_TIME_GAP_MS;
    }
}