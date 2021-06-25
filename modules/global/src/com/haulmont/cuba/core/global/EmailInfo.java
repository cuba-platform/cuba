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

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Contains email details: list of recipients, from address, caption, body and attachments.
 * See {@link EmailInfoBuilder} for more information.
 *
 * @see com.haulmont.cuba.core.app.EmailService
 * @see EmailInfoBuilder
 */
public class EmailInfo implements Serializable {

    private static final long serialVersionUID = -382773435130109083L;

    public static final String HTML_CONTENT_TYPE = "text/html; charset=UTF-8";
    public static final String TEXT_CONTENT_TYPE = "text/plain; charset=UTF-8";

    /**
     * Recipient email addresses separated with "," or ";" symbol.
     * <p>
     * Flag {@code sendInOneMessage} is for backward compatibility with previous CUBA versions.
     * If {@code sendInOneMessage = true} then one message will be sent for all recipients and it will include CC and BCC.
     * Otherwise CC and BCC are ignored and multiple messages by the number of recipients in addresses will be sent.
     */
    private String addresses;
    private String cc;
    private String bcc;
    private boolean sendInOneMessage = false;
    private String caption;
    private String from;
    private String templatePath;
    private Map<String, Serializable> templateParameters;
    private String body;
    private String bodyContentType;
    private EmailAttachment[] attachments;
    private List<EmailHeader> headers;

    /**
     * Deprecated. Use {@link EmailInfoBuilder} instead.
     *
     * Constructor. Example usage:
     * <pre>{@code
     *     EmailInfo emailInfo = new EmailInfo(
     *          "john.doe@company.com,jane.roe@company.com",
     *          "Company news",
     *          "do-not-reply@company.com",
     *          "com/company/sample/email_templates/news.txt",
     *          Collections.singletonMap("some_var", some_value)
     *     );
     * }</pre>
     * <p>
     * If you want to set content body type you can use
     * {@link #EmailInfo(String, String, String, String, String, Map, EmailAttachment...)} instead or use {@code
     * setBodyContentType()} method.
     *
     * @param addresses          comma or semicolon separated list of addresses
     * @param caption            email subject
     * @param from               "from" address. If null, a default provided by {@code cuba.email.fromAddress} app property is used.
     * @param templatePath       path to a Freemarker template which is used to create the message body. The template
     *                           is loaded through {@link Resources} in the <b>core</b> module.
     * @param templateParameters map of parameters to be passed to the template
     * @param attachments        email attachments. Omit this parameter if there are no attachments.
     */
    @Deprecated
    public EmailInfo(String addresses, String caption, @Nullable String from, String templatePath,
                     Map<String, Serializable> templateParameters, EmailAttachment... attachments) {
        this.addresses = addresses;
        this.caption = caption;
        this.templatePath = templatePath;
        this.attachments = attachments;
        this.templateParameters = templateParameters;
        this.from = from;
    }

    /**
     * Deprecated. Use {@link EmailInfoBuilder} instead.
     *
     * Constructor. Example usage:
     * <pre>{@code
     *     EmailInfo emailInfo = new EmailInfo(
     *          "john.doe@company.com,jane.roe@company.com",
     *          "Company news",
     *          "do-not-reply@company.com",
     *          EmailInfo.HTML_CONTENT_TYPE,
     *          "com/company/sample/email_templates/news.txt",
     *          Collections.singletonMap("some_var", some_value)
     *     );
     * }</pre>
     *
     * @param addresses          comma or semicolon separated list of addresses
     * @param caption            email subject
     * @param from               "from" address. If null, a default provided by {@code cuba.email.fromAddress} app property is used.
     * @param bodyContentType    email body like "text/plain; charset=UTF-8" or "text/html; charset=UTF-8", etc
     * @param templatePath       path to a Freemarker template which is used to create the message body. The template
     *                           is loaded through {@link Resources} in the <b>core</b> module.
     * @param templateParameters map of parameters to be passed to the template
     * @param attachments        email attachments. Omit this parameter if there are no attachments.
     */
    @Deprecated
    public EmailInfo(String addresses, String caption, @Nullable String from, String bodyContentType,
                     String templatePath, Map<String, Serializable> templateParameters,
                     EmailAttachment... attachments) {
        this.addresses = addresses;
        this.caption = caption;
        this.templatePath = templatePath;
        this.attachments = attachments;
        this.templateParameters = templateParameters;
        this.from = from;
        this.bodyContentType = bodyContentType;
    }

    /**
     * Deprecated. Use {@link EmailInfoBuilder} instead.
     *
     * Constructor.
     * <pre>{@code
     *     EmailInfo emailInfo = new EmailInfo(
     *          "john.doe@company.com,jane.roe@company.com",
     *          "Company news",
     *          null,
     *          "Some content"
     *     );
     * }</pre>
     * <p>
     * If you want to set content body type you can use
     * {@link #EmailInfo(String, String, String, String, String, EmailAttachment...)} instead or use {@code
     * setBodyContentType()} method.
     *
     * @param addresses   comma or semicolon separated list of addresses
     * @param caption     email subject
     * @param from        "from" address. If null, a default provided by {@code cuba.email.fromAddress} app property is used.
     * @param body        email body
     * @param attachments email attachments. Omit this parameter if there are no attachments.
     */
    @Deprecated
    public EmailInfo(String addresses, String caption, @Nullable String from, String body, EmailAttachment... attachments) {
        this.addresses = addresses;
        this.caption = caption;
        this.body = body;
        this.attachments = attachments;
        this.from = from;
    }

    /**
     * Deprecated. Use {@link EmailInfoBuilder} instead.
     *
     * Constructor.
     * <pre>{@code
     *     EmailInfo emailInfo = new EmailInfo(
     *          "john.doe@company.com,jane.roe@company.com",
     *          "Company news",
     *          null,
     *          "Some content",
     *          EmailInfo.TEXT_CONTENT_TYPE
     *     );
     * }</pre>
     *
     * @param addresses       comma or semicolon separated list of addresses
     * @param caption         email subject
     * @param from            "from" address. If null, a default provided by {@code cuba.email.fromAddress} app property is used.
     * @param body            email body
     * @param bodyContentType email body like "text/plain; charset=UTF-8" or "text/html; charset=UTF-8", etc
     * @param attachments     email attachments. Omit this parameter if there are no attachments.
     */
    @Deprecated
    public EmailInfo(String addresses, String caption, @Nullable String from, String body, String bodyContentType,
                     EmailAttachment... attachments) {
        this.addresses = addresses;
        this.caption = caption;
        this.body = body;
        this.bodyContentType = bodyContentType;
        this.attachments = attachments;
        this.from = from;
    }

    /**
     * Deprecated. Use {@link EmailInfoBuilder} instead.
     *
     * Constructor. The "from" address is taken from the {@code cuba.email.fromAddress} app property.
     * <pre>{@code
     *     EmailInfo emailInfo = new EmailInfo(
     *          "john.doe@company.com,jane.roe@company.com",
     *          "Company news",
     *          "Some content"
     *     );
     * }</pre>
     * <p>
     * If you want to set content body type you can use {@link #EmailInfo(String, String, String, String)} instead or
     * use {@code setBodyContentType()} method.
     *
     * @param addresses comma or semicolon separated list of addresses
     * @param caption   email subject
     * @param body      email body
     */
    @Deprecated
    public EmailInfo(String addresses, String caption, String body) {
        this.addresses = addresses;
        this.caption = caption;
        this.body = body;
    }

    /**
     * Deprecated. Use {@link EmailInfoBuilder} instead.
     *
     * Constructor. The "from" address is taken from the {@code cuba.email.fromAddress} app property.
     * <pre>{@code
     *     EmailInfo emailInfo = new EmailInfo(
     *         "john.doe@company.com,jane.roe@company.com",
     *         "Company news",
     *         "Some content",
     *         EmailInfo.TEXT_CONTENT_TYPE"
     *     );
     * }</pre>
     *
     * @param addresses       comma or semicolon separated list of addresses
     * @param caption         email subject
     * @param body            email body
     * @param bodyContentType email body like "text/plain; charset=UTF-8" or "text/html; charset=UTF-8", etc
     */
    @Deprecated
    public EmailInfo(String addresses, String caption, String body, String bodyContentType) {
        this.addresses = addresses;
        this.caption = caption;
        this.body = body;
        this.bodyContentType = bodyContentType;
    }

    /**
     * INTERNAL
     */
    EmailInfo(String addresses,
              String cc,
              String bcc,
              boolean sendInOneMessage,
              String caption,
              String from,
              String templatePath,
              Map<String, Serializable> templateParameters,
              String body,
              String bodyContentType,
              List<EmailHeader> headers,
              EmailAttachment... attachments) {
        this.addresses = addresses;
        this.cc = cc;
        this.bcc = bcc;
        this.sendInOneMessage = sendInOneMessage;
        this.caption = caption;
        this.from = from;
        this.templatePath = templatePath;
        this.templateParameters = templateParameters;
        this.body = body;
        this.bodyContentType = bodyContentType;
        this.headers = headers;
        this.attachments = attachments;

    }

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public EmailAttachment[] getAttachments() {
        return attachments;
    }

    public void setAttachments(EmailAttachment[] attachments) {
        this.attachments = attachments;
    }

    public Map<String, Serializable> getTemplateParameters() {
        return templateParameters;
    }

    public void setTemplateParameters(Map<String, Serializable> templateParameters) {
        this.templateParameters = templateParameters;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<EmailHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<EmailHeader> headers) {
        this.headers = headers;
    }

    public void addHeader(String name, String value) {
        if (this.headers == null)
            this.headers = new ArrayList<>();
        this.headers.add(new EmailHeader(name, value));
    }

    public String getBodyContentType() {
        return bodyContentType;
    }

    public void setBodyContentType(String bodyContentType) {
        this.bodyContentType = bodyContentType;
    }

    public String getCc() {
        return cc;
    }

    /**
     *  Result of this method call (i.e., setting addresses of the email CC field) is ignored during the message creation if
     *  {@link com.haulmont.cuba.core.global.EmailInfo#isSendInOneMessage()} returns {@code false} (default behaviour),
     *  because, in this case, the message is generated for each of the recipients separately.
     */
    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    /**
     *  Result of this method call (i.e., setting addresses of the email BCC field) is ignored during the message creation
     *  if {@link com.haulmont.cuba.core.global.EmailInfo#isSendInOneMessage()} returns {@code false} (default behaviour),
     *  because, in this case, the message is generated for each of the recipients separately.
     */
    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public boolean isSendInOneMessage() {
        return sendInOneMessage;
    }

    public void setSendInOneMessage(boolean sendInOneMessage) {
        this.sendInOneMessage = sendInOneMessage;
    }
}