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

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.SendingMessage;

import javax.mail.MessagingException;

/**
 * Adapter to javax.mail email sending API.
 * <p/>
 * Should not be used from application code, use {@link EmailerAPI}.
 *
 */
public interface EmailSenderAPI {
    String NAME = "cuba_EmailSender";

    /**
     * Sends email with help of {@link org.springframework.mail.javamail.JavaMailSender}.
     * Message body and attachments' content must be loaded from file storage.
     * <p/>
     * Use {@link EmailerAPI} instead if you need email to be delivered reliably and stored to email history.
     *
     * @throws MessagingException if delivery fails
     */
    void sendEmail(SendingMessage sendingMessage) throws MessagingException;
}
