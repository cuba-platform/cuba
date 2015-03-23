/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.statistics;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Map;

/**
 * @author krivenko
 * @version $Id$
 */
public class ThreadsMonitoringWindow extends AbstractWindow {

    @Inject
    protected ThreadsDatasource threadsDs;

    @Inject
    protected LinkButton nodeLink;

    @Inject
    protected Table threadsTable;

    private JmxInstance node;

    @Override
    public void init(Map<String, Object> params) {
        threadsDs.refresh(params);
        threadsDs.addListener(new DsListenerAdapter<ThreadSnapshot>() {
            @Override
            public void itemChanged(Datasource<ThreadSnapshot> ds, @Nullable ThreadSnapshot prevItem, @Nullable ThreadSnapshot item) {
                updateStacktrace(item);
            }
        });
        node = (JmxInstance)params.get("node");
        nodeLink.setCaption(node.getCaption());
        threadsTable.getColumn("cpu").setFormatter(new PercentFormatter());
    }

    protected void updateStacktrace(ThreadSnapshot item) {
        if (item!=null) {
            item.setStackTrace(threadsDs.getStackTrace(item.getThreadId()));
        }
    }

    public void onRefresh(Timer timer) {
        threadsDs.refresh();
        updateStacktrace(threadsDs.getItem());
    }

    public void viewNode() {
        openEditor("sys$JmxInstance.edit", node,  WindowManager.OpenType.DIALOG);
    }

    protected static class PercentFormatter implements Formatter {
        @Override
        public String format(Object value) {
            String res=null;
            if (value instanceof Double) {
                double doubleValue = (double)value;
                res = String.format("%.1f %%", doubleValue * 100);
            }
            return res !=null ? res : (value!=null ? value.toString() : null);
        }
    }
}
