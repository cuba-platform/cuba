/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Contains email details: list of recipients, from address, caption, body and attachments.
 * See constructors for more information.
 *
 * @author degtyarjov
 * @see com.haulmont.cuba.core.app.EmailService
 */
public class EmailInfo implements Serializable {

    private static final long serialVersionUID = -382773435130109083L;

    /**
     * Recipient email addresses separated with "," or ";" symbol.
     */
    private String addresses;
    private String caption;
    private String from;
    private String templatePath;
    private Map<String, Serializable> templateParameters;
    private String body;
    private EmailAttachment[] attachments;
    private List<EmailHeader> headers;

    /**
     * Constructor. Example usage:
     * <pre>
     *     EmailInfo emailInfo = new EmailInfo(
                "john.doe@company.com,jane.roe@company.com",
                "Company news",
                "do-not-reply@company.com",
                "com/company/sample/email_templates/news.txt",
                Collections.singletonMap("some_var", some_value)
            );
     * </pre>
     *
     * @param addresses             comma or semicolon separated list of addresses
     * @param caption               email subject
     * @param from                  "from" address. If null, a default provided by {@code cuba.email.fromAddress} app property is used.
     * @param templatePath          path to a Freemarker template which is used to create the message body. The template
     *                              is loaded through {@link Resources} in the <b>core</b> module.
     * @param templateParameters    map of parameters to be passed to the template
     * @param attachments           email attachments. Omit this parameter if there are no attachments.
     */
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
     * Constructor.
     * <pre>
     *     EmailInfo emailInfo = new EmailInfo(
                "john.doe@company.com,jane.roe@company.com",
                "Company news",
                null,
                "Some content"
            );
     * </pre>
     *
     * @param addresses             comma or semicolon separated list of addresses
     * @param caption               email subject
     * @param from                  "from" address. If null, a default provided by {@code cuba.email.fromAddress} app property is used.
     * @param body                  email body
     * @param attachments           email attachments. Omit this parameter if there are no attachments.
     */
    public EmailInfo(String addresses, String caption, @Nullable String from, String body, EmailAttachment... attachments) {
        this.addresses = addresses;
        this.caption = caption;
        this.body = body;
        this.attachments = attachments;
        this.from = from;
    }

    /**
     * Constructor. The "from" address is taken from the {@code cuba.email.fromAddress} app property.
     * <pre>
     *     EmailInfo emailInfo = new EmailInfo(
                "john.doe@company.com,jane.roe@company.com",
                "Company news",
                "Some content"
            );
     * </pre>
     *
     * @param addresses             comma or semicolon separated list of addresses
     * @param caption               email subject
     * @param body                  email body
     */
    public EmailInfo(String addresses, String caption, String body) {
        this.addresses = addresses;
        this.caption = caption;
        this.body = body;
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
}