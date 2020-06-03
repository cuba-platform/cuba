/*
 * Copyright (c) 2008-2020 Haulmont.
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
import com.haulmont.cuba.gui.Dialogs;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles a {@link ClassCastException} for the same classes that occurs during the hot deployment.
 */
@Component("cuba_HotDeployClassCastExceptionHandler")
public class HotDeployClassCastExceptionHandler extends AbstractUiExceptionHandler {

    /**
     * Regexp to find the same class names in the {@link ClassCastException} exception message.
     * <p>
     * Regexp explanation:
     * <ul>
     *     <li>{@code ^(\w.+?)\b } - matches the class name at the beginning of the string that consists of words
     *     separated by a dot and ends with a space</li>
     *     <li>{@code (\w.+?)\b$} - matches the class name that consists of words separated by a dot and ends at the end
     *     of the string</li>
     * </ul>
     * <p>
     * Example:
     * <pre>{@code
     *      com.company.sample.web.screens.TestScreen cannot be cast to com.company.sample.web.screens.TestScreen
     * }</pre>
     */
    protected static final Pattern HOT_DEPLOY_CLASS_CAST_REGEXP = Pattern.compile("^(\\w.+?)\\b cannot be cast to (\\w.+?)\\b$");

    @Inject
    protected Messages messages;

    public HotDeployClassCastExceptionHandler() {
        super(ClassCastException.class.getName());
    }

    @Override
    protected boolean canHandle(String className, String message, @Nullable Throwable throwable) {
        Matcher matcher = HOT_DEPLOY_CLASS_CAST_REGEXP.matcher(message);
        return matcher.find()
                && matcher.group(1).equals(matcher.group(2));
    }

    @Override
    protected void doHandle(String className, String message, @Nullable Throwable throwable, UiContext context) {
        Dialogs.ExceptionDialogBuilder builder = context.getDialogs().createExceptionDialog()
                .withCaption(messages.getMainMessage("hotDeployClassCastException.caption"));

        Matcher matcher = HOT_DEPLOY_CLASS_CAST_REGEXP.matcher(message);
        if (matcher.find()) {
            builder.withMessage(messages.formatMainMessage("hotDeployClassCastException.message", matcher.group(1)));
        }

        if (throwable != null) {
            builder.withThrowable(throwable);
        }

        builder.show();
    }
}
