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

package com.haulmont.cuba.desktop.sys.validation;

import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import org.apache.commons.lang.BooleanUtils;

/**
 */
public final class ValidationAlertHolder {

    protected static Boolean validationAlert = null;

    private ValidationAlertHolder() {
    }

    public static void validationExpected() {
        validationAlert = false;
    }

    public static void validationFailed() {
        validationAlert = true;
    }

    public static void clear() {
        validationAlert = null;
    }

    public static boolean isListen() {
        return BooleanUtils.isFalse(validationAlert);
    }

    public static boolean isFailed() {
        return BooleanUtils.isTrue(validationAlert);
    }

    public static void runIfValid(Runnable r) {
        validationExpected();

        try {
            DesktopComponentsHelper.flushCurrentInputField();

            if (!isFailed()) {
                clear();

                r.run();
            }
        } finally {
            clear();
        }
    }
}