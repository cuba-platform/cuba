/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.haulmont.cuba.gui.components.mainwindow.FoldersPane;
import com.haulmont.cuba.web.app.folders.CubaFoldersPane;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import org.apache.commons.lang.StringUtils;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebFoldersPane extends WebAbstractComponent<CubaFoldersPane> implements FoldersPane {

    protected String styleName;

    public WebFoldersPane() {
        component = new CubaFoldersPane();
    }

    @Override
    public void setStyleName(String styleName) {
        if (StringUtils.isNotEmpty(this.styleName)) {
            getComposition().removeStyleName(this.styleName);
        }

        this.styleName = styleName;

        if (StringUtils.isNotEmpty(styleName)) {
            getComposition().addStyleName(styleName);
        }
    }

    @Override
    public void loadFolders() {
        component.loadFolders();
    }
}