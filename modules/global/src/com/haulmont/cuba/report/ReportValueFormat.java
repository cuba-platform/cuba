/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Yuryi Artamonov
 * Created: 19.10.2010 16:51:23
 *
 * $Id$
 */
package com.haulmont.cuba.report;

import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.*;

@Entity(name = "report$ReportValueFormat")
@Table(name = "REPORT_VALUE_FORMAT")
@SystemLevel
public class ReportValueFormat extends HardDeleteEntity {

    private static final long serialVersionUID = 680180375698449946L;

    @Column(name = "NAME")
    private String valueName;

    @Column(name = "FORMAT")
    private String formatString;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_ID")
    private Report report;

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getFormatString() {
        return formatString;
    }

    public void setFormatString(String formatString) {
        this.formatString = formatString;
    }
}
