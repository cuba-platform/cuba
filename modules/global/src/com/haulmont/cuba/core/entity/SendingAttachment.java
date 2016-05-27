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

import javax.persistence.*;
import javax.persistence.Entity;

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