/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.google.common.base.Strings;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.theme.ThemeConstants;

import javax.inject.Inject;
import java.util.Map;

/**
 * Window for editing new filter name
 * @author gorbunkov
 * @version $Id$
 */
public class SaveFilterWindow extends AbstractWindow {

    @Inject
    protected TextField filterName;

    @Inject
    protected ThemeConstants theme;

    @Override
    public void init(Map<String, Object> params) {
        getDialogParams()
                .setWidth(theme.getInt("cuba.gui.saveFilterWindow.dialog.width"));
        super.init(params);
        String filterNameParam = (String) params.get("filterName");
        if (!Strings.isNullOrEmpty(filterNameParam)) {
            filterName.setValue(filterNameParam);
        }
    }

    public void commit() {
        if (Strings.isNullOrEmpty(filterName.getValue())) {
            showNotification(getMessage("SaveFilter.fillName"), NotificationType.WARNING);
            return;
        }
        close(Window.COMMIT_ACTION_ID);
    }

    public void cancel() {
        close(Window.CLOSE_ACTION_ID);
    }

    public String getFilterName() {
        return filterName.getValue();
    }
}