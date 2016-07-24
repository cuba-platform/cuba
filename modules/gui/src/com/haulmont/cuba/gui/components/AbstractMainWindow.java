/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.components.mainwindow.FoldersPane;
import com.haulmont.cuba.gui.components.mainwindow.UserIndicator;

import javax.annotation.Nullable;

/**
 * Base class for controller of application Main window
 */
public class AbstractMainWindow extends AbstractTopLevelWindow implements Window.MainWindow {

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
}