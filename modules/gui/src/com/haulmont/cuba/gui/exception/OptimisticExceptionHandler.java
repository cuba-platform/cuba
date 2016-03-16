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

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Frame;
import org.springframework.core.Ordered;

import org.springframework.stereotype.Component;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles a JPA optimistic lock exception.
 *
 */
@Component("cuba_OptimisticExceptionHandler")
public class OptimisticExceptionHandler extends AbstractGenericExceptionHandler implements Ordered {

    @Inject
    protected Messages messages;

    public OptimisticExceptionHandler() {
        super("javax.persistence.OptimisticLockException");
    }

    @Override
    protected void doHandle(String className, String message, @Nullable Throwable throwable, WindowManager windowManager) {
        Pattern pattern = Pattern.compile("Class> (.+)");
        Matcher matcher = pattern.matcher(message);
        String entityClassName = "";
        if (matcher.find()) {
            entityClassName = matcher.group(1);
        }

        String msg;

        if (entityClassName.contains(".")) {
            String entityName = entityClassName.substring(entityClassName.lastIndexOf(".") + 1);
            String packageName = entityClassName.substring(0, entityClassName.lastIndexOf("."));
            String localizedEntityName = messages.getMessage(packageName, entityName);

            msg = messages.formatMessage(messages.getMainMessagePack(),
                    "optimisticException.message", "\"" + localizedEntityName + "\"");
        } else {
            msg = messages.getMessage(messages.getMainMessagePack(), "optimisticExceptionUnknownObject.message");
        }
        windowManager.showNotification(msg, Frame.NotificationType.ERROR);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 40;
    }
}