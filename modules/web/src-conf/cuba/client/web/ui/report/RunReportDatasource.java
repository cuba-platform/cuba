/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 20.07.2010 11:36:54
 *
 * $Id$
 */
package cuba.client.web.ui.report;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.report.Report;
import com.haulmont.cuba.report.ReportScreen;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private List<Report> checkRoles(User user, List<Report> reports) {
        List<Report> filter = new ArrayList<Report>();
        data.clear();
        for (Report report : reports) {
            List<Role> reportRoles = report.getRoles();
            if (reportRoles == null || reportRoles.size() == 0) {
                filter.add(report);
                attachListener((Instance) report);
            } else {
                Set<UserRole> userRoles = user.getUserRoles();
                for (UserRole userRole : userRoles) {
                    if (reportRoles.contains(userRole.getRole()) ||
                            Boolean.TRUE.equals(userRole.getRole().getSuperRole())) {
                        filter.add(report);
                        break;
                    }
                }
            }
        }
        return filter;
    }

    private List<Report> checkScreens(User user, List<Report> reports, String screen) {
        List<Report> filter = new ArrayList<Report>();
        for (Report report : reports) {
            List<ReportScreen> reportScreens = report.getReportScreens();
            List<String> reportScreensAliases = new ArrayList<String>();
            for (ReportScreen reportScreen : reportScreens) {
                reportScreensAliases.add(reportScreen.getScreenId());
            }

            if ((reportScreensAliases.contains(screen) || reportScreensAliases.size() == 0))
                filter.add(report);
        }
        return filter;
    }

    private void applySecurityPolicies(User user, String screen) {
        final List<Report> reports = new ArrayList<Report>(data.values());
        data.clear();
        List<Report> filter = checkRoles(user, reports);
        filter = checkScreens(user,filter,screen);
        for (Report report : filter) {
            data.put(report.getId(), report);
            attachListener((Instance) report);     
        }
    }
}
