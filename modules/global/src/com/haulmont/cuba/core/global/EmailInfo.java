/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import java.util.Map;
import java.io.Serializable;

/**
 * Contains information about email sending.<br>
 * Used by {@link com.haulmont.cuba.core.app.EmailService#sendEmail(EmailInfo)} method.
 *
 * @author degtyarjov
 * @version $Id$
 */
public class EmailInfo implements Serializable {

    private static final long serialVersionUID = -382773435130109083L;

    private String addresses;
    private String caption;
    private String from;
    private String templatePath;
    private Map<String, Serializable> templateParameters;
    private String body;
    private EmailAttachment[] attachment;

    public EmailInfo(String addresses, String caption, String from, String templatePath,
                     Map<String, Serializable> templateParameters, String body, EmailAttachment... attachment) {

        this.addresses = addresses;
        this.caption = caption;
        this.templatePath = templatePath;
        this.body = body;
        this.attachment = attachment;
        this.templateParameters = templateParameters;
        this.from = from;
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

    public EmailAttachment[] getAttachment() {
        return attachment;
    }

    public void setAttachment(EmailAttachment[] attachment) {
        this.attachment = attachment;
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