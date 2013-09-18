/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import javax.annotation.Nullable;
import javax.mail.Part;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * @author krokhin
 * @version $Id$
 * @see EmailInfo
 * @see com.haulmont.cuba.core.app.EmailService
 */
public class EmailAttachment implements Serializable {
    private static final long serialVersionUID = 8201729520638588939L;

    private final byte[] data;
    private final String name;

    private final String contentId;

    /**
     * @see Part#INLINE
     * @see Part#ATTACHMENT
     */
    private final String disposition;

    /**
     * UTF-8 is the default if not specified.
     */
    private final String encoding;

    /**
     * Create file attachment.
     */
    public EmailAttachment(byte[] data, String name) {
        this(data, name, null, Part.ATTACHMENT, null);
    }

    /**
     * Create inline attachment (e.g. image embedded into html).
     */
    public EmailAttachment(byte[] data, String name, String contentId) {
        this(data, name, contentId, Part.INLINE, null);
    }

    public EmailAttachment(byte[] data, String name, @Nullable String contentId, String disposition,
                           @Nullable String encoding) {
        this.data = data;
        this.name = name;
        this.contentId = contentId;
        this.disposition = disposition;
        this.encoding = encoding;
    }

    public byte[] getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getContentId() {
        return contentId;
    }

    public String getDisposition() {
        return disposition;
    }

    public String getEncoding() {
        return encoding;
    }

    public static EmailAttachment createTextAttachment(String text, String encoding, String name) {
        byte[] data;
        try {
            data = text.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return new EmailAttachment(data, name, null, Part.ATTACHMENT, encoding);
    }
}
