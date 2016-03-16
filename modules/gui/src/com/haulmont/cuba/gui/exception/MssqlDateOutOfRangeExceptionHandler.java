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
import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Frame;
import org.apache.commons.lang.exception.ExceptionUtils;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

/**
 */
@Component("cuba_MssqlDateOutOfRangeExceptionHandler")
public class MssqlDateOutOfRangeExceptionHandler implements GenericExceptionHandler {

    protected String className;

    protected static final String MESSAGE = "Only dates between January 1, 1753 and December 31, 9999 are accepted";

    @Inject
    protected Messages messages;

    public MssqlDateOutOfRangeExceptionHandler() {
        this.className = SQLException.class.getName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean handle(Throwable exception, WindowManager windowManager) {
        List<Throwable> list = ExceptionUtils.getThrowableList(exception);
        for (Throwable throwable : list) {
            if (className.contains(throwable.getClass().getName()) && isDateOutOfRangeMessage(throwable.getMessage())) {
                doHandle(windowManager);
                return true;
            }
            if (throwable instanceof RemoteException) {
                RemoteException remoteException = (RemoteException) throwable;
                for (RemoteException.Cause cause : remoteException.getCauses()) {
                    if (className.contains(cause.getClassName()) && isDateOutOfRangeMessage(throwable.getMessage())) {
                        doHandle(windowManager);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean isDateOutOfRangeMessage(String message) {
        return message != null && message.contains(MESSAGE);
    }

    protected void doHandle(WindowManager windowManager) {
        String msg = messages.formatMessage(getClass(), "mssqlDateOutOfRangeException.message");
        windowManager.showNotification(msg, Frame.NotificationType.WARNING);
    }
}