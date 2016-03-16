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
