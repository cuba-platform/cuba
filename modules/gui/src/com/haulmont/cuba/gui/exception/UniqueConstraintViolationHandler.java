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

package com.haulmont.cuba.gui.exception;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Frame;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.Ordered;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles database unique constraint violations. Determines the exception type by searching a special marker string
 * in the messages of all exceptions in the chain.
 *
 */
@Component("cuba_UniqueConstraintViolationHandler")
public class UniqueConstraintViolationHandler implements GenericExceptionHandler, Ordered {

    @Inject
    protected Messages messages;

    @Inject
    protected ClientConfig clientConfig;

    @Override
    public boolean handle(Throwable exception, WindowManager windowManager) {
        Throwable t = exception;
        try {
            while (t != null) {
                if (t.toString().contains("org.eclipse.persistence.exceptions.DatabaseException")) {
                    return doHandle(t, windowManager);
                }
                t = t.getCause();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean doHandle(Throwable throwable, WindowManager windowManager) {
        Pattern pattern = clientConfig.getUniqueConstraintViolationPattern();
        String constraintName = "";

        Matcher matcher = pattern.matcher(throwable.toString());
        if (matcher.find()) {
            if (matcher.groupCount() == 1) {
                constraintName = matcher.group(1);
            } else {
                for (int i = 1; i > matcher.groupCount(); i++) {
                    if (StringUtils.isNotBlank(matcher.group(i))) {
                        constraintName = matcher.group(i);
                        break;
                    }
                }
            }

            String msg = "";
            if (StringUtils.isNotBlank(constraintName)) {
                msg = messages.getMainMessage(constraintName.toUpperCase());
            }

            if (msg.equalsIgnoreCase(constraintName)) {
                msg = messages.getMainMessage("uniqueConstraintViolation.message");
                if (StringUtils.isNotBlank(constraintName)) {
                    msg = msg + " (" + constraintName + ")";
                }
            }

            windowManager.showNotification(msg, Frame.NotificationType.ERROR);
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 60;
    }
}