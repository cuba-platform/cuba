/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 07.05.2010 12:04:00
 *
 * $Id$
 */
package com.haulmont.cuba.report;

import com.haulmont.chile.core.annotations.Aggregation;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.security.entity.Role;

import javax.persistence.*;
import java.util.List;

@Entity(name = "report$Report")
@Table(name = "REPORT_REPORT")
@NamePattern("%s|name")
public class Report extends HardDeleteEntity {
    private static final long serialVersionUID = -2817764915661205093L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "REPORT_OUTPUT_TYPE")
    private Integer reportOutputType;

    @Column(name = "IS_CUSTOM")
    private Boolean isCustom = false;

    @Column(name = "CUSTOM_CLASS")
    private String customClass;

    @Column(name = "REPORT_TYPE")
    private Integer reportType;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ROOT_DEFINITION_ID")
    @OnDelete(value = DeletePolicy.CASCADE)
    private BandDefinition rootBandDefinition;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "TEMPLATE_FILE_ID")
    @OnDelete(value = DeletePolicy.CASCADE)
    private FileDescriptor templateFileDescriptor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report", cascade = {CascadeType.PERSIST})
    @OnDelete(value = DeletePolicy.CASCADE)
    @Aggregation
    @OrderBy("position")
    private List<ReportInputParameter> inputParameters;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report", cascade = {CascadeType.PERSIST})
    @OnDelete(value = DeletePolicy.CASCADE)
    @Aggregation
    private List<ReportScreen> reportScreens;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "REPORT_REPORTS_ROLES",
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"),
            joinColumns = @JoinColumn(name = "REPORT_ID", referencedColumnName = "ID")
    )
    private List<Role> roles;

    public BandDefinition getRootBandDefinition() {
        return rootBandDefinition;
    }

    public void setRootBandDefinition(BandDefinition rootBandDefinition) {
        this.rootBandDefinition = rootBandDefinition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ReportInputParameter> getInputParameters() {
        return inputParameters;
    }

    public void setInputParameters(List<ReportInputParameter> inputParameters) {
        this.inputParameters = inputParameters;
    }

    public ReportOutputType getReportOutputType() {
        return reportOutputType != null ? ReportOutputType.fromId(reportOutputType) : null;
    }

    public void setReportOutputType(ReportOutputType reportOutputType) {
        this.reportOutputType = reportOutputType != null ? reportOutputType.getId() : null;
    }

    public Boolean getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(Boolean custom) {
        isCustom = custom;
    }

    public String getCustomClass() {
        return customClass;
    }

    public void setCustomClass(String customClass) {
        this.customClass = customClass;
    }

    public FileDescriptor getTemplateFileDescriptor() {
        return templateFileDescriptor;
    }

    public void setTemplateFileDescriptor(FileDescriptor templateFileDescriptor) {
        this.templateFileDescriptor = templateFileDescriptor;
    }

    public ReportType getReportType() {
        return reportType != null ? ReportType.fromId(reportType) : null;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType != null ? reportType.getId() : null;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<ReportScreen> getReportScreens() {
        return reportScreens;
    }

    public void setReportScreens(List<ReportScreen> reportScreens) {
        this.reportScreens = reportScreens;
    }
}
