/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

/**
 * Email sending error.<br>
 * Contains failed addresses and corresponding error messages.
 */
public class EmailException extends Exception
{
    private static final long serialVersionUID = -2559499596752714382L;
    
    private String[] failedAddresses;
    private String[] messages;

    public EmailException(String[] failedAddresses, String[] messages) {
        if (failedAddresses == null || messages == null || failedAddresses.length != messages.length)
            throw new IllegalArgumentException();
        
        this.failedAddresses = failedAddresses;
        this.messages = messages;
    }

    public String[] getFailedAddresses() {
        return failedAddresses;
    }

    public String[] getMessages() {
        return messages;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < failedAddresses.length; i++) {
            sb.append(failedAddresses[i]).append(" : ").append(messages[i]).append("\n");
        }
        return sb.toString();
    }
}
