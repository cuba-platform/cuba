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

    /**
     * @deprecated Please use one of other constructors:
     * one which uses template path and parameters,
     * or another which uses pre-formed body.
     */
    @Deprecated
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

    public EmailInfo(String addresses, String caption, @Nullable String from, String templatePath,
                     Map<String, Serializable> templateParameters, EmailAttachment... attachment) {
        this.addresses = addresses;
        this.caption = caption;
        this.templatePath = templatePath;
        this.attachment = attachment;
        this.templateParameters = templateParameters;
        this.from = from;
    }

    public EmailInfo(String addresses, String caption, @Nullable String from, String body, EmailAttachment... attachment) {
        this.addresses = addresses;
        this.caption = caption;
        this.body = body;
        this.attachment = attachment;
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