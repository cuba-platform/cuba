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

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.components.Frame;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.remoting.RemoteAccessException;

import java.util.List;

/**
 */
public class ConnectExceptionHandler implements ExceptionHandler {

    @Override
    public boolean handle(Thread thread, Throwable exception) {
        @SuppressWarnings("unchecked")
        List<Throwable> list = ExceptionUtils.getThrowableList(exception);
        for (Throwable throwable : list) {
            if (throwable instanceof RemoteAccessException) {
                Messages messages = AppBeans.get(Messages.NAME);
                String msg = messages.getMessage(getClass(), "connectException.message");
                if (throwable.getCause() == null) {
                    App.getInstance().getMainFrame().showNotification(msg, Frame.NotificationType.ERROR);
                } else {
                    String description = messages.formatMessage(getClass(), "connectException.description",
                            throwable.getCause().toString());
                    App.getInstance().getMainFrame().showNotification(msg, description, Frame.NotificationType.ERROR);
                }
                return true;
            }
        }
        return false;
    }
}
