package com.haulmont.cuba.report;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

/*
* Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
* Haulmont Technology proprietary and confidential.
* Use is subject to license terms.

* Author: FONTANENKO VASILIY
* Created: 19.07.2010 18:32:28
*
* $Id$
*/

@Entity(name = "report$ReportScreen")
@Table(name = "REPORT_REPORT_SCREEN")
public class ReportScreen extends HardDeleteEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_ID")
    private Report report;

    @Column(name="SCREEN_ID")
    private String screenId;

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }
}
