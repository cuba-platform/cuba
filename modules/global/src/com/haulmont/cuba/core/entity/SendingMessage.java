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

package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.SendingStatus;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;
import java.util.List;

/**
 * Entity to store information about sending emails.
 *
 */
@Entity(name = "sys$SendingMessage")
@Table(name = "SYS_SENDING_MESSAGE")
@SystemLevel
public class SendingMessage extends StandardEntity {

    private static final long serialVersionUID = -8156998515878702538L;

    public static final int CAPTION_LENGTH = 500;
    public static final int BODY_CONTENT_TYPE_LENGTH = 50;
    public static final String HEADERS_SEPARATOR = "\n";

    @Column(name = "ADDRESS_TO")
    protected String address;

    @Column(name = "ADDRESS_FROM")
    protected String from;

    @Column(name = "CAPTION", length = CAPTION_LENGTH)
    protected String caption;

    /**
     * Email body is stored either in this field or in {@link #contentTextFile}.
     */
    @Column(name = "CONTENT_TEXT")
    protected String contentText;

    @JoinColumn(name = "CONTENT_TEXT_FILE_ID")
    @OneToOne(fetch = FetchType.LAZY)
    protected FileDescriptor contentTextFile;

    @Column(name = "STATUS")
    protected Integer status;

    @Column(name = "DATE_SENT")
    protected Date dateSent;

    @Column(name = "ATTACHMENTS_NAME")
    protected String attachmentsName;

    @Column(name = "DEADLINE")
    protected Date deadline;

    @Column(name = "ATTEMPTS_COUNT")
    protected Integer attemptsCount;

    @Column(name = "ATTEMPTS_MADE")
    protected Integer attemptsMade;

    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY)
    protected List<SendingAttachment> attachments;

    @Column(name = "EMAIL_HEADERS")
    protected String headers;

    @Column(name = "BODY_CONTENT_TYPE", length = BODY_CONTENT_TYPE_LENGTH)
    protected String bodyContentType;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public SendingStatus getStatus() {
        return status == null ? null : SendingStatus.fromId(status);
    }

    public void setStatus(SendingStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public Integer getAttemptsCount() {
        return attemptsCount;
    }

    public void setAttemptsCount(Integer attemptsCount) {
        this.attemptsCount = attemptsCount;
    }

    public String getAttachmentsName() {
        return attachmentsName;
    }

    public void setAttachmentsName(String attachmentsName) {
        this.attachmentsName = attachmentsName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setCaption(String caption) {
        this.caption = StringUtils.substring(caption, 0, SendingMessage.CAPTION_LENGTH);
    }

    public String getCaption() {
        return caption;
    }

    public List<SendingAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<SendingAttachment> attachments) {
        this.attachments = attachments;
    }

    public Integer getAttemptsMade() {
        return attemptsMade;
    }

    public void setAttemptsMade(Integer attemptsMade) {
        this.attemptsMade = attemptsMade;
    }

    public FileDescriptor getContentTextFile() {
        return contentTextFile;
    }

    public void setContentTextFile(FileDescriptor contentTextFile) {
        this.contentTextFile = contentTextFile;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getBodyContentType() {
        return bodyContentType;
    }

    public void setBodyContentType(String bodyContentType) {
        this.bodyContentType = bodyContentType;
    }
}
