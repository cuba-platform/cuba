/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.report;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.report.locale.ReportLocaleHelper;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
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

    @Column(name = "LOCALE_NAMES")
    private String localeNames;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private Set<Report> reports;

    @Transient
    private String localeName;

    public String getTitle() {
        return title;
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

    public String getLocaleNames() {
        return localeNames;
    }

    public void setLocaleNames(String localeNames) {
        this.localeNames = localeNames;
    }

    @MetaProperty
    public String getLocName() {
        if (localeName == null) {
            localeName = ReportLocaleHelper.getLocalizedName(localeNames);
            if (localeName == null)
                localeName = title;
        }
        return localeName;
    }

    @MetaProperty
    public Boolean getSystemFlag() {
        return StringUtils.isNotEmpty(code);
    }
}