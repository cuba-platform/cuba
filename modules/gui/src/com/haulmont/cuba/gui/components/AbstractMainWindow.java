/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.components.mainwindow.FoldersPane;
import com.haulmont.cuba.gui.components.mainwindow.UserIndicator;

import javax.annotation.Nullable;

/**
 * Base class for controller of application Main window
 *
 * @author artamonov
 * @version $Id$
 */
public class AbstractMainWindow extends AbstractWindow implements Window.MainWindow {

    private AppWorkArea workArea;
    private UserIndicator userIndicator;
    private FoldersPane foldersPane;

    @Override
    @Nullable
    public AppWorkArea getWorkArea() {
        return workArea;
    }

    public void setWorkArea(AppWorkArea workArea) {
        this.workArea = workArea;
    }

    @Override
    @Nullable
    public UserIndicator getUserIndicator() {
        return userIndicator;
    }

    public void setUserIndicator(UserIndicator userIndicator) {
        this.userIndicator = userIndicator;
    }

    @Nullable
    @Override
    public FoldersPane getFoldersPane() {
        return foldersPane;
    }

    public void setFoldersPane(FoldersPane foldersPane) {
        this.foldersPane = foldersPane;
    }

    @Override
    public boolean close(String actionId) {
        throw new UnsupportedOperationException("Close operation for Main window is unsupported");
    }

    @Override
    public boolean close(String actionId, boolean force) {
        throw new UnsupportedOperationException("Close operation for Main window is unsupported");
    }

    @Override
    public void closeAndRun(String actionId, Runnable runnable) {
        throw new UnsupportedOperationException("Close operation for Main window is unsupported");
    }
}