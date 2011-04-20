/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Sergey Ovchinnikov
 * Created: 26.01.11 16:53
 *
 * $Id$
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.global.EmailAttachment;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity(name = "sys$SendingAttachment")
@Table(name = "SYS_SENDING_ATTACHMENT")
public class SendingAttachment extends BaseUuidEntity {

    private static final long serialVersionUID = -8253918579521701435L;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "MESSAGE_ID")
    private SendingMessage message;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CONTENT")
    private byte[] content;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CONTENT_ID")
    private String contentId;

    public SendingAttachment(EmailAttachment ea, SendingMessage message) {
        super();
        this.content = ea.getData();
        this.contentId = ea.getContentId();
        this.name = ea.getName();
        this.message = message;
    }

    public SendingAttachment() {
        super();
    }

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
}
