/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.SendingStatus;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;
import java.util.List;

/**
 * Entity to store information about sending emails.
 *
 * @author ovchinnikov
 * @version $Id$
 */
@Entity(name = "sys$SendingMessage")
@Table(name = "SYS_SENDING_MESSAGE")
@SystemLevel
public class SendingMessage extends BaseUuidEntity implements Updatable, SoftDelete {

    private static final long serialVersionUID = -8156998515878702538L;

    public SendingMessage() {
        attemptsMade = 0;
        version = 0;
    }

    @Column(name = "VERSION")
    protected Integer version;

    @Column(name = "UPDATE_TS")
    protected Date updateTs;

    @Column(name = "UPDATED_BY", length = LOGIN_FIELD_LEN)
    protected String updatedBy;

    @Column(name = "DELETE_TS")
    protected Date deleteTs;

    @Column(name = "DELETED_BY", length = LOGIN_FIELD_LEN)
    protected String deletedBy;

    @Column(name = "ADDRESS_TO")
    private String address;

    @Column(name = "ADDRESS_FROM")
    private String from;

    @Column(name = "CAPTION")
    private String caption;

    @Column(name = "CONTENT_TEXT")
    private String contentText;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "DATE_SENT")
    private Date dateSent;

    @Column(name = "ATTACHMENTS_NAME")
    private String attachmentsName;

    @Column(name = "DEADLINE")
    private Date deadline;

    @Column(name = "ATTEMPTS_COUNT")
    private Integer attemptsCount;

    @Column(name = "ATTEMPTS_MADE")
    private Integer attemptsMade;


    @OneToMany(mappedBy = "message", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<SendingAttachment> attachments;

    public SendingMessage(Integer attemptsCount) {
        this.attemptsCount = attemptsCount;
    }

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
        this.caption = caption;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getUpdateTs() {
        return updateTs;
    }

    public void setUpdateTs(Date updateTs) {
        this.updateTs = updateTs;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Boolean isDeleted() {
        return deleteTs != null;
    }

    public Date getDeleteTs() {
        return deleteTs;
    }

    public void setDeleteTs(Date deleteTs) {
        this.deleteTs = deleteTs;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }
}
