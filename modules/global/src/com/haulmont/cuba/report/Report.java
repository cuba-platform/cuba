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
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.Role;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.Iterator;
import java.util.List;

@Entity(name = "report$Report")
@Table(name = "REPORT_REPORT")
@NamePattern("%s|name")
@SystemLevel
@SuppressWarnings("unused")
public class Report extends HardDeleteEntity {
    private static final long serialVersionUID = -2817764915661205093L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "REPORT_TYPE")
    private Integer reportType;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ROOT_DEFINITION_ID")
    @OnDelete(value = DeletePolicy.CASCADE)
    private BandDefinition rootBandDefinition;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @OnDelete(value = DeletePolicy.CASCADE)
    @Aggregation
    private List<ReportTemplate> templates;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @OnDelete(value = DeletePolicy.CASCADE)
    @Aggregation
    @OrderBy("position")
    private List<ReportInputParameter> inputParameters;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @OnDelete(value = DeletePolicy.CASCADE)
    @Aggregation
    private List<ReportValueFormat> valuesFormats;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
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

    public List<ReportValueFormat> getValuesFormats() {
        return valuesFormats;
    }

    public void setValuesFormats(List<ReportValueFormat> valuesFormats) {
        this.valuesFormats = valuesFormats;
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

    public List<ReportTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<ReportTemplate> templates) {
        this.templates = templates;
    }

    /**
     * Get default template for report
     *
     * @return Template
     */
    public ReportTemplate getDefaultTemplate(){
        ReportTemplate template = null;
        if (templates != null) {
            if (templates.size() == 1)
                template = templates.get(0);
            else {
                Iterator<ReportTemplate> iter = templates.iterator();
                while (iter.hasNext() && template == null) {
                    ReportTemplate temp = iter.next();
                    if (temp.getDefaultFlag())
                        template = temp;
                }
            }
        }
        return template;
    }

    public ReportTemplate getTemplateByCode(String templateCode) {
        ReportTemplate template = null;
        if (templates != null) {
            Iterator<ReportTemplate> iter = templates.iterator();
            while (iter.hasNext() && template == null) {
                ReportTemplate temp = iter.next();
                if (StringUtils.equalsIgnoreCase(temp.getCode(), templateCode)) {
                    template = temp;
                }
            }
        }
        return template;
    }
}