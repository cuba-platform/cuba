/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.serverlogviewer;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.sys.logging.LogArchiver;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.web.export.LogDataProvider;

import javax.inject.Inject;
import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class LogDownloadOptionsWindow extends AbstractWindow {

    private static final int BYTES_IN_MB = 1024 * 1024;

    @Inject
    protected Button downloadFullBtn;

    @Inject
    protected Button downloadTailBtn;

    @Inject
    protected LookupField remoteContextField;

    @Inject
    protected BoxLayout remoteContextBox;

    @Inject
    protected Label sizeNotificationLabel;

    @WindowParam(required = true)
    protected JmxInstance connection;

    @WindowParam(required = true)
    protected String logFileName;

    @WindowParam
    protected List<String> remoteContextList;

    @WindowParam(required = true)
    protected Long logFileSize;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams().setWidthAuto();

        if (remoteContextList == null || remoteContextList.isEmpty()) {
            remoteContextBox.setVisible(false);
        } else {
            List<String> contexts = new ArrayList<>(remoteContextList);
            Collections.sort(contexts, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    if ((o1.contains("core") && !o2.contains("core"))
                            || (o2.contains("portal") && !o1.contains("portal"))) {
                        return -1;
                    }

                    if ((o2.contains("core") && !o1.contains("core"))
                            || (o1.contains("portal") && !o2.contains("portal"))) {
                        return 1;
                    }

                    return o1.compareTo(o2);
                }
            });
            remoteContextField.setOptionsList(contexts);
            remoteContextField.setValue(contexts.get(0));
        }

        if (logFileSize <= LogArchiver.LOG_TAIL_FOR_PACKING_SIZE) {
            downloadTailBtn.setVisible(false);
            downloadFullBtn.setCaption(getMessage("log.download"));
        } else {
            long sizeMb = logFileSize / BYTES_IN_MB;

            sizeNotificationLabel.setValue(formatMessage("log.selectDownloadOption", sizeMb));
        }
    }

    public void cancel() {
        close(CLOSE_ACTION_ID);
    }

    public void downloadTail() {
        LogDataProvider logDataProvider;
        if (remoteContextBox.isVisible()) {
            logDataProvider = new LogDataProvider(connection, logFileName,
                    (String) remoteContextField.getValue(), false);
        } else {
            logDataProvider = new LogDataProvider(connection, logFileName, false);
        }

        exportFile(logDataProvider, logFileName);

        close(CLOSE_ACTION_ID);
    }

    public void downloadFull() {
        LogDataProvider logDataProvider;
        if (remoteContextBox.isVisible()) {
            logDataProvider = new LogDataProvider(connection, logFileName,
                    (String) remoteContextField.getValue(), true);
        } else {
            logDataProvider = new LogDataProvider(connection, logFileName, true);
        }

        exportFile(logDataProvider, logFileName);

        close(CLOSE_ACTION_ID);
    }

    protected void exportFile(LogDataProvider logDataProvider, String fileName) {
        AppConfig.createExportDisplay(this).show(logDataProvider, fileName + ".zip");
    }
}