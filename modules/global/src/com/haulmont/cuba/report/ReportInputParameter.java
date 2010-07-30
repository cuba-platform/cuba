/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 14.05.2010 14:27:09
 *
 * $Id$
 */
package com.haulmont.cuba.report;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Entity(name = "report$ReportInputParameter")
@Table(name = "REPORT_INPUT_PARAMETER")
public class ReportInputParameter extends StandardEntity {
    private static final long serialVersionUID = 6231014880104406246L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_ID")
    private Report report;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ALIAS")
    private String alias;

    @Column(name = "POSITION")
    private Integer position;

    @Column(name = "META_CLASS")
    private String entityMetaClass;

    @Column(name = "SCREEN")
    private String screen;

    @Column(name = "CLASS_NAME")
    private String className;

    @Column(name = "FROM_BROWSER")
    private Boolean getFromBrowser = false;

    @Column(name = "REQUIRED")
    private Boolean required = false;

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public ParameterType getType() {
        return type != null ? ParameterType.fromId(type) : null;
    }

    public void setType(ParameterType type) {
        this.type = type != null ? type.getId() : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getEntityMetaClass() {
        return entityMetaClass;
    }

    public void setEntityMetaClass(String entityMetaClass) {
        this.entityMetaClass = entityMetaClass;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Boolean getGetFromBrowser() {
        return getFromBrowser;
    }

    public void setGetFromBrowser(Boolean getFromBrowser) {
        this.getFromBrowser = getFromBrowser;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }
}
