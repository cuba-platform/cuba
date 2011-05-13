/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 20.07.2010 11:36:54
 *
 * $Id$
 */
package com.haulmont.cuba.web.ui.report;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.report.Report;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.app.ui.report.ReportHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RunReportDatasource extends CollectionDatasourceImpl {
    private static final long serialVersionUID = -4470826840980416614L;

    public RunReportDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

    public RunReportDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, View view) {
        super(context, dataservice, id, metaClass, view);
    }

    public RunReportDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, String viewName, boolean softDeletion) {
        super(context, dataservice, id, metaClass, viewName, softDeletion);
    }

    @Override
    protected void loadData(Map params) {
        User user = (User) params.get("param$user");
        String screen = (String) params.get("param$screen");
        super.loadData(params);
        if (user != null && screen != null) {
            applySecurityPolicies(user, screen);
        } else {
            //todo: if user is null???
        }
    }

    private void applySecurityPolicies(User user, String screen) {
        final List<Report> reports = new ArrayList<Report>(data.values());
        data.clear();
        List<Report> filter = ReportHelper.applySecurityPolicies(user,screen,reports);
        for (Report report : filter) {
            data.put(report.getId(), report);
            attachListener((Instance) report);     
        }
    }
}
