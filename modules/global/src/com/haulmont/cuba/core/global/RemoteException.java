/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.ClassesInfo;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Exception that returns to clients from the middleware. Contains the information about the whole server-side
 * exception chain in the <code>Cause</code> objects list. Actual exception instances are included only if they
 * explicitly made available for the clients (registered in {@link ClassesInfo}).
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class RemoteException extends RuntimeException {

    public static class Cause implements Serializable {

        private String className;
        private String message;
        private Throwable throwable;

        public Cause(Throwable throwable) {
            className = throwable.getClass().getName();
            message = throwable.getMessage();
            if (ClassesInfo.isClientSupported(throwable.getClass()))
                this.throwable = throwable;
        }

        public String getClassName() {
            return className;
        }

        public String getMessage() {
            return message;
        }

        @Nullable
        public Throwable getThrowable() {
            return throwable;
        }

        @Override
        public String toString() {
            return className + ": " + message;
        }
    }

    private List<Cause> causes = new ArrayList<Cause>();

    @SuppressWarnings("unchecked")
    public RemoteException(Throwable throwable) {
        List<Throwable> list = ExceptionUtils.getThrowableList(throwable);
        for (Throwable t : list) {
            causes.add(new Cause(t));
        }
    }

    public List<Cause> getCauses() {
        return Collections.unmodifiableList(causes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RemoteException:");
        for (Cause cause : causes) {
            sb.append("\n---\n").append(cause.getClassName()).append(": ").append(cause.getMessage());
        }
        return sb.toString();
    }
}
