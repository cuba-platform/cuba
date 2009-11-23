/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.10.2009 12:24:21
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.core.file;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TableActionsHelper;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.app.FileDownloadHelper;
import com.haulmont.cuba.web.rpt.WebExportDisplay;

import java.util.Map;

public class FileBrowser extends AbstractWindow {

    private CollectionDatasource ds;

    public FileBrowser(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);
        Table filesTable = getComponent("files");
        TableActionsHelper helper = new TableActionsHelper(this, filesTable);
        helper.createRefreshAction();
        helper.createCreateAction();
        helper.createEditAction();
        helper.createRemoveAction();
        helper.createExcelAction(new WebExportDisplay());

        FileDownloadHelper.initGeneratedColumn(filesTable);
    }
}
