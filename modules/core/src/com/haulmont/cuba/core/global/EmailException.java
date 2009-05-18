/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.05.2009 11:59:03
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

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
