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

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.TableActionsHelper;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.app.FileDownloadHelper;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.gui.components.Table;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

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

        ds = filesTable.getDatasource();
        MetaPropertyPath nameProperty = ds.getMetaClass().getPropertyEx("name");
        final com.vaadin.ui.Table table = (com.vaadin.ui.Table) WebComponentsHelper.unwrap(filesTable);

        table.addGeneratedColumn(nameProperty, new com.vaadin.ui.Table.ColumnGenerator() {
            public Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
                final FileDescriptor fd = (FileDescriptor) ds.getItem(itemId);

                String link = FileDownloadHelper.makeLink(fd, true, false);
                Label label = new Label(link);
                label.setContentMode(Label.CONTENT_XHTML);

                return label;
            }
        });
    }
}
