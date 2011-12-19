/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Exception that returns to clients from the middleware. Contains the information about the whole server-side
 * exception chain in the <code>Cause</code> objects list. Actual exception instances are included only if they
 * explicitly declared as available for the clients (annotated with {@link SupportedByClient}).
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
            if (throwable.getClass().getAnnotation(SupportedByClient.class) != null)
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

    /**
     * @return  First exception in the causes list if it is checked, null otherwise
     */
    public Exception getFirstCheckedException() {
        if (!causes.isEmpty()) {
            Throwable t = causes.get(0).getThrowable();
            if (t != null && !(t instanceof RuntimeException) && !(t instanceof Error)) {
                return (Exception) t;
            }
        }
        return null;
    }

    @Override
    public String getMessage() {
        if (!causes.isEmpty())
            return causes.get(causes.size() - 1).getMessage();
        else
            return null;
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
