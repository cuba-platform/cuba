/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * <p>$Id$</p>
 *
 * @author ovchinnikov
 */
@Entity(name = "sys$SendingAttachment")
@Table(name = "SYS_SENDING_ATTACHMENT")
@SystemLevel
public class SendingAttachment extends StandardEntity {
    private static final long serialVersionUID = -8253918579521701435L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MESSAGE_ID")
    protected SendingMessage message;

    /**
     * Attachment data is stored either in this field or in {@link #contentFile}.
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CONTENT")
    protected byte[] content;

    @JoinColumn(name = "CONTENT_FILE_ID")
    @OneToOne(fetch = FetchType.LAZY)
    protected FileDescriptor contentFile;

    @Column(name = "NAME", length = 500)
    protected String name;

    @Column(name = "CONTENT_ID", length = 50)
    protected String contentId;

    @Column(name = "DISPOSITION", length = 50)
    protected String disposition;

    @Column(name = "TEXT_ENCODING", length = 50)
    protected String encoding;

    public SendingMessage getMessage() {
        return message;
    }

    public void setMessage(SendingMessage message) {
        this.message = message;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public FileDescriptor getContentFile() {
        return contentFile;
    }

    public void setContentFile(FileDescriptor contentFile) {
        this.contentFile = contentFile;
    }
}
