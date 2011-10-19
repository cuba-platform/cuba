/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.report.actions;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.report.ReportHelper;
import com.haulmont.cuba.report.Report;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class RunReportAction extends AbstractAction {

    private final Window window;

    public RunReportAction(Window window, String captionId) {
        super(captionId);

        checkNotNull(window);

        this.window = window;
    }

    @Override
    public void actionPerform(Component component) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("screen", window.getId());

        window.openLookup("report$Report.run", new Window.Lookup.Handler() {

            @Override
            public void handleLookup(Collection items) {
                if (items != null && items.size() > 0) {
                    Report report = (Report) items.iterator().next();
                    report = window.getDsContext().getDataService().reload(report, "report.edit");
                    if (report != null) {
                        if (report.getInputParameters() != null && report.getInputParameters().size() > 0) {
                            openReportParamsDialog(report, window);
                        } else {
                            ReportHelper.printReport(report, Collections.<String, Object>emptyMap());
                        }
                    }
                }
            }
        }, WindowManager.OpenType.DIALOG, params);
    }

    @Override
    public String getCaption() {
        return window.getMessage(getId());
    }

    private void openReportParamsDialog(Report report, Window window) {
        window.openWindow("report$inputParameters", WindowManager.OpenType.DIALOG,
                Collections.<String, Object>singletonMap("report", report));
    }
}