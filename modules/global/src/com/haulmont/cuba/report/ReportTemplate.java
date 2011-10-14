/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.report;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

/**
 * Template for {@link Report}
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@Entity(name = "report$ReportTemplate")
@Table(name = "REPORT_TEMPLATE")
@SystemLevel
@SuppressWarnings("unused")
public class ReportTemplate extends HardDeleteEntity {

    private static final long serialVersionUID = 3692751073234357754L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_ID")
    private Report report;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "TEMPLATE_FILE_ID")
    @OnDelete(value = DeletePolicy.CASCADE)
    private FileDescriptor templateFileDescriptor;

    @Column(name = "OUTPUT_TYPE")
    private Integer reportOutputType;

    @Column(name = "CODE")
    private String code;

    @Column(name = "IS_DEFAULT")
    private Boolean defaultFlag;

    @Column(name = "IS_CUSTOM")
    private Boolean customFlag = false;

    @Column(name = "CUSTOM_CLASS")
    private String customClass;

    public FileDescriptor getTemplateFileDescriptor() {
        return templateFileDescriptor;
    }

    public void setTemplateFileDescriptor(FileDescriptor templateFileDescriptor) {
        this.templateFileDescriptor = templateFileDescriptor;
    }

    public ReportOutputType getReportOutputType() {
        return reportOutputType != null ? ReportOutputType.fromId(reportOutputType) : null;
    }

    public void setReportOutputType(ReportOutputType reportOutputType) {
        this.reportOutputType = reportOutputType != null ? reportOutputType.getId() : null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Boolean defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Boolean getCustomFlag() {
        return customFlag;
    }

    public void setCustomFlag(Boolean customFlag) {
        this.customFlag = customFlag;
    }

    public String getCustomClass() {
        return customClass;
    }

    public void setCustomClass(String customClass) {
        this.customClass = customClass;
    }
}
