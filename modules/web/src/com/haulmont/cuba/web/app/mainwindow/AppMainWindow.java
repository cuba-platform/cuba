/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.mainwindow;

import com.haulmont.cuba.core.global.FtsConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.mainwindow.AppMenu;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.components.mainwindow.FoldersPane;
import com.haulmont.cuba.gui.components.mainwindow.FtsField;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaHorizontalSplitPanel;
import com.vaadin.server.Sizeable;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class AppMainWindow extends AbstractMainWindow {

    @Inject
    protected AppMenu mainMenu;

    @Inject
    protected AppWorkArea workArea;

    @Inject
    protected BoxLayout titleBar;

    @Inject
    protected FoldersPane foldersPane;

    @Inject
    protected SplitPanel foldersSplit;

    @Inject
    protected FtsField ftsField;

    @Inject
    protected Embedded logoImage;

    @Inject
    protected WebConfig webConfig;

    @Inject
    protected FtsConfig ftsConfig;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        mainMenu.requestFocus();

        String logoImagePath = messages.getMainMessage("application.logoImage");
        if (StringUtils.isNotBlank(logoImagePath) && !"application.logoImage".equals(logoImagePath)) {
            logoImage.setSource("theme://" + logoImagePath);
        }

        if (webConfig.getUseInverseHeader()) {
            titleBar.setStyleName("cuba-app-menubar cuba-inverse-header");
        }

        if (!ftsConfig.getEnabled()) {
            ftsField.setVisible(false);
        }

        if (webConfig.getFoldersPaneEnabled()) {
            if (webConfig.getFoldersPaneVisibleByDefault()) {
                foldersSplit.setSplitPosition(webConfig.getFoldersPaneDefaultWidth(), Component.UNITS_PIXELS);
            } else {
                foldersSplit.setSplitPosition(0);
            }

            CubaHorizontalSplitPanel vSplitPanel = (CubaHorizontalSplitPanel) WebComponentsHelper.unwrap(foldersSplit);
            vSplitPanel.setDefaultPosition(webConfig.getFoldersPaneDefaultWidth() + "px");
            vSplitPanel.setMaxSplitPosition(50, Sizeable.Unit.PERCENTAGE);
            vSplitPanel.setDockable(true);
        } else {
            foldersPane.setEnabled(false);
            foldersPane.setVisible(false);

            foldersSplit.remove(workArea);

            int foldersSplitIndex = indexOf(foldersSplit);

            remove(foldersSplit);
            add(workArea, foldersSplitIndex);

            expand(workArea);
        }
    }
}