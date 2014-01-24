/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.bali.util;

import javax.annotation.Nullable;

/**
 * Simple static methods to be called at the start of your own methods to verify correct arguments and state.
 *
 * @author artamonov
 * @version $Id$
 */
public final class Preconditions {

    private Preconditions() {
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @throws IllegalArgumentException if {@code reference} is null
     */
    public static void checkNotNullArgument(Object reference) {
        if (reference == null) {
            throw new IllegalArgumentException("Null reference passed as parameter");
        }
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference    an object reference
     * @param errorMessage the exception message to use if the check fails; will
     *                     be converted to a string using {@link String#valueOf(Object)}
     * @throws IllegalArgumentException if {@code reference} is null
     */
    public static void checkNotNullArgument(Object reference, @Nullable String errorMessage) {
        if (reference == null) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference            an object reference
     * @param errorMessageTemplate a template for the exception message should the check fail.
     *                             The message is formed by replacing each {@code %s}
     *                             placeholder in the template with an argument. These are matched by
     *                             position - the first {@code %s} gets {@code errorMessageArgs[0]}, etc.
     *                             Unmatched arguments will be appended to the formatted message in square
     *                             braces. Unmatched placeholders will be left as-is.
     * @param errorMessageArgs     the arguments to be substituted into the message template.
     *                             Arguments are converted to strings using
     *                             {@link String#valueOf(Object)}.
     * @throws IllegalArgumentException if {@code reference} is null
     */
    public static void checkNotNullArgument(Object reference, @Nullable String errorMessageTemplate,
                                            @Nullable Object... errorMessageArgs) {
        if (reference == null) {
            throw new IllegalArgumentException(formatExceptionArgs(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static String formatExceptionArgs(String template, @Nullable Object... args) {
        template = String.valueOf(template); // null -> "null"
        if (args == null) {
            return template;
        }

        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template.substring(templateStart, placeholderStart));
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template.substring(templateStart));

        // if we run out of placeholders, append the extra args in square braces
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }
}