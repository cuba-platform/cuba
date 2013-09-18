/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Map;

/**
 * Contains email details: list of recipients, from address, caption, body and attachments.
 *
 * @author degtyarjov
 * @version $Id$
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

    public EmailInfo(String addresses, String caption, @Nullable String from, String templatePath,
                     Map<String, Serializable> templateParameters, EmailAttachment... attachments) {
        this.addresses = addresses;
        this.caption = caption;
        this.templatePath = templatePath;
        this.attachments = attachments;
        this.templateParameters = templateParameters;
        this.from = from;
    }

    public EmailInfo(String addresses, String caption, @Nullable String from, String body, EmailAttachment... attachments) {
        this.addresses = addresses;
        this.caption = caption;
        this.body = body;
        this.attachments = attachments;
        this.from = from;
    }

    /**
     * Take "from" value from system settings.
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
}