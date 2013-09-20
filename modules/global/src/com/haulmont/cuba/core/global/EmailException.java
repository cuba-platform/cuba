/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import java.util.List;

/**
 * Email sending error.<br>
 * Contains failed addresses and corresponding error messages.
 */
@SupportedByClient
public class EmailException extends Exception {

    private static final long serialVersionUID = -9129158384759856382L;
    private final List<String> failedAddresses;
    /**
     * List of error messages which prevented email to be sent.
     */
    private final List<String> messages;

    public EmailException(List<String> failedAddresses, List<String> messages) {
        if (failedAddresses == null || messages == null || failedAddresses.size() != messages.size())
            throw new IllegalArgumentException();

        this.failedAddresses = failedAddresses;
        this.messages = messages;
    }

    public List<String> getFailedAddresses() {
        return failedAddresses;
    }

    public List<String> getMessages() {
        return messages;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < failedAddresses.size(); i++) {
            sb.append(failedAddresses.get(i)).append(" : ").append(messages.get(i)).append("\n");
        }
        return sb.toString();
    }
}
