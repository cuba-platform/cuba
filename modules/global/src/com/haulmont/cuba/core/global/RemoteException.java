/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Exception that returns to clients from the middleware. Contains the information about the whole server-side
 * exception chain in the <code>Cause</code> objects list. Actual exception instances are included only if they
 * explicitly declared as available for the clients (annotated with {@link SupportedByClient}).
 *
 * @author krivopustov
 * @version $Id$
 */
public class RemoteException extends RuntimeException {

    private static final long serialVersionUID = -681950463552884310L;

    public static class Cause implements Serializable {

        private static final long serialVersionUID = 7677717005347643512L;

        private String className;
        private String message;
        private Throwable throwable;

        public Cause(Throwable throwable) {
            className = throwable.getClass().getName();
            message = throwable.getMessage();
            if (throwable.getClass().getName().startsWith("java.")
                    || throwable.getClass().getAnnotation(SupportedByClient.class) != null) {
                this.throwable = throwable;
            }
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

    private List<Cause> causes = new ArrayList<>();

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
     * Search for {@link Cause} by its exception class name. Subclasses are not taken into account.
     *
     * @param throwableClassName exception class name
     * @return Cause instance or null if it is not found
     */
    @Nullable
    public Cause getCause(String throwableClassName) {
        for (Cause cause : causes) {
            if (cause.getClassName().equals(throwableClassName)) {
                return cause;
            }
        }
        return null;
    }

    /**
     * Search for {@link Cause} by its exception class. Subclasses are not taken into account. The given class is
     * converted into its string name, so it doesn't matter what Cause contains - class or its name only.
     *
     * @param throwableClass exception class
     * @return true if such cause is found
     */
    public boolean contains(Class<?> throwableClass) {
        Objects.requireNonNull(throwableClass, "throwableClass is null");
        return getCause(throwableClass.getName()) != null;
    }

    /**
     * Search for {@link Cause} by its exception class. Subclasses are not taken into account.
     *
     * @param throwableClassName exception class name
     * @return true if such cause is found
     */
    public boolean contains(String throwableClassName) {
        return getCause(throwableClassName) != null;
    }

    /**
     * @return First exception in the causes list if it is checked or it is supported by client, null otherwise
     */
    @Nullable
    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
    public Exception getFirstCauseException() {
        if (!causes.isEmpty()) {
            Throwable t = causes.get(0).getThrowable();
            if (t != null) {
                if (!(t instanceof RuntimeException) && !(t instanceof Error)) {
                    return (Exception) t;
                }
                if (!(t instanceof Error) && t.getClass().getAnnotation(SupportedByClient.class) != null)
                    return (Exception) t;
            }
        }
        return null;
    }

    @Override
    @Nullable
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
