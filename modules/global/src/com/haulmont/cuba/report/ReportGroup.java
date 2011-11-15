/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.report;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.MessageProvider;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@Entity(name = "report$ReportGroup")
@Table(name = "REPORT_GROUP")
@NamePattern("%s|locName")
@SystemLevel
@SuppressWarnings("unused")
public class ReportGroup extends HardDeleteEntity {

    private static final long serialVersionUID = 5399528790289039413L;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CODE")
    private String code;

    @OneToMany(mappedBy = "group")
    private Set<Report> reports;

    public String getTitle() {
        return title;
    }

    @MetaProperty
    public String getLocName() {
        if (StringUtils.isEmpty(code))
            return title;
        else
            return MessageProvider.getMessage(ReportGroup.class, code);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }
}